package workflowsuite.kpi;

public final class KpiUtility {

    public static void onCheckpoint(String checkpointCode, String sessionId) {
        KpiClient kpiClient = new KpiClient();
        kpiClient.onCheckpoint(checkpointCode, sessionId);
    }
}
