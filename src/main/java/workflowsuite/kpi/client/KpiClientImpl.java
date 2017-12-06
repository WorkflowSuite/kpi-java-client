package workflowsuite.kpi.client;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import javax.net.SocketFactory;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import workflowsuite.kpi.client.rabbitmq.CheckpointRabbitProducer;
import workflowsuite.kpi.client.rabbitmq.DurationMetricRabbitProducer;
import workflowsuite.kpi.client.rabbitmq.RabbitConfigurationProvider;
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

    private final TimeSynchronizer timeSynchronizer;

    private final CircleBuffer<CheckpointMessage> checkpointMessageBuffer;
    private final Thread checkpointMessagesConsumeThread;
    private final CheckpointRabbitProducer checkpointMessageProducer;

    private final CircleBuffer<DurationMetricMessage> durationMetricBuffer;
    private final Thread durationMetricConsumeThread;
    private final DurationMetricRabbitProducer durationMetricMessageProducer;

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

        this.checkpointMessageBuffer = new CircleBuffer<>(CheckpointMessage.class, DEFAULT_BUFFER_SIZE, loggerFactory);
        this.checkpointMessageProducer = new CheckpointRabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME,
                        RabbitConfigurationProvider.KPI_GENERAL_QUEUE_CONTRACT),
                loggerFactory, socketFactory);
        this.checkpointMessagesConsumeThread = new Thread(this::consumeCheckpointMessages);
        this.checkpointMessagesConsumeThread.setName("KPI_GENERAL_CONSUME_MESSAGE");
        this.checkpointMessagesConsumeThread.start();

        this.durationMetricBuffer = new CircleBuffer<DurationMetricMessage>(DurationMetricMessage.class,
                DEFAULT_BUFFER_SIZE, loggerFactory);
        this.durationMetricMessageProducer = new DurationMetricRabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME,
                        RabbitConfigurationProvider.KPI_DURATION_METRIC_QUEUE_CONTRACT),
                loggerFactory, socketFactory);

        this.durationMetricConsumeThread = new Thread(this::consumeDurationMetricMessages);
        this.durationMetricConsumeThread.setName("KPI_DURATION_METRIC_CONSUME_MESSAGE");
        this.durationMetricConsumeThread.start();
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

        boolean result = this.checkpointMessageBuffer.offer(message);
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

        boolean result = this.checkpointMessageBuffer.offer(message);
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

        boolean result = this.durationMetricBuffer.offer(message);
        LOG.debug("Leaving traceDuration(): {}", result);
        return true;
    }

    private void consumeCheckpointMessages() {
        while (true) {
            try {
                CheckpointMessage message = this.checkpointMessageBuffer.poll();
                LOG.debug("Poll message from checkpoint message buffer checkpointCode = {} sessionId = {}",
                        message.getCheckpointCode(), message.getSessionId());
                if (!this.checkpointMessageProducer.trySendMessage(message)) {
                    LOG.warn("Can not send checkpoint message checkpointCode = {} sessionId = {}",
                            message.getCheckpointCode(), message.getSessionId());
                    Thread.sleep(SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS);
                } else {
                    LOG.debug("Remove message from checkpoint message buffer checkpointCode = {} sessionId = {}",
                            message.getCheckpointCode(), message.getSessionId());
                    this.checkpointMessageBuffer.remove(message);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void consumeDurationMetricMessages() {
        while (true) {
            try {
                DurationMetricMessage message = this.durationMetricBuffer.poll();
                LOG.debug("Poll message from duration metric message buffer metricCode = {} duration = {}",
                        message.getMetricCode(), message.getDuration());
                if (!this.durationMetricMessageProducer.trySendMessage(message)) {
                    LOG.warn("Can not send duration metric message metricCode = {} duration = {}",
                            message.getMetricCode(), message.getDuration());
                    Thread.sleep(SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS);
                } else {
                    LOG.debug("Remove message from duration metric message buffer metricCode = {} duration = {}",
                            message.getMetricCode(), message.getDuration());
                    this.durationMetricBuffer.remove(message);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
