package workflowsuite.kpi.client;

import org.junit.Test;

import java.net.URI;

public class KpiClientFactoryTest {
    //@Test
    public void newKpiClient() throws Exception {
        KpiClientFactory factory = new KpiClientFactory()
                .useServiceRegistry(URI.create("http://msk-dev-foris:9130/"));

        KpiClient client = factory.newKpiClient();
        client.onCheckpoint("Код контрольной точки согласно ЧТЗ", "ID сессии из rabbit сообщения");
    }

}
