package workflowsuite.kpi.client;

import workflowsuite.kpi.KpiPoint;

import java.time.Instant;
import java.time.LocalDateTime;

public final class KpiMessage {
    private KpiPoint point;
    private LocalDateTime localTime;
    private Instant UtcTime;
    private String sessionId;

    public KpiPoint getPoint() {
        return point;
    }

    public void setPoint(KpiPoint point) {
        this.point = point;
    }

    public LocalDateTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalDateTime localTime) {
        this.localTime = localTime;
    }

    public Instant getUtcTime() {
        return UtcTime;
    }

    public void setUtcTime(Instant utcTime) {
        UtcTime = utcTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isTimeAdjusted() {
        return isTimeAdjusted;
    }

    public void setTimeAdjusted(boolean timeAdjusted) {
        isTimeAdjusted = timeAdjusted;
    }

    private boolean isTimeAdjusted;
}
