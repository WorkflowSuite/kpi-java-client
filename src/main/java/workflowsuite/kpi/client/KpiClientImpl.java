package workflowsuite.kpi.client;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import javax.net.SocketFactory;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import workflowsuite.kpi.client.rabbitmq.RabbitConfigurationProvider;
import workflowsuite.kpi.client.rabbitmq.RabbitProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.time.ServerTimeProvider;
import workflowsuite.kpi.client.time.TimeOffsetCalculator;
import workflowsuite.kpi.client.time.TimeServerConfiguration;
import workflowsuite.kpi.client.time.TimeServerConfigurationProvider;
import workflowsuite.kpi.client.time.TimeSynchronizer;

public final class KpiClientImpl implements KpiClient {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;
    private static final long SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(KpiClientImpl.class);


    private final CheckpointMessageBuffer buffer;

    private final Thread consumeMessagesThread;
    private final MessageProducer messageProducer;
    private final TimeSynchronizer timeSynchronizer;

    /**
     * Create new instance of kpi client.
     * @param serviceRegistryUri The address where the service registry is deployed.
     */
    KpiClientImpl(URI serviceRegistryUri, SocketFactory socketFactory) {
        ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient(serviceRegistryUri, loggerFactory);

        ConfigurationProvider<TimeServerConfiguration> timeServerConfiguration =
                new TimeServerConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME);
        this.timeSynchronizer = new TimeSynchronizer(
                timeServerConfiguration,
                new ServerTimeProvider(timeServerConfiguration, socketFactory),
                new TimeOffsetCalculator());

        this.buffer = new CheckpointMessageBuffer(DEFAULT_BUFFER_SIZE, loggerFactory);
        this.messageProducer = new RabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME,
                        RabbitConfigurationProvider.KPI_GENERAL_QUEUE_CONTRACT),
                loggerFactory, socketFactory);
        this.consumeMessagesThread = new Thread(this::consumeMessages);
        this.consumeMessagesThread.setName("KPI_GENERAL_CONSUME_MESSAGE");
        this.consumeMessagesThread.start();
    }

    /**
     * Registers a checkpoint crossing fact for an explicitly specified session.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    @Override
    public boolean onCheckpoint(String checkpointCode, String sessionId) {
        LOG.debug("Entering onCheckpoint(checkpointCode={}, sessionId = {})", checkpointCode, sessionId);
        if (checkpointCode == null || checkpointCode.isEmpty()) {
            throw new IllegalArgumentException("Checkpoint code has no content");
        }
        if (sessionId == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("Session id has no content");
        }
        Instant now = Instant.now();
        CheckpointMessage message = new CheckpointMessage();
        message.setCheckpointCode(checkpointCode);
        message.setSessionId(sessionId);
        message.setClientEventTime(now);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        boolean result = this.buffer.offer(message);
        LOG.debug("Leaving onCheckpoint(): {}", result);
        return result;
    }

    /**
     * Notifies that specified checkpoint is unreachable in the current execution path
     * and all started rules which should be ended in the specified checkpoint should be cancelled.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    @Override
    public boolean unreachableCheckpoint(String checkpointCode, String sessionId) {
        LOG.debug("Entering unreachableCheckpoint(checkpointCode={}, sessionId = {})", checkpointCode, sessionId);
        if (checkpointCode == null || checkpointCode.isEmpty()) {
            throw new IllegalArgumentException("Checkpoint code has no content");
        }
        if (sessionId == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("Session id has no content");
        }
        Instant now = Instant.now();
        CheckpointMessage message = new CheckpointMessage();
        message.setCheckpointCode(checkpointCode);
        message.setSessionId(sessionId);
        message.setClientEventTime(now);
        message.setUnreachable(true);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        boolean result = this.buffer.offer(message);
        LOG.debug("Leaving unreachableCheckpoint(): {}", result);
        return result;
    }

    /**
     * Trace duration metric.
     * @param metricCode Unique metric code. If the code is invalid, the method does nothing.
     * @param duration Value of metric.
     * @return {@code true}
     */
    @Override
    public boolean traceDuration(String metricCode, Duration duration) {
        LOG.debug("Entering traceDuration(metricCode={}, value = {} ms)", metricCode, duration);
        if (metricCode == null || metricCode.isEmpty()) {
            throw new IllegalArgumentException("Metric code has no content");
        }
        if (duration == null) {
            throw new IllegalArgumentException("Duration is null");
        }
        Instant now = Instant.now();
        DurationMetricMessage message = new DurationMetricMessage();
        message.setMetricCode(metricCode);
        message.setDuration(duration);
        message.setClientEventTime(now);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        boolean result = true;
        LOG.debug("Leaving traceDuration(): {}", result);
        return true;
    }

    private void consumeMessages() {
        while (true) {
            try {
                CheckpointMessage message = this.buffer.poll();
                LOG.debug("Poll kpi message from buffer checkpointCode = {} sessionId = {}",
                        message.getCheckpointCode(), message.getSessionId());
                if (!this.messageProducer.trySendMessage(message)) {
                    LOG.warn("Can not send kpi message checkpointCode = {} sessionId = {}",
                            message.getCheckpointCode(), message.getSessionId());
                    Thread.sleep(SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS);
                } else {
                    LOG.debug("Remove kpi message from buffer checkpointCode = {} sessionId = {}",
                            message.getCheckpointCode(), message.getSessionId());
                    this.buffer.remove(message);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
