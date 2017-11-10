package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.ConfigurationProvider;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ServerTimeProviderTest {
    //@Test
    public void getNtpData() throws URISyntaxException, IOException {
        TimeServerConfiguration configuration = new TimeServerConfiguration();
        configuration.setEndpoint(URI.create("tcp://msk-tfs-lab09:9701/"));
        ServerTimeProvider timeProvider = new ServerTimeProvider(ConfigurationProvider.wrap(configuration), SocketFactory.getDefault());
        NtpData ntpData = timeProvider.getNtpData();
    }
}
