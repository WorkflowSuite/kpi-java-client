package workflowsuite.kpi.client.serviceregistry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public final class ServiceEndpointInfosXmlParser extends   DefaultHandler {

    private String currentElement = "";

    public ServiceEndpointsInfo serviceInfo;


    @Override
    public void startDocument() throws SAXException {
        serviceInfo = new ServiceEndpointsInfo("", "", new ArrayList<EndpointConfiguration>(0), new ArrayList<TransportSettings>(0));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentElement == "DeploymentUnitName")
            serviceInfo.setDeploymentUnitName(new String(ch, start, length));
        else if (currentElement == "ServiceKind")
            serviceInfo.setServiceKind(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentElement = "";
    }
}