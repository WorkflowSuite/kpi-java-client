package workflowsuite.kpi.client.serviceregistry;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public final class EndpointConfiguration {

    private String serviceContract = "";
    private URI address;
    private String transportSettingsCode = "";

    public EndpointConfiguration(String serviceContract, URI address, String transportSettingsCode) {

        this.serviceContract = serviceContract;
        this.address = address;
        this.transportSettingsCode = transportSettingsCode;
    }

    @Contract(pure = true)
    @NotNull
    public String getServiceContract() { return this.serviceContract; }

    @Contract(pure = true)
    @NotNull
    public URI getAddress() {
        return this.address;
    }

    @Contract(pure = true)
    @NotNull
    public String getTransportSettingsCode() {
        return this.transportSettingsCode;
    }

    public void setServiceContract(@NotNull String serviceContract) {
        this.serviceContract = serviceContract;
    }

    public void setAddress(@NotNull URI address) {
        this.address = address;
    }

    public void setTransportSettingsCode(@NotNull String transportSettingsCode) {
        this.transportSettingsCode = transportSettingsCode;
    }
}