package workflowsuite.kpi.client;

import java.net.URI;
import javax.net.SocketFactory;

public class KpiClientFactory {
    private URI srUri;

    /**
     * Set URI for interacting service registry.
     * @param serviceRegistryUri The address where the service registry is deployed.
     * @return Current instance of {{@link KpiClientFactory}}.
     */
    public KpiClientFactory useServiceRegistry(URI serviceRegistryUri) {

        this.srUri = serviceRegistryUri;
        return this;
    }

    /**
     * Create kpi client object.
     * @return Kpi client object.
     */
    public KpiClient newKpiClient() {
        return new KpiClientImpl(srUri, SocketFactory.getDefault());
    }
}
