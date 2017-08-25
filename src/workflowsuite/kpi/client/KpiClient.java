package workflowsuite.kpi.client;

import workflowsuite.kpi.client.collections.KpiMessageBuffer;
import workflowsuite.kpi.client.rabbitmq.RabbitConfigurationProvider;
import workflowsuite.kpi.client.rabbitmq.RabbitProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.time.ServerTimeProvider;
import workflowsuite.kpi.client.time.TimeServerConfiguration;
import workflowsuite.kpi.client.time.TimeServerRefreshableConfigurationProvider;
import workflowsuite.kpi.client.time.TimeSynchronizer;
import workflowsuite.kpi.client.time.TimeOffsetCalculator;

import java.net.URI;
import java.time.Instant;

public final class KpiClient {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;
    private static final long SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS = 1000;

    private final KpiMessageBuffer buffer;

    private final Thread consumeMessagesThread;
    private final MessageProducer messageProducer;
    private final TimeSynchronizer timeSynchronizer;

    public KpiClient(URI serviceRegistryUri) {
        this.buffer = new KpiMessageBuffer(DEFAULT_BUFFER_SIZE);
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient(serviceRegistryUri);
        this.messageProducer = new RabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME));
        ConfigurationProvider<TimeServerConfiguration> timeServerConfiguration =
                new TimeServerRefreshableConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME);
        this.timeSynchronizer = new TimeSynchronizer(
                timeServerConfiguration,
                new ServerTimeProvider(timeServerConfiguration),
                new TimeOffsetCalculator());

        this.consumeMessagesThread = new Thread(this::consumeMessages);
        this.consumeMessagesThread.setName("KPI_CONSUME_MESSAGE");
        this.consumeMessagesThread.start();
    }

    public final boolean onCheckpoint(String checkpointCode, String sessionId) {
        Instant now = Instant.now();
        KpiMessage message = new KpiMessage();
        message.setCheckpointCode(checkpointCode);
        message.setSessionId(sessionId);
        message.setClientEventTime(now);

        Instant adjustedTime = now.plus(this.timeSynchronizer.getOffset());
        message.setSynchronizedEventTime(adjustedTime);

        return this.buffer.offer(message);
    }

    private void consumeMessages() {
        while (true) {
            try {
                KpiMessage message = this.buffer.take();
                if (message != null) {
                    if (!this.messageProducer.TrySendMessage(message)) {
                        Thread.sleep(this.SEND_FAILURE_RELAXATION_TIMEOUT_MILLIS);
                    }
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}