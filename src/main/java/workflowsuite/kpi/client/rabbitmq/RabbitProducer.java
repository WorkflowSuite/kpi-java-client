package workflowsuite.kpi.client.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import workflowsuite.kpi.client.KpiMessage;
import workflowsuite.kpi.client.MessageProducer;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

public final class RabbitProducer implements MessageProducer {

    private final ConfigurationProvider<RabbitQueueConfiguration> configurationProvider;
    private final KpiMessageSerializer serializer;
    private Connection connection;
    private Channel channel;
    private String queueName = "";

    /**
     * Create instance of {{@link ConfigurationProvider}} class
     * for processing {{@link KpiMessage}}.
     * @param configurationProvider The provider for get settings.
     */
    public RabbitProducer(ConfigurationProvider<RabbitQueueConfiguration> configurationProvider) {

        this.configurationProvider = configurationProvider;
        this.serializer = new KpiMessageSerializer();
    }

    @Override
    public boolean trySendMessage(KpiMessage message) {
        byte[] messageBytes = this.serializer.serialize(message);
        return trySendMessage(messageBytes);
    }

    private boolean trySendMessage(byte[] message) {
        try {
            if (tryGetOnline()) {
                /*int channelNumber = this.channel.getChannelNumber();
                AMQConnection amqConnection = (AMQConnection) this.channel.getConnection();

                com.rabbitmq.client.impl.Method m = (com.rabbitmq.client.impl.Method)
                        (new com.rabbitmq.client.AMQP.Basic.Publish.Builder()
                        .exchange("")
                        .routingKey(queueName)
                        .mandatory(false)
                        .immediate(false)
                        .build());

                amqConnection.writeFrame(m.toFrame(channelNumber));
                amqConnection.writeFrame(MessageProperties.MINIMAL_BASIC.toFrame(channelNumber, message.length));
                int frameMax = amqConnection.getFrameMax();
                byte[] body = message;
                int bodyPayloadMax = (frameMax == 0) ? message.length : frameMax
                        - 8;
                for (int offset = 0; offset < body.length; offset += bodyPayloadMax) {
                    int remaining = body.length - offset;

                    int fragmentLength = (remaining < bodyPayloadMax) ? remaining
                            : bodyPayloadMax;
                    com.rabbitmq.client.impl.Frame frame =
                            com.rabbitmq.client.impl.Frame.fromBodyFragment(channelNumber, body,
                                    offset, fragmentLength);
                    amqConnection.writeFrame(frame);
                }
                amqConnection.flush();*/

                this.channel.basicPublish("", queueName, null, message);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean tryGetOnline() {
        try {
            if (this.connection != null && this.channel != null && this.connection.isOpen() && this.channel.isOpen()) {

                return true;
            }
            GetConfigurationResult<RabbitQueueConfiguration> configurationResult
                    = configurationProvider.tryGetValidConfiguration();
            if (!configurationResult.getSuccess()) {
                return false;
            }
            RabbitQueueConfiguration cfg = configurationResult.getConfiguration();
            this.queueName = cfg.getQueue();
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(cfg.getEndpoint().getHost());
            connectionFactory.setUsername(cfg.getUserName());
            connectionFactory.setPassword(cfg.getPassword());
            connectionFactory.setAutomaticRecoveryEnabled(false);

            this.connection = connectionFactory.newConnection("Workflow Suite KPI Java Client");
            this.channel = this.connection.createChannel();

            Map<String, Object> arguments = new HashMap<>();
            //arguments.putIfAbsent("x-dead-letter-exchange", cfg.getDeadLetterExchange());
            //arguments.putIfAbsent("x-dead-letter-routing-key", cfg.getDeadLetterRoutingKey());

            this.channel.queueDeclare(this.queueName, cfg.isDurable(), cfg.isExclusive(),
                    cfg.isAutoDelete(), arguments);

            return this.connection.isOpen() && this.channel.isOpen();
        } catch (TimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
