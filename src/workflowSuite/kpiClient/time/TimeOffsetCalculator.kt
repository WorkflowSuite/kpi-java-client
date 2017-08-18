package workflowSuite.kpiClient.time

import java.time.Duration

class TimeOffsetCalculator : ITimeOffsetCalculator {

    override fun calculateTimeOffset(data: NtpData): TimeSyncData {
        val t1 = data.requestTransmission
        val t2 = data.requestReception
        val t3 = data.responseTransmission
        val t4 = data.responseReception

        val d41 = Duration.between(t1, t4)
        val d32 = Duration.between(t2, t3)
        val halfDelta = (d41 - d32).toMillis() / 2
        val offset = Duration.between(t4, t3).toMillis() + halfDelta

        return TimeSyncData(Duration.ofMillis(offset), Duration.ofMillis(halfDelta))
    }
}