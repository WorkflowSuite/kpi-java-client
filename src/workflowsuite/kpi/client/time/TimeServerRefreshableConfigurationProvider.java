package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ServiceRegistryConfigurationProvider;

import java.time.Duration;

public final class TimeServerRefreshableConfigurationProvider extends ServiceRegistryConfigurationProvider<TimeServerConfiguration> {

    public TimeServerRefreshableConfigurationProvider(ServiceRegistryClient serviceRegistryClient, Duration refreshPeriod) {
        super(serviceRegistryClient, refreshPeriod);
    }

    @Override
    protected void SyncConfiguration() {
        ServiceEndpointsInfo info = serviceRegistryClient.getServiceEndpointsInfo("http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer");
        if (info.endpoints.size() > 0) {

            String transportSettings = info.transportSettigs.size() > 0 ? info.transportSettigs.get(0).getBody() : "";
            this.configuration = new TimeServerConfiguration(info.endpoints.get(0).getAddress(), transportSettings);
        }
    }
}
