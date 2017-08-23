package workflowsuite.kpi.client.time;

import org.junit.Test;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public final class ServerTimeProviderTest {
    //@Test
    public void getNtpData() throws URISyntaxException, IOException, ConfigurationNotFoundException {
        ServiceRegistryClient srClient = new ServiceRegistryClient(new URI("http://msk-dev-foris:9130/"));
        TimeServerRefreshableConfigurationProvider configuration = new TimeServerRefreshableConfigurationProvider(srClient, Duration.ofSeconds(5));
        ServerTimeProvider timeProvider = new ServerTimeProvider(configuration);
        NtpData ntpData = timeProvider.GetNtpData();
    }

}