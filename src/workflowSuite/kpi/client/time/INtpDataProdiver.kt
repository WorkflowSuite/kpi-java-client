package workflowsuite.kpi.client.time

interface INtpDataProvider {
    fun GetNtpData(): NtpData
}