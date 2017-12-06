package workflowsuite.kpi.client.rabbitmq;

import javax.net.SocketFactory;

import org.slf4j.ILoggerFactory;

import workflowsuite.kpi.client.CheckpointMessage;
import workflowsuite.kpi.client.DurationMetricMessage;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

public final class DurationMetricRabbitProducer extends RabbitProducerBase<DurationMetricMessage> {

    private final DurationMetricMessageSerializer serializer;

    /**
     * Create instance of {{@link ConfigurationProvider}} class
     * for processing {{@link CheckpointMessage}}.
     * @param configurationProvider The provider for get settings.
     */
    public DurationMetricRabbitProducer(ConfigurationProvider<RabbitQueueConfiguration> configurationProvider,
                                    ILoggerFactory loggerFactory, SocketFactory socketFactory) {
        super(configurationProvider, loggerFactory, socketFactory);
        this.serializer = new DurationMetricMessageSerializer();
    }

    @Override
    public boolean trySendMessage(DurationMetricMessage message) {
        this.logger.debug("Entering trySendMessage(metricCode={}, duration={})",
                message.getMetricCode(), message.getDuration());
        byte[] messageBytes = this.serializer.serialize(message);
        boolean result = trySendMessage(messageBytes);
        this.logger.debug("Leaving trySendMessage(): {}", result);
        return result;
    }
}
