package workflowsuite.kpi.client.rabbitmq;

import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

import java.net.URI;
import java.time.Duration;

public class RabbitConfigurationProviderTest {
    //@Test
    public void tryGetValidConfiguration() throws Exception {
        ServiceRegistryClient srClient = new ServiceRegistryClient(new URI("http://msk-dev-foris:9130/"),
                org.slf4j.LoggerFactory.getILoggerFactory());
        RabbitConfigurationProvider provider = new RabbitConfigurationProvider(srClient, Duration.ofSeconds(5),
                RabbitConfigurationProvider.KPI_GENERAL_QUEUE_CONTRACT);
        GetConfigurationResult c = provider.tryGetValidConfiguration();
    }

}
