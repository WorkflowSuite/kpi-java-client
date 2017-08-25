package workflowsuite.kpi.client.serviceregistry;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public final class ServiceEndpointInfosXmlParser extends   DefaultHandler {

    private State _state;
    private StringBuilder _buffer;

    private enum State {
        NONE,
        SERVICEENDPOINTSCONFIGURATION,
        ENDPOINTS,
        EndpointConfiguration,
        TRANSPORTSETTINGS,
        TRANSPORTSETTING,
    }

    private ServiceEndpointsInfo _serviceInfo;

    @NotNull
    public ServiceEndpointsInfo parse(@NotNull InputStream inputStream) {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        try {
            saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser xmlParser = saxFactory.newSAXParser();
            xmlParser.parse(inputStream, this);
            return _serviceInfo;
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
        _serviceInfo = new ServiceEndpointsInfo();
        _state = State.SERVICEENDPOINTSCONFIGURATION;
        _buffer = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        _buffer.setLength(0);
        // if local name not equals any, then current element is property name
        switch (qName) {
            case "ServiceEndpointsConfiguration":
                _state = State.SERVICEENDPOINTSCONFIGURATION;
                break;
            case "Endpoints":
                _state = State.ENDPOINTS;
                break;
            case "d2p1:EndpointConfiguration":
                _state = State.EndpointConfiguration;
                _serviceInfo.endpoints.add(new EndpointConfiguration("", URI.create(""), ""));
                break;
            case "TransportSettings":
                _state = State.TRANSPORTSETTINGS;
                break;
            case "d2p1:TransportSettings":
                _state = State.TRANSPORTSETTING;
                _serviceInfo.transportSettigs.add(new TransportSettings("", "", "", ""));
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        _buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (_state) {
            case SERVICEENDPOINTSCONFIGURATION:
                FillServiceEnpointConfiguration(qName, _buffer);
                break;
            case TRANSPORTSETTING:
                FillTransportSetting(qName, _buffer);
                break;
            case EndpointConfiguration:
                FillEnpointConfiguration(qName, _buffer);
                break;
        }
        _buffer.setLength(0);

        switch (qName) {
            case "ServiceEndpointsConfiguration":
                _state = State.NONE;
                break;
            case "Endpoints":
                _state = State.SERVICEENDPOINTSCONFIGURATION;
                break;
            case "d2p1:EndpointConfiguration":
                _state = State.ENDPOINTS;
                break;
            case "TransportSettings":
                _state = State.SERVICEENDPOINTSCONFIGURATION;
            case "d2p1:TransportSettings":
                _state = State.TRANSPORTSETTINGS;
                break;
        }
    }

    private void FillEnpointConfiguration(String currentElement, StringBuilder buffer) {
        EndpointConfiguration e = _serviceInfo.endpoints.get(_serviceInfo.endpoints.size() - 1);
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
        }
    }

    private void FillTransportSetting(String currentElement, StringBuilder buffer) {
        TransportSettings t = _serviceInfo.transportSettigs.get(_serviceInfo.transportSettigs.size() - 1);
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
        }
    }

    private void FillServiceEnpointConfiguration(String currentElement, StringBuilder buffer) {
        String value = buffer.toString();
        switch (currentElement) {
            case "DeploymentUnitName":
                _serviceInfo.deploymentUnitName = value;
                break;
            case "ServiceKind":
                _serviceInfo.serviceKind = value;
                break;
        }
    }
}