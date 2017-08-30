package workflowsuite.kpi.client.serviceregistry;

import java.net.URI;

public final class EndpointConfiguration {

    private String serviceContract = "";
    private URI address;
    private String transportSettingsCode = "";

    EndpointConfiguration(String serviceContract, URI address, String transportSettingsCode) {

        this.serviceContract = serviceContract;
        this.address = address;
        this.transportSettingsCode = transportSettingsCode;
    }

    public String getServiceContract() {
        return this.serviceContract;
    }

    public URI getAddress() {
        return this.address;
    }

    public String getTransportSettingsCode() {
        return this.transportSettingsCode;
    }

    public void setServiceContract(String serviceContract) {
        this.serviceContract = serviceContract;
    }

    public void setAddress(URI address) {
        this.address = address;
    }

    public void setTransportSettingsCode(String transportSettingsCode) {
        this.transportSettingsCode = transportSettingsCode;
    }
}
