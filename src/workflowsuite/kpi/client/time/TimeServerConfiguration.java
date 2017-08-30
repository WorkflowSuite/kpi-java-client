package workflowsuite.kpi.client.time;

import java.io.StringReader;
import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class TimeServerConfiguration {

    protected static final long DEFAULT_CLIENT_SYNC_INTERVAL_SECONDS = 60;

    private URI endpoint;
    private long clientTimeSyncIntervalSeconds;

    /**
     * Create instance of {{@link TimeServerConfiguration}} class.
     * @param endpoint Endpoint.
     * @param transportSettings XML transport settings definition.
     */
    public static TimeServerConfiguration parse(URI endpoint, String transportSettings) {
        TimeServerConfiguration configuration = new TimeServerConfiguration();
        configuration.endpoint = endpoint;
        configuration.clientTimeSyncIntervalSeconds = parseClientTimeSyncInterval(transportSettings);
        return configuration;
    }

    public URI getEndpoint() {
        return this.endpoint;
    }

    public long getClientTimeSyncIntervalSeconds() {
        return this.clientTimeSyncIntervalSeconds;
    }

    private static long parseClientTimeSyncInterval(String transportSettings) {
        if (transportSettings.isEmpty()) {
            return DEFAULT_CLIENT_SYNC_INTERVAL_SECONDS;
        }
        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            SAXParser xmlParser = saxFactory.newSAXParser();

            TransportSettingsParser transportSettingsParser = new TransportSettingsParser();
            xmlParser.parse(new InputSource(new StringReader(transportSettings)), transportSettingsParser);
            return transportSettingsParser.getClientTimeSyncInterval();

        } catch (Exception e) {
            return DEFAULT_CLIENT_SYNC_INTERVAL_SECONDS;
        }
    }

    private static final class TransportSettingsParser extends DefaultHandler {

        private long clientTimeSyncInterval;
        private final StringBuilder buffer;

        TransportSettingsParser() {
            this.buffer = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            clear(this.buffer);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            this.buffer.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("clientTimeSyncInterval".equals(qName)) {
                clientTimeSyncInterval = LocalTime.parse(buffer, DateTimeFormatter.ISO_LOCAL_TIME).toSecondOfDay();
            }
            clear(this.buffer);
        }

        public long getClientTimeSyncInterval() {
            return clientTimeSyncInterval;
        }

        private void clear(StringBuilder b) {
                b.setLength(0);
        }
    }
}
