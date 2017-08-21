package workflowsuite.kpi.client.serviceregistry

data class ServiceEndpointsInfo(var deploymentUnitName: String, var serviceKind: String, var endpoints:List<EndpointConfiguration>, var transportSettigs: List<TransportSettings>) {
    companion object {
        val Empty = ServiceEndpointsInfo("", "", emptyList(), emptyList() )

        fun CreateInstance() : ServiceEndpointsInfo {
            return ServiceEndpointsInfo("", "", emptyList(), emptyList())
        }
    }
}