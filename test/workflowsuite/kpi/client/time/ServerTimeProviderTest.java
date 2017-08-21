package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.SimpleConfigurationProvider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ServerTimeProviderTest {
    //@Test
    public void getNtpData() throws URISyntaxException, IOException, ConfigurationNotFoundException {
        SimpleConfigurationProvider configuration = new SimpleConfigurationProvider(new TimeServerConfiguration(new URI("tcp://msk-dev-foris:9701/")));
        ServerTimeProvider timeProvider = new ServerTimeProvider(configuration);
        NtpData ntpData = timeProvider.GetNtpData();
    }

}