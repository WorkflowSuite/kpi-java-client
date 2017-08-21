package workflowsuite.kpi.client.serviceregistry;

import java.net.URL;

public final class EndpointConfiguration {

    private final String _serviceContract;
    private final URL _address;
    private final String _transportSettingsCode;

    public EndpointConfiguration(String serviceContract, URL address, String transportSettingsCode) {

        _serviceContract = serviceContract;
        _address = address;
        _transportSettingsCode = transportSettingsCode;
    }

    public String getServiceContract() {
        return _serviceContract;
    }

    public URL getAddress() {
        return _address;
    }

    public String getTransportSettingsCode() {
        return _transportSettingsCode;
    }
}