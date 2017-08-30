package workflowsuite.kpi.client.settings;

public interface ConfigurationProvider<T> {
    /**
     * Return configuration result.
     * @return Configuration result.
     */
    GetConfigurationResult<T> tryGetValidConfiguration();
}

