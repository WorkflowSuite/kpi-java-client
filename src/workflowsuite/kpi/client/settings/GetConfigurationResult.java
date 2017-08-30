package workflowsuite.kpi.client.settings;

public final class GetConfigurationResult<T> {

    private boolean success;
    private T configuration;

    public GetConfigurationResult(boolean success, T configuration) {
        this.success = success;
        this.configuration = configuration;
    }

    public static GetConfigurationResult success(Object configuration) {
        return new GetConfigurationResult(true, configuration);
    }

    public static GetConfigurationResult fail() {
        return new GetConfigurationResult(false, null);
    }

    public boolean getSuccess() {
        return success;
    }

    public T getConfiguration() {
        return configuration;
    }
}
