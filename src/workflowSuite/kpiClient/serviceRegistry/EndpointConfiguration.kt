package workflowSuite.kpiClient.serviceRegistry

import java.net.URL

data class EndpointConfiguration(val serviceContract: String, val address: URL, val transportSettingsCode: String) {
}