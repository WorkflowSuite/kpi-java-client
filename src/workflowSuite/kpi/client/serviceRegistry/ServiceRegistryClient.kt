package workflowsuite.kpi.client.serviceregistry

import java.net.HttpURLConnection
import java.net.URL
import javax.xml.XMLConstants
import javax.xml.parsers.SAXParserFactory

class ServiceRegistryClient(private val serverEndpoint: String) {
    fun getServiceEndpointsInfo(contract: String) : ServiceEndpointsInfo {

        val query = serverEndpoint + "worklfow/serviceregistry/api/v1/serviceendpoints?contract=$contract&client=java"
        val url = URL(query)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/xml; charset=utf-8")
        connection.setRequestProperty("Accept", "application/xml")
        connection.setRequestProperty("User-Agent", "Workflow Suite KPI Client JAVA");
        connection.useCaches = false
        connection.defaultUseCaches = false
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        try {
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // return connection.responseMessage
                val ct = connection.contentType
                val saxFactory = SAXParserFactory.newInstance()
                saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                val xmlParser = saxFactory.newSAXParser()
                val serviceInfosParser = ServiceEndpointInfosXmlParser()

                val stream = connection.inputStream

                xmlParser.parse(stream, serviceInfosParser)

                return serviceInfosParser.serviceInfo
            }
        }
        catch (e: java.net.SocketTimeoutException) {
            return ServiceEndpointsInfo.Empty
        }
        catch (e: java.io.IOException) {
            return ServiceEndpointsInfo.Empty
        }
        finally {
            if (connection != null) {
                connection.disconnect()
            }
        }


        return ServiceEndpointsInfo.Empty

    }
}