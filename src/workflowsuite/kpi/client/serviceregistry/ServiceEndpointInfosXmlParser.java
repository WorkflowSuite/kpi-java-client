package workflowsuite.kpi.client.serviceregistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

final class ServiceEndpointInfosXmlParser extends   DefaultHandler {

    private State state;
    private StringBuilder tempBuffer;

    private enum State {
        NONE,
        SERVICE_ENDPOINTS_CONFIGURATION,
        ENDPOINTS,
        ENDPOINT_CONFIGURATION,
        TRANSPORT_SETTINGS,
        TRANSPORT_SETTING,
    }

    private ServiceEndpointsInfo serviceEndpointsInfo;

    protected ServiceEndpointsInfo parse(InputStream inputStream) {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        try {
            saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser xmlParser = saxFactory.newSAXParser();
            xmlParser.parse(inputStream, this);
            return serviceEndpointsInfo;
        } catch (ParserConfigurationException e) {
        } catch (SAXNotRecognizedException e) {
        } catch (SAXNotSupportedException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
        return new ServiceEndpointsInfo();
    }

    @Override
    public void startDocument() throws SAXException {
        serviceEndpointsInfo = new ServiceEndpointsInfo();
        state = State.SERVICE_ENDPOINTS_CONFIGURATION;
        tempBuffer = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempBuffer.setLength(0);
        // if local name not equals any, then current element is property name
        switch (qName) {
            case "ServiceEndpointsConfiguration":
                state = State.SERVICE_ENDPOINTS_CONFIGURATION;
                break;
            case "Endpoints":
                state = State.ENDPOINTS;
                break;
            case "d2p1:EndpointConfiguration":
                state = State.ENDPOINT_CONFIGURATION;
                serviceEndpointsInfo.endpoints.add(new EndpointConfiguration("", URI.create(""), ""));
                break;
            case "TransportSettings":
                state = State.TRANSPORT_SETTINGS;
                break;
            case "d2p1:TransportSettings":
                state = State.TRANSPORT_SETTING;
                serviceEndpointsInfo.transportSettigs.add(new TransportSettings("", "", "", ""));
                break;
            default: break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempBuffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (state) {
            case SERVICE_ENDPOINTS_CONFIGURATION:
                FillServiceEnpointConfiguration(qName, tempBuffer);
                break;
            case TRANSPORT_SETTING:
                FillTransportSetting(qName, tempBuffer);
                break;
            case ENDPOINT_CONFIGURATION:
                FillEnpointConfiguration(qName, tempBuffer);
                break;
            default: break;
        }
        tempBuffer.setLength(0);

        switch (qName) {
            case "ServiceEndpointsConfiguration":
                state = State.NONE;
                break;
            case "Endpoints":
                state = State.SERVICE_ENDPOINTS_CONFIGURATION;
                break;
            case "d2p1:EndpointConfiguration":
                state = State.ENDPOINTS;
                break;
            case "TransportSettings":
                state = State.SERVICE_ENDPOINTS_CONFIGURATION;
                break;
            case "d2p1:TransportSettings":
                state = State.TRANSPORT_SETTINGS;
                break;
            default: break;
        }
    }

    private void FillEnpointConfiguration(String currentElement, StringBuilder buffer) {
        EndpointConfiguration e = serviceEndpointsInfo.endpoints.get(serviceEndpointsInfo.endpoints.size() - 1);
        String value = buffer.toString();
        switch (currentElement) {
            case "d2p1:Address":
                e.setAddress(URI.create(value));
                break;
            case "d2p1:ServiceContract":
                e.setServiceContract(value);
                break;
            case "d2p1:TransportSettingsCode":
                e.setTransportSettingsCode(value);
                break;
            default: break;
        }
    }

    private void FillTransportSetting(String currentElement, StringBuilder buffer) {
        TransportSettings t = serviceEndpointsInfo.transportSettigs.get(
                serviceEndpointsInfo.transportSettigs.size() - 1);
        String value = buffer.toString();
        switch (currentElement) {
            case "d2p1:Body":
                t.setBody(value);
                break;
            case "d2p1:Code":
                t.setCode(value);
                break;
            case "d2p1:Name":
                t.setName(value);
                break;
            case "d2p1:TypeCode":
                t.setTypeCode(value);
                break;
            default: break;
        }
    }

    private void FillServiceEnpointConfiguration(String currentElement, StringBuilder buffer) {
        String value = buffer.toString();
        switch (currentElement) {
            case "DeploymentUnitName":
                serviceEndpointsInfo.deploymentUnitName = value;
                break;
            case "ServiceKind":
                serviceEndpointsInfo.serviceKind = value;
                break;
            default: break;
        }
    }
}
