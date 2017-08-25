package workflowsuite.kpi.client.settings;

public final class SimpleConfigurationProvider<T> implements ConfigurationProvider<T> {
    private final T _configuration;

    public SimpleConfigurationProvider(T configuration) {
        _configuration = configuration;
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public final GetConfigurationResult<T> tryGetValidConfiguration() {
        return new GetConfigurationResult(true, _configuration);
    }
}