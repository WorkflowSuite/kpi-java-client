package workflowsuite.kpi.client.time;

import java.time.Duration;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ServiceRegistryConfigurationProvider;

public final class TimeServerConfigurationProvider extends
        ServiceRegistryConfigurationProvider<TimeServerConfiguration> {

    private static final String TIME_SERVER_CONTRACT
            = "http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer";

    /**
     * Create instance of {{@link TimeServerConfiguration}} class.
     * @param serviceRegistryClient The instance of service registry client, which use for read configuration.
     * @param refreshPeriod The period after which the settings are read again
     */
    public TimeServerConfigurationProvider(ServiceRegistryClient serviceRegistryClient,
                                           Duration refreshPeriod) {
        super(serviceRegistryClient, refreshPeriod);
    }

    @Override
    protected void syncConfiguration() {
        ServiceEndpointsInfo info = serviceRegistryClient.getServiceEndpointsInfo(TIME_SERVER_CONTRACT);
        if (info.endpoints.size() > 0) {

            String transportSettings = info.transportSettigs.size() > 0
                    ? info.transportSettigs.get(0).getBody()
                    : "";
            this.configuration = TimeServerConfiguration.parse(info.endpoints.get(0).getAddress(), transportSettings);
        }
    }
}
