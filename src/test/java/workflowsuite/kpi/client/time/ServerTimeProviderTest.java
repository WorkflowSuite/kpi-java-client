package workflowsuite.kpi.client.time;

import org.junit.Test;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public final class ServerTimeProviderTest {
    @Test
    public void getNtpData() throws URISyntaxException, IOException, ConfigurationNotFoundException {
        ServiceRegistryClient srClient = new ServiceRegistryClient(new URI("http://msk-tfs-lab09:9130/"));
        //TimeServerConfigurationProvider configuration = new TimeServerConfigurationProvider(srClient, Duration.ofSeconds(5));
        StaticProvider configuration = new StaticProvider();
        ServerTimeProvider timeProvider = new ServerTimeProvider(configuration);
        NtpData ntpData = timeProvider.getNtpData();
    }

    public class StaticProvider implements ConfigurationProvider<TimeServerConfiguration> {

        @Override
        public GetConfigurationResult<TimeServerConfiguration> tryGetValidConfiguration() {
            TimeServerConfiguration c = new TimeServerConfiguration();
            c.setEndpoint(URI.create("tcp://msk-tfs-lab09:9701/"));
            return GetConfigurationResult.success(c);
        }
    }

}
