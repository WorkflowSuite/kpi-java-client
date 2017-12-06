package workflowsuite.kpi.client;

//import org.junit.Test;

import java.net.URI;
import java.time.Duration;

public class KpiClientFactoryTest {
    //@Test
    public void newKpiClient() throws Exception {
        KpiClientFactory factory = new KpiClientFactory()
                .useServiceRegistry(URI.create("http://msk-dev-foris:9130/"));

        KpiClient client = factory.newKpiClient();
        client.onCheckpoint("TestCheckpoint", "02be60e389d24425bb6e6254fbfe1cae");
        client.traceDuration("TestDurationMetric", Duration.ofSeconds(1));
        Thread.sleep(5000);
    }

}
