package workflowsuite.kpi.client;

import java.time.Instant;

public final class KpiMessage {
    private String checkpointCode = "";
    private Instant clientEventTime = Instant.MIN;
    private Instant synchronizedEventTime = Instant.MIN;
    private String sessionId = "";


    public Instant getClientEventTime() {
        return clientEventTime;
    }

    public void setClientEventTime(Instant clientEventTime) {
        this.clientEventTime = clientEventTime;
    }

    public Instant getSynchronizedEventTime() {
        return synchronizedEventTime;
    }

    public void setSynchronizedEventTime(Instant synchronizedEventTime) {
        this.synchronizedEventTime = synchronizedEventTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCheckpointCode() {
        return checkpointCode;
    }

    public void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }
}
