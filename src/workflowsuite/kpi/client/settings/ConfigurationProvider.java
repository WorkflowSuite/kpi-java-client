package workflowsuite.kpi.client.settings;

public interface ConfigurationProvider<T> {
    GetConfigurationResult<T> tryGetValidConfiguration();
}

