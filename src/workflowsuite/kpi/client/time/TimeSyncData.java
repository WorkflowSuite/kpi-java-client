package workflowsuite.kpi.client.time;

import java.time.Duration;

final class TimeSyncData {

    private final Duration offset;
    private final Duration halfDelta;

    TimeSyncData(Duration offset, Duration halfDelta) {

        this.offset = offset;
        this.halfDelta = halfDelta;
    }

    public Duration getOffset() {
        return this.offset;
    }

    public Duration getHalfDelta() {
        return this.halfDelta;
    }
}
