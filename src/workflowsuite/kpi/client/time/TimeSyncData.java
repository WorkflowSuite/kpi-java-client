package workflowsuite.kpi.client.time;

import java.time.Duration;

public final class TimeSyncData {

    private final Duration _offset;
    private final Duration _halfDelta;

    public TimeSyncData(Duration offset, Duration halfDelta) {

        _offset = offset;
        _halfDelta = halfDelta;
    }

    public Duration getOffset() {
        return _offset;
    }

    public Duration getHalfDelta() {
        return _halfDelta;
    }
}
