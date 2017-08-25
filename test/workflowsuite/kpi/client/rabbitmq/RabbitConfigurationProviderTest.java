package workflowsuite.kpi.client.rabbitmq;

import org.junit.Test;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

import java.net.URI;
import java.time.Duration;

import static org.junit.Assert.*;

public class RabbitConfigurationProviderTest {
    //@Test
    public void tryGetValidConfiguration() throws Exception {
        ServiceRegistryClient srClient = new ServiceRegistryClient(new URI("http://msk-dev-foris:9130/"));
        RabbitConfigurationProvider provider = new RabbitConfigurationProvider(srClient, Duration.ofSeconds(5));
        GetConfigurationResult c = provider.tryGetValidConfiguration();
    }

}