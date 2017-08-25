package workflowsuite.kpi.client.settings;

import org.jetbrains.annotations.Contract;

public final class GetConfigurationResult<T> {

    private boolean _success;
    private T _configuration;

    public GetConfigurationResult(boolean success, T configuration) {
        _success = success;
        _configuration = configuration;
    }

    public static GetConfigurationResult success(Object configuration) {
        return new GetConfigurationResult(true, configuration);
    }

    public static GetConfigurationResult fail() {
        return new GetConfigurationResult(false, null);
    }

    @Contract(pure = true)
    public boolean getSuccess() {
        return _success;
    }

    @Contract(pure = true)
    public T getConfiguration() {
        return _configuration;
    }
}
