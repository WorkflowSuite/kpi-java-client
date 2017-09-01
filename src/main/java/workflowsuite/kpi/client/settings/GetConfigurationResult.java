package workflowsuite.kpi.client.settings;

public final class GetConfigurationResult<T> {

    private final boolean success;
    private final T configuration;

    private GetConfigurationResult(boolean success, T configuration) {
        this.success = success;
        this.configuration = configuration;
    }

    /**
     * Create success result.
     * @param configuration Configuration.
     * @param <U> Type of configuration.
     * @return Success result.
     */
    public static <U> GetConfigurationResult<U> success(U configuration) {
        return new GetConfigurationResult<U>(true, configuration);
    }

    /**
     * Create fail result.
     * @param <U> Type of configuration.
     * @return Failt result.
     */
    public static <U> GetConfigurationResult<U> fail() {
        return new GetConfigurationResult<U>(false, null);
    }

    public boolean getSuccess() {
        return success;
    }

    public T getConfiguration() {
        return configuration;
    }
}
