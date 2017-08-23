package workflowsuite.kpi;

import java.net.URI;
import java.net.URISyntaxException;

public final class KpiUtility {

    public static void onCheckpoint(String checkpointCode, String sessionId) throws URISyntaxException {
        KpiClient kpiClient = new KpiClient(new URI(""));
        kpiClient.onCheckpoint(checkpointCode, sessionId);
    }
}
