package workflowsuite.kpi.client.serviceregistry;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public final class ServiceRegistryClientTest {
    @Test
    public void getServiceEndpointsInfo() throws ParserConfigurationException, SAXException, IOException {
        ServiceRegistryClient clinet = new ServiceRegistryClient("http://msk-dev-foris:9130/");
        ServiceEndpointsInfo r = clinet.getServiceEndpointsInfo("http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer");
    }

}