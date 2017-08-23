package workflowsuite.kpi.client.time;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class TimeSyncData {

    private final Duration offset;
    private final Duration halfDelta;

    public TimeSyncData(@NotNull Duration offset,@NotNull Duration halfDelta) {

        this.offset = offset;
        this.halfDelta = halfDelta;
    }

    @Contract(pure = true)
    @NotNull
    public Duration getOffset() {
        return this.offset;
    }

    @Contract(pure = true)
    @NotNull
    public Duration getHalfDelta() {
        return this.halfDelta;
    }
}
