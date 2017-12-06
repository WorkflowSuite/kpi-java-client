package workflowsuite.kpi.client;

import java.time.Duration;
import java.time.Instant;

public final class DurationMetricMessage {
    private String metricCode = "";
    private Instant clientEventTime = Instant.MIN;
    private Instant synchronizedEventTime = Instant.MIN;
    private Duration duration = Duration.ZERO;


    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
