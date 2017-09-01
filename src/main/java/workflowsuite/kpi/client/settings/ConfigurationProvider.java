package workflowsuite.kpi.client.settings;

public interface ConfigurationProvider<T> {
    /**
     * Return configuration result.
     * @return Configuration result.
     */
    GetConfigurationResult<T> tryGetValidConfiguration();

    /**
     * Create configuration provider which always return the same configuration.
     * @param configuration Configuration, which will be returned.
     * @param <U> Configuration type.
     * @return Configuration passed to configuration argument.
     */
    static <U> ConfigurationProvider<U> wrap(U configuration) {
        return new FixProvider<U>(configuration);
    }

    /**
     * Provider, which always return same configuration.
     * @param <T> Configuration type
     */
    final class FixProvider<T> implements ConfigurationProvider<T> {

        private final T configuration;

        private FixProvider(T configuration) {

            this.configuration = configuration;
        }

        @Override
        public GetConfigurationResult<T> tryGetValidConfiguration() {
            return GetConfigurationResult.<T>success(this.configuration);
        }
    }
}

