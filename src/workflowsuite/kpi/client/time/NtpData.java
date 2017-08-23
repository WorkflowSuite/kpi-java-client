package workflowsuite.kpi.client.time;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

final class NtpData {
    private final Instant _requestTransmission;
    private final Instant _requestReception;
    private final Instant _responseTransmission;
    private final Instant _responseReception;

    NtpData(@NotNull Instant requestTransmission, @NotNull Instant requestReception,
            @NotNull Instant responseTransmission, @NotNull Instant responseReception) {

        _requestTransmission = requestTransmission;
        _requestReception = requestReception;
        _responseTransmission = responseTransmission;
        _responseReception = responseReception;
    }

    @Contract(pure = true)
    @NotNull
    public Instant getRequestTransmission() {
        return _requestTransmission;
    }

    @Contract(pure = true)
    @NotNull
    public Instant getRequestReception() {
        return _requestReception;
    }

    @Contract(pure = true)
    @NotNull
    public Instant getResponseTransmission() {
        return _responseTransmission;
    }

    @Contract(pure = true)
    @NotNull
    public Instant getResponseReception() {
        return _responseReception;
    }
}