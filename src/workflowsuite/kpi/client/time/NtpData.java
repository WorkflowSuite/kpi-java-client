package workflowsuite.kpi.client.time;

import java.time.Instant;

public final class NtpData {

    private final Instant _requestTransmission;
    private final Instant _requestReception;
    private final Instant _responseTransmission;
    private final Instant _responseReception;

    public NtpData(Instant requestTransmission, Instant requestReception,
                   Instant responseTransmission, Instant responseReception) {

        _requestTransmission = requestTransmission;
        _requestReception = requestReception;
        _responseTransmission = responseTransmission;
        _responseReception = responseReception;
    }

    public Instant getRequestTransmission() {
        return _requestTransmission;
    }

    public Instant getRequestReception() {
        return _requestReception;
    }

    public Instant getResponseTransmission() {
        return _responseTransmission;
    }

    public Instant getResponseReception() {
        return _responseReception;
    }
}