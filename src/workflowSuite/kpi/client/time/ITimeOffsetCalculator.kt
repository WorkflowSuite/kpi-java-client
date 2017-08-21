package workflowsuite.kpi.client.time

interface ITimeOffsetCalculator {
    fun calculateTimeOffset(data: NtpData) : TimeSyncData
}