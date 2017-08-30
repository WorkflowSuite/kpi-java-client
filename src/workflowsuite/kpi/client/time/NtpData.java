package workflowsuite.kpi.client.time;

import java.time.Instant;

final class NtpData {
    private final Instant requestTransmission;
    private final Instant requestReception;
    private final Instant responsetransmission;
    private final Instant responseReception;

    NtpData(Instant requestTransmission, Instant requestReception,
            Instant responseTransmission, Instant responseReception) {

        this.requestTransmission = requestTransmission;
        this.requestReception = requestReception;
        responsetransmission = responseTransmission;
        this.responseReception = responseReception;
    }

    public Instant getRequestTransmission() {
        return requestTransmission;
    }

    public Instant getRequestReception() {
        return requestReception;
    }

    public Instant getResponseTransmission() {
        return responsetransmission;
    }

    public Instant getResponseReception() {
        return responseReception;
    }
}
