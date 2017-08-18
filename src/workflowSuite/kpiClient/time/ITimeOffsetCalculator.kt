package workflowSuite.kpiClient.time

interface ITimeOffsetCalculator {
    fun calculateTimeOffset(data: NtpData) : TimeSyncData
}