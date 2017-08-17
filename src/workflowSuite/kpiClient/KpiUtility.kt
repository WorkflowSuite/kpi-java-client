package workflowSuite.kpiClient

fun onCheckpoint(checkpointCode: String, sessionId: String) {
    val kpiClient = KpiClient()
    kpiClient.onCheckpoint(checkpointCode, sessionId)
}