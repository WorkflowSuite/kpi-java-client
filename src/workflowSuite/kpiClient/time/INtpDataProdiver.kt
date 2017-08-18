package workflowSuite.kpiClient.time

interface INtpDataProvider {
    fun GetNtpData(): NtpData
}