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

    private final URI endpoint;
    private final long clientTimeSyncIntervalSeconds;

    public TimeServerConfiguration(URI endpoint, String transportSettings) {
        this.endpoint = endpoint;
        this.clientTimeSyncIntervalSeconds = parseClientTimeSyncInterval(transportSettings);
    }

    public URI getEndpoint() {
        return this.endpoint;
    }

    public long getClientTimeSyncIntervalSeconds() {
        return this.clientTimeSyncIntervalSeconds;
    }

    private long parseClientTimeSyncInterval(String transportSettings) {
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

    private final class TransportSettingsParser extends DefaultHandler {

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
