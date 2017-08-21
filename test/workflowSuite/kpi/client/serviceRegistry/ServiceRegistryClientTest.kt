package workflowsuite.kpi.client.serviceregistry

import org.junit.Test

class ServiceRegistryClientTest {
    @Test
    fun getServiceEndpointsInfo() {
        val clinet = ServiceRegistryClient("http://msk-dev-foris:9130/")
        val r = clinet.getServiceEndpointsInfo("http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer")
    }

}