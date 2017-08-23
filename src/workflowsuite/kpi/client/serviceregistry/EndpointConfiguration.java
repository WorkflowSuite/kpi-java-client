package workflowsuite.kpi.client.serviceregistry;

import java.net.URI;

public final class EndpointConfiguration {

    private String _serviceContract;
    private URI _address;
    private String _transportSettingsCode;

    public EndpointConfiguration(String serviceContract, URI address, String transportSettingsCode) {

        _serviceContract = serviceContract;
        _address = address;
        _transportSettingsCode = transportSettingsCode;
    }

    public String getServiceContract() {
        return _serviceContract;
    }

    public URI getAddress() {
        return _address;
    }

    public String getTransportSettingsCode() {
        return _transportSettingsCode;
    }

    public void setServiceContract(String serviceContract) {
        _serviceContract = serviceContract;
    }

    public void setAddress(URI address) {
        _address = address;
    }

    public void setTransportSettingsCode(String transportSettingsCode) {
        _transportSettingsCode = transportSettingsCode;
    }
}