package workflowsuite.kpi.client.rabbitmq;

import java.time.Duration;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ServiceRegistryConfigurationProvider;

public final class RabbitConfigurationProvider extends ServiceRegistryConfigurationProvider<RabbitQueueConfiguration> {
    private static final String KPI_CONTRACT = "http://schemas.sitels.ru/marti/workflow/rabbitmq/queue/KpiQueue";

    public RabbitConfigurationProvider(ServiceRegistryClient serviceRegistryClient,
                                Duration refreshPeriod) {
        super(serviceRegistryClient, refreshPeriod);
    }

    @Override
    protected void SyncConfiguration() {
        ServiceEndpointsInfo info = serviceRegistryClient.getServiceEndpointsInfo(KPI_CONTRACT);
        this.configuration = RabbitQueueConfigurationParser.parse(KPI_CONTRACT, info);
    }
}
