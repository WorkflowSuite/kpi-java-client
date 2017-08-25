package workflowsuite.kpi.client;

import workflowsuite.kpi.client.collections.KpiMessageBuffer;
import workflowsuite.kpi.client.rabbitmq.RabbitConfigurationProvider;
import workflowsuite.kpi.client.rabbitmq.RabbitProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.time.ServerTimeProvider;
import workflowsuite.kpi.client.time.TimeServerConfiguration;
import workflowsuite.kpi.client.time.TimeServerRefreshableConfigurationProvider;
import workflowsuite.kpi.client.time.TimeSynhronizer;
import workflowsuite.kpi.client.time.TimeOffsetCalculator;

import java.net.URI;
import java.time.Instant;

public final class KpiClient {
    private final int DEFAULT_BUFFER_SIZE = 1024 * 16;

    private final KpiMessageBuffer buffer;
    private final long sendFailureRelaxationTimeoutMillis;
    private final Thread consumeMessagesThread;
    private final MessageProducer messageProducer;
    private final TimeSynhronizer timeSynchronizer;

    public KpiClient(URI serviceRegistryUri) {
        this.buffer = new KpiMessageBuffer(DEFAULT_BUFFER_SIZE);
        this.sendFailureRelaxationTimeoutMillis = 1000;
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient(serviceRegistryUri);
        this.messageProducer = new RabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME));
        ConfigurationProvider<TimeServerConfiguration> timeServerConfiguration =
                new TimeServerRefreshableConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME);
        this.timeSynchronizer = new TimeSynhronizer(
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
        message.setClientEventTime(now);
        message.setSynchronizedEventTime(now);
        message.setSessionId(sessionId);

        return this.buffer.offer(message);
    }

    private void consumeMessages() {
        while (true) {
            try {
                KpiMessage message = this.buffer.take();
                if (message != null) {
                    Instant utcTime = message.getSynchronizedEventTime();
                    Instant adjustedTime = utcTime.plus(this.timeSynchronizer.getOffset());
                    message.setSynchronizedEventTime(adjustedTime);
                    if (!this.messageProducer.TrySendMessage(message)) {
                        Thread.sleep(this.sendFailureRelaxationTimeoutMillis);
                    }
                }
            } catch (InterruptedException e) {
                return;
            }

        }
    }
}