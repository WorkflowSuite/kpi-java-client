package workflowsuite.kpi.client;

import java.net.URI;
import java.time.Instant;

import workflowsuite.kpi.client.rabbitmq.RabbitConfigurationProvider;
import workflowsuite.kpi.client.rabbitmq.RabbitProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.time.ServerTimeProvider;
import workflowsuite.kpi.client.time.TimeOffsetCalculator;
import workflowsuite.kpi.client.time.TimeServerConfiguration;
import workflowsuite.kpi.client.time.TimeServerConfigurationProvider;
import workflowsuite.kpi.client.time.TimeSynchronizer;

public final class KpiClient {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;
    private static final long SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS = 1000;

    private final KpiMessageBuffer buffer;

    private final Thread consumeMessagesThread;
    private final MessageProducer messageProducer;
    private final TimeSynchronizer timeSynchronizer;

    /**
     * Create new instance of kpi client.
     * @param serviceRegistryUri The address where the service registry is deployed.
     */
    KpiClient(URI serviceRegistryUri) {
        this.buffer = new KpiMessageBuffer(DEFAULT_BUFFER_SIZE);
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient(serviceRegistryUri);
        this.messageProducer = new RabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME));
        ConfigurationProvider<TimeServerConfiguration> timeServerConfiguration =
                new TimeServerConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME);
        this.timeSynchronizer = new TimeSynchronizer(
                timeServerConfiguration,
                new ServerTimeProvider(timeServerConfiguration),
                new TimeOffsetCalculator());

        this.consumeMessagesThread = new Thread(this::consumeMessages);
        this.consumeMessagesThread.setName("KPI_CONSUME_MESSAGE");
        this.consumeMessagesThread.start();
    }

    /**
     * Registers a checkpoint crossing fact for an explicitly specified session.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    public boolean onCheckpoint(String checkpointCode, String sessionId) {
        Instant now = Instant.now();
        KpiMessage message = new KpiMessage();
        message.setCheckpointCode(checkpointCode);
        message.setSessionId(sessionId);
        message.setClientEventTime(now);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        return this.buffer.offer(message);
    }

    /**
     * Notifies that specified checkpoint is unreachable in the current execution path
     * and all started rules which should be ended in the specified checkpoint should be cancelled.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    public boolean unreachableCheckpoint(String checkpointCode, String sessionId) {
        Instant now = Instant.now();
        KpiMessage message = new KpiMessage();
        message.setCheckpointCode(checkpointCode);
        message.setSessionId(sessionId);
        message.setClientEventTime(now);
        message.setUnreachable(true);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        return this.buffer.offer(message);
    }


    private void consumeMessages() {
        while (true) {
            try {
                KpiMessage message = this.buffer.poll();
                if (!this.messageProducer.trySendMessage(message)) {
                    Thread.sleep(SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS);
                } else {
                    this.buffer.remove(message);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
