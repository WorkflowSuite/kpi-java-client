package workflowsuite.kpi.client;

import java.time.Duration;

public interface KpiClient {

    Duration MAX_DURATION = Duration.ofMillis(Long.MAX_VALUE / 10000 - 1);

    /**
     * Registers a checkpoint crossing fact for an explicitly specified session.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    boolean onCheckpoint(String checkpointCode, String sessionId);

    /**
     * Notifies that specified checkpoint is unreachable in the current execution path
     * and all started rules which should be ended in the specified checkpoint should be cancelled.
     * @param checkpointCode Unique checkpoint code. If the code is invalid, the method does nothing.
     * @param sessionId Explicit kpi session identifier.
     * @return {@code true}
     */
    boolean unreachableCheckpoint(String checkpointCode, String sessionId);

    /**
     * Trace duration metric.
     * @param metricCode Unique metric code. If the code is invalid, the method does nothing.
     * @param value Value of metric.
     * @return {@code true}
     */
    boolean traceDuration(String metricCode, Duration value);
}
