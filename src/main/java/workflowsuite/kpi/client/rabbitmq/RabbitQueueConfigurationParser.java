package workflowsuite.kpi.client.rabbitmq;

import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import workflowsuite.kpi.client.serviceregistry.ServiceEndpointsInfo;
import workflowsuite.kpi.client.serviceregistry.TransportSettings;

class RabbitQueueConfigurationParser extends DefaultHandler {

    private RabbitQueueConfiguration configuration;
    private final StringBuilder buffer;

    RabbitQueueConfigurationParser() {
        buffer = new StringBuilder();
    }

    public static RabbitQueueConfiguration parse(String contract,
                                                 ServiceEndpointsInfo endpointsInfo) {
        if (endpointsInfo.endpoints.size() == 0) {
            return new RabbitQueueConfiguration();
        }

        RabbitQueueConfigurationParser parser = new RabbitQueueConfigurationParser();
        parser.configuration = new RabbitQueueConfiguration();
        parser.configuration.setQueue(getQueueNameFromContract(contract));
        parser.configuration.setEndpoint(endpointsInfo.endpoints.get(0).getAddress());

        if (endpointsInfo.transportSettigs.size() > 0) {
            TransportSettings transportSettings = endpointsInfo.transportSettigs.get(0);
            if (!transportSettings.getBody().isEmpty()) {
                try {
                    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                    saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    SAXParser xmlParser = saxFactory.newSAXParser();

                    xmlParser.parse(new InputSource(new StringReader(transportSettings.getBody())), parser);

                } catch (Exception e) {
                    return new RabbitQueueConfiguration();
                }
            }
        }

        return parser.configuration;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.buffer.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "username":
                this.configuration.setUserName(this.buffer.toString());
                break;
            case "password":
                this.configuration.setPassword(this.buffer.toString());
                break;
            case "durable":
                this.configuration.setDurable(isTrue(this.buffer));
                break;
            case "exclusive":
                this.configuration.setExclusive(isTrue(this.buffer));
                break;
            case "autodelete":
                this.configuration.setAutoDelete(isTrue(this.buffer));
                break;
            case "deadLetterExchange":
                this.configuration.setDeadLetterExchange(this.buffer.toString());
                break;
            case "deadLetterRoutingKey":
                this.configuration.setDeadLetterRoutingKey(this.buffer.toString());
                break;
            default: break;
        }

        buffer.setLength(0);
    }

    private static boolean isTrue(StringBuilder buffer) {
        return buffer.length() == 4 && buffer.indexOf("true") >= 0;
    }

    private static String getQueueNameFromContract(String contract) {
        int lastDelimiterIndex = contract.lastIndexOf("/");
        if (lastDelimiterIndex >= 0) {
            return contract.substring(lastDelimiterIndex + 1);
        }
        return contract;
    }
}
