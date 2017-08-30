package workflowsuite.kpi.client.settings;

import java.time.Duration;
import java.time.Instant;

import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;

public abstract class ServiceRegistryConfigurationProvider<T> implements ConfigurationProvider<T> {

    private final Duration refreshPeriod;
    private Instant lastSync;
    protected final ServiceRegistryClient serviceRegistryClient;
    protected T configuration;

    public ServiceRegistryConfigurationProvider(ServiceRegistryClient serviceRegistryClient,
                                                Duration refreshPeriod) {
        this.serviceRegistryClient = serviceRegistryClient;
        this.refreshPeriod = refreshPeriod;
        this.configuration = null;
        this.lastSync = Instant.MIN;
    }

    public final GetConfigurationResult<T> tryGetValidConfiguration() {
        if (Duration.between(lastSync, Instant.now()).compareTo(refreshPeriod) > 0) {
            SyncConfiguration();
            lastSync = Instant.now();
        }

        if (configuration != null) {
            return GetConfigurationResult.success(configuration);
        }

        return GetConfigurationResult.fail();
    }

    protected abstract void SyncConfiguration();
}
