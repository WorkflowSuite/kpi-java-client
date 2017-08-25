package workflowsuite.kpi.client.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import workflowsuite.kpi.client.KpiMessage;
import workflowsuite.kpi.client.MessageProducer;
import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitProducer implements MessageProducer {

    private final ConfigurationProvider<RabbitQueueConfiguration> configurationProvider;
    private final KpiMessageSerializer serializer;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    private String queueName = "";

    public RabbitProducer(@NotNull ConfigurationProvider<RabbitQueueConfiguration> configurationProvider) {

        this.configurationProvider = configurationProvider;
        this.serializer = new KpiMessageSerializer();
    }

    @Override
    public boolean TrySendMessage(@NotNull KpiMessage message) {
        byte[] messageBytes = this.serializer.serialize(message);
        return TrySendMessage(messageBytes);
    }

    private boolean TrySendMessage(@NotNull byte[] message) {
        try {
            if (TryGetOnline())
            {
                this.channel.basicPublish("", queueName, null, message);
            }

            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private boolean TryGetOnline() {
        try {
            if (this.connection != null && this.channel != null && this.connection.isOpen() && this.channel.isOpen()) {

                return true;
            }
            GetConfigurationResult<RabbitQueueConfiguration> configurationResult
                    = configurationProvider.tryGetValidConfiguration();
            if (!configurationResult.getSuccess())
                return false;
            RabbitQueueConfiguration cfg = configurationResult.getConfiguration();
            this.queueName = cfg.getQueue();
            this.connectionFactory = new ConnectionFactory();
            this.connectionFactory.setHost(cfg.getEndpoint().getHost());
            this.connectionFactory.setUsername(cfg.getUserName());
            this.connectionFactory.setPassword(cfg.getPassword());
            this.connectionFactory.setAutomaticRecoveryEnabled(false);

            this.connection = connectionFactory.newConnection("Workflow Suite KPI Java Client");
            this.channel = this.connection.createChannel();

            Map<String, Object> arguments = new HashMap<>();
            //arguments.putIfAbsent("x-dead-letter-exchange", cfg.getDeadLetterExchange());
            //arguments.putIfAbsent("x-dead-letter-routing-key", cfg.getDeadLetterRoutingKey());

            this.channel.queueDeclare(this.queueName, cfg.isDurable(), cfg.isExclusive(), cfg.isAutoDelete(), arguments);

            return this.connection.isOpen() && this.channel.isOpen();
        }
        catch (TimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
