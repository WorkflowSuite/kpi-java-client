package workflowsuite.kpi.client.rabbitmq;

import java.time.Duration;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.ServiceRegistryClient;
import workflowsuite.kpi.client.settings.ServiceRegistryConfigurationProvider;

public final class RabbitConfigurationProvider extends ServiceRegistryConfigurationProvider<RabbitQueueConfiguration> {
    /**
     * Contract of general kpi queue
     */
    public static final String KPI_GENERAL_QUEUE_CONTRACT
            = "http://schemas.sitels.ru/marti/workflow/rabbitmq/queue/KpiQueue";
    /**
     * Contract of duration metric queue
     */
    public static final String KPI_DURATION_METRIC_QUEUE_CONTRACT
            = "http://schemas.sitels.ru/marti/workflow/rabbitmq/queue/KpiDurationMetricQueue";
    private final String queueContract;

    /**
     * Create instance of {@code RabbitConfigurationProvider} class.
     * @param serviceRegistryClient The instance of service registry client, which use for read configuration.
     * @param refreshPeriod The period after which the settings are read again
     * @param queueContract Contract of queue.
     */
    public RabbitConfigurationProvider(ServiceRegistryClient serviceRegistryClient,
                                Duration refreshPeriod, String queueContract) {
        super(serviceRegistryClient, refreshPeriod);
        this.queueContract = queueContract;
    }

    @Override
    protected void syncConfiguration() {
        ServiceEndpointsInfo info = serviceRegistryClient.getServiceEndpointsInfo(this.queueContract);
        if (info.endpoints.size() > 0) {
            this.configuration = RabbitQueueConfigurationParser.parse(this.queueContract, info);
        }
    }
}
