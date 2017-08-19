package workflowSuite.kpiClient.time

import org.junit.Test
import workflowSuite.kpiClient.settings.SimpleConfigurationProvider
import java.net.URI

class ServerTimeProviderTest {
    @Test
    fun getNtpData() {
        val configuration = SimpleConfigurationProvider(TimeServerConfiguration(URI("tcp://msk-dev-foris:9701/")))
        val timeProvider = ServerTimeProvider(configuration)
        val ntpData = timeProvider.GetNtpData()
    }

}