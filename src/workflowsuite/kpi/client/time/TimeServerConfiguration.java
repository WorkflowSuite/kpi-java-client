package workflowsuite.kpi.client.time;

import java.net.URI;

public final class TimeServerConfiguration {

    private final URI _endpoint;

    public TimeServerConfiguration(URI endpoint) {
        _endpoint = endpoint;
    }

    public final URI getEndpoint() {
        return _endpoint;
    }
}