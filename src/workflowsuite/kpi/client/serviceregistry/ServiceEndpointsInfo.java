package workflowsuite.kpi.client.serviceregistry;

import java.util.ArrayList;

public final class ServiceEndpointsInfo {
    public final static ServiceEndpointsInfo Empty = new ServiceEndpointsInfo();

    public String deploymentUnitName = "";

    public String serviceKind = "";

    public ArrayList<EndpointConfiguration> endpoints = new ArrayList<>(0);

    public ArrayList<TransportSettings> transportSettigs = new ArrayList<>(0);
}