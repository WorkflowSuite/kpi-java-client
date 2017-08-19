package workflowSuite.kpiClient.serviceRegistry

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class ServiceEndpointInfosXmlParser : DefaultHandler() {

    private var currentElement: String = ""

    var serviceInfo : ServiceEndpointsInfo = ServiceEndpointsInfo.Empty

    override fun startDocument() {
        serviceInfo = ServiceEndpointsInfo.CreateInstance()
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        currentElement = if (qName == null) "" else qName
        /*when(currentElement) {
            "EndpointConfiguration" -> serviceInfo.endpoints.
        }*/
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        when (currentElement) {
            "DeploymentUnitName" -> serviceInfo.deploymentUnitName = toString(ch, start, length)
            "ServiceKind" -> serviceInfo.serviceKind = toString(ch, start, length)
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        currentElement = ""
    }

    private fun toString(ch: CharArray?, start: Int, length: Int) : String{
        return if (ch == null) "" else String(ch, start, length)
    }
}