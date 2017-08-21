package workflowsuite.kpi.client.settings;

public interface IConfigurationProvider<T> {
    GetConfigurationResult<T> TryGetValidConfiguration();
}

