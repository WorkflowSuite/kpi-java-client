package workflowsuite.kpi.client.serviceregistry;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class ServiceRegistryClientTest {
    //@Test
    public void getServiceEndpointsInfo() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        ServiceRegistryClient clinet = new ServiceRegistryClient(new URI("http://msk-dev-foris:9130/"),
                org.slf4j.LoggerFactory.getILoggerFactory());
        ServiceEndpointsInfo r = clinet.getServiceEndpointsInfo("http://schemas.sitels.ru/marti/workflow/kpi/services/TimeServer");
    }

}
