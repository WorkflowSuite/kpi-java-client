package workflowsuite.kpi.client.time

import java.time.Duration

data class TimeSyncData(val offset: Duration, val halfDelta: Duration)