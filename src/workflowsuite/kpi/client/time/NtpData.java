package workflowsuite.kpi.client.time;

import java.time.Instant;

final class NtpData {
    private final Instant requestTransmission;
    private final Instant requestReception;
    private final Instant responseTransmission;
    private final Instant responseReception;

    NtpData(Instant requestTransmission, Instant requestReception,
            Instant responseTransmission, Instant responseReception) {

        this.requestTransmission = requestTransmission;
        this.requestReception = requestReception;
        this.responseTransmission = responseTransmission;
        this.responseReception = responseReception;
    }

    public Instant getRequestTransmission() {
        return requestTransmission;
    }

    public Instant getRequestReception() {
        return requestReception;
    }

    public Instant getResponseTransmission() {
        return responseTransmission;
    }

    public Instant getResponseReception() {
        return responseReception;
    }
}
