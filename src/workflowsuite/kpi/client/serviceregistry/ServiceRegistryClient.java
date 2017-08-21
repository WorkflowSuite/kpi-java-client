package workflowsuite.kpi.client.serviceregistry;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ServiceRegistryClient {

    private final String _serverEndpoint;

    public ServiceRegistryClient(String serverEndpoint) {

        _serverEndpoint = serverEndpoint;
    }

    public final ServiceEndpointsInfo getServiceEndpointsInfo(String contract) throws IOException, SAXException, ParserConfigurationException {

        String query = _serverEndpoint + "worklfow/serviceregistry/api/v1/serviceendpoints?contract=" + contract + "&client=java";
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/xml; charset=utf-8");
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("User-Agent", "Workflow Suite KPI Client JAVA");
        connection.setUseCaches(false);
        connection.setDefaultUseCaches(false);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                SAXParser xmlParser = saxFactory.newSAXParser();
                ServiceEndpointInfosXmlParser serviceInfosParser = new ServiceEndpointInfosXmlParser();

                InputStream stream = connection.getInputStream();

                xmlParser.parse(stream, serviceInfosParser);

                return serviceInfosParser.serviceInfo;
            }
        } catch (IOException ex) {
            return ServiceEndpointsInfo.Empty;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


        return ServiceEndpointsInfo.Empty;

    }
}