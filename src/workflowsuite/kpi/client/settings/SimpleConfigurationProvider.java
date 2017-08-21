package workflowsuite.kpi.client.settings;

public final class SimpleConfigurationProvider<T> implements IConfigurationProvider<T> {
    private final T _configuration;

    public SimpleConfigurationProvider(T configuration) {
        _configuration = configuration;
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public final GetConfigurationResult<T> TryGetValidConfiguration() {
        return new GetConfigurationResult(true, _configuration);
    }
}