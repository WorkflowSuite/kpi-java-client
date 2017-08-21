package workflowsuite.kpi.client.serviceregistry

import java.net.URL

data class EndpointConfiguration(val serviceContract: String, val address: URL, val transportSettingsCode: String) {
}