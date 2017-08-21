package workflowsuite.kpi.client.settings

class SimpleConfigurationProvider<T> (private val configuration : T) : IConfigurationProvider<T> {
    override fun TryGetValidConfiguration(): GetConfigurationResult<T> {
        return GetConfigurationResult(true, configuration)
    }
}