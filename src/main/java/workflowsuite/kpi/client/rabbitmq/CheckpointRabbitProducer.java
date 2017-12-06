package workflowsuite.kpi.client.rabbitmq;

import javax.net.SocketFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import org.slf4j.ILoggerFactory;

import workflowsuite.kpi.client.CheckpointMessage;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

public final class CheckpointRabbitProducer extends RabbitProducerBase<CheckpointMessage> {

    private final CheckpointMessageSerializer serializer;
    private Connection connection;
    private Channel channel;
    private String queueName = "";

    /**
     * Create instance of {{@link ConfigurationProvider}} class
     * for processing {{@link CheckpointMessage}}.
     * @param configurationProvider The provider for get settings.
     */
    public CheckpointRabbitProducer(ConfigurationProvider<RabbitQueueConfiguration> configurationProvider,
                                    ILoggerFactory loggerFactory, SocketFactory socketFactory) {
        super(configurationProvider, loggerFactory, socketFactory);
        this.serializer = new CheckpointMessageSerializer();
    }

    @Override
    public boolean trySendMessage(CheckpointMessage message) {
        this.logger.debug("Entering trySendMessage(checkpointCode={}, sessionId={})",
                message.getCheckpointCode(), message.getSessionId());
        byte[] messageBytes = this.serializer.serialize(message);
        boolean result = trySendMessage(messageBytes);
        this.logger.debug("Leaving trySendMessage(): {}", result);
        return result;
    }
}
