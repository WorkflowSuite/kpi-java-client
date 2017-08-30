package workflowsuite.kpi.client.time;

import java.time.Duration;
import java.time.Instant;

public final class TimeOffsetCalculator implements ITimeOffsetCalculator {


    /**
     * {@inheritDoc}
     */
    @Override
    public TimeSyncData calculateTimeOffset(NtpData data) {
        Instant t1 = data.getRequestTransmission();
        Instant t2 = data.getRequestReception();
        Instant t3 = data.getResponseTransmission();
        Instant t4 = data.getResponseReception();

        Duration d41 = Duration.between(t1, t4);
        Duration d32 = Duration.between(t2, t3);
        long halfDelta = (d41.minus(d32)).toMillis() / 2;
        long offset = Duration.between(t4, t3).toMillis() + halfDelta;

        return new TimeSyncData(Duration.ofMillis(offset), Duration.ofMillis(halfDelta));
    }
}
