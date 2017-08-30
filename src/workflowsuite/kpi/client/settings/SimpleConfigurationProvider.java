package workflowsuite.kpi.client.settings;

public final class SimpleConfigurationProvider<T> implements ConfigurationProvider<T> {
    private final T configuration;

    public SimpleConfigurationProvider(T configuration) {
        this.configuration = configuration;
    }

    @Override
    public GetConfigurationResult<T> tryGetValidConfiguration() {
        return new GetConfigurationResult(true, configuration);
    }
}
