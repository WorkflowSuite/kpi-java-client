package workflowsuite.kpi.client.rabbitmq;

import org.junit.Assert;
import org.junit.Test;
import workflowsuite.kpi.client.KpiMessage;
import workflowsuite.kpi.client.MessageProducer;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;

import java.net.URI;

public class RabbitProducerTest {
    @Test
    public void trySendMessage() throws Exception {
        ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient(new URI("http://msk-dev-foris:9130/"));
        MessageProducer rabbitProducer = new RabbitProducer(
                new RabbitConfigurationProvider(serviceRegistryClient, ServiceRegistryClient.DEFAULT_REFRESH_TIME));
        boolean send = rabbitProducer.trySendMessage(new KpiMessage());
        Assert.assertTrue(send);
    }
}
