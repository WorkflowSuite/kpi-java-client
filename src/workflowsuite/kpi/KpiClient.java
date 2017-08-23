package workflowsuite.kpi;

import workflowsuite.kpi.client.KpiMessage;
import workflowsuite.kpi.client.collections.ConcurrentSingleConsumerQueue;
import workflowsuite.kpi.client.rabbitmq.NullProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.IConfigurationProvider;
import workflowsuite.kpi.client.time.*;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executor;

public final class KpiClient {
    private final int DEFAULT_BUFFER_SIZE = 1024 * 16;

    private final ConcurrentSingleConsumerQueue<KpiMessage> buffer;
    private final Duration sendFailureRelaxationTimeout;
    private final Thread consumeMessagesThread;
    private final NullProducer rabbitProducer;
    private final ServiceRegistryClient serviceRegistryClient;
    private final TimeSynhronizer timeSynchronizer;

    public KpiClient(URI serviceRegistryUri) {
        this.buffer = new ConcurrentSingleConsumerQueue<>(DEFAULT_BUFFER_SIZE);
        this.sendFailureRelaxationTimeout = Duration.ofSeconds(1);
        this.serviceRegistryClient = new ServiceRegistryClient(serviceRegistryUri);
        this.rabbitProducer = new NullProducer();
        IConfigurationProvider<TimeServerConfiguration> timeServerConfiguration =
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
        KpiPoint point = new KpiPoint();
        point.setCheckpointCode(checkpointCode);
        return enqueueMessage(point, sessionId, now);
    }

    private boolean enqueueMessage(KpiPoint point, String sessionId, Instant now) {
        KpiMessage message = new KpiMessage();
        message.setPoint(point);
        message.setLocalTime(LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        message.setUtcTime(now);
        message.setSessionId(sessionId);

        return this.buffer.offer(message);
    }

    private void consumeMessages() {

    }
}