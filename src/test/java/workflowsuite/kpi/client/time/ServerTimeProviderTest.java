package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ServerTimeProviderTest {
    //@Test
    public void getNtpData() throws URISyntaxException, IOException, ConfigurationNotFoundException {
        TimeServerConfiguration configuration = new TimeServerConfiguration();
        configuration.setEndpoint(URI.create("tcp://msk-tfs-lab09:9701/"));
        ServerTimeProvider timeProvider = new ServerTimeProvider(ConfigurationProvider.wrap(configuration));
        NtpData ntpData = timeProvider.getNtpData();
    }
}
