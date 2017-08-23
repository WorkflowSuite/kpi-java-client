package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.IConfigurationProvider;

import java.time.Duration;
import java.time.Instant;

public final class TimeServerRefreshableConfigurationProvider implements IConfigurationProvider<TimeServerConfiguration> {
    private final ServiceRegistryClient serviceRegistryClient;
    private final Duration refreshPeriod;
    private TimeServerConfiguration configuration;
    private Instant lastSync;

    public TimeServerRefreshableConfigurationProvider(ServiceRegistryClient serviceRegistryClient, Duration refreshPeriod) {

        this.serviceRegistryClient = serviceRegistryClient;
        this.refreshPeriod = refreshPeriod;
        this.configuration = null;
        this.lastSync = Instant.MIN;
    }

    @Override
    public GetConfigurationResult<TimeServerConfiguration> TryGetValidConfiguration() {
        if (Duration.between(lastSync, Instant.now()).compareTo(refreshPeriod) > 0) {
            SyncConfiguration();
        }

        if (configuration != null) {
            return new GetConfigurationResult<>(true, configuration);
        }

        return null;
    }

    private void SyncConfiguration() {
        ServiceEndpointsInfo infos = serviceRegistryClient.getServiceEndpointsInfo("http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer");
        if (infos != null && infos.endpoints.size() > 0) {

            String transportSettings = infos.transportSettigs.size() > 0 ? infos.transportSettigs.get(0).getBody() : "";
            this.configuration = new TimeServerConfiguration(infos.endpoints.get(0).getAddress(), transportSettings);
        }
        lastSync = Instant.now();
    }
}
