package workflowsuite.kpi.client.time;

import org.junit.Test;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ServerTimeProviderTest {
    //@Test
    public void getNtpData() throws URISyntaxException, IOException, ConfigurationNotFoundException {
        StaticProvider configuration = new StaticProvider("tcp://msk-tfs-lab09:9701/");
        ServerTimeProvider timeProvider = new ServerTimeProvider(configuration);
        NtpData ntpData = timeProvider.getNtpData();
    }

    public class StaticProvider implements ConfigurationProvider<TimeServerConfiguration> {

        private final String ntpServer;

        StaticProvider(String ntpServer) {

            this.ntpServer = ntpServer;
        }

        @Override
        public GetConfigurationResult<TimeServerConfiguration> tryGetValidConfiguration() {
            TimeServerConfiguration c = new TimeServerConfiguration();
            c.setEndpoint(URI.create(this.ntpServer));
            return GetConfigurationResult.success(c);
        }
    }

}
