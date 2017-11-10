package workflowsuite.kpi.client.time;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;
import javax.net.SocketFactory;

import workflowsuite.kpi.client.bits.BitConverter;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

public final class ServerTimeProvider implements INtpDataProvider {
    private static final int TIMESTAMP_SIZE = Long.BYTES + Integer.BYTES;
    private static final int BUFFER_SIZE = TIMESTAMP_SIZE * 2;
    private static final int CONNECTION_TIMEOUT_MILLIS = 2000;
    private static final int SOCKET_TIMEOUT_MILLIS = 5000;

    private final ConfigurationProvider<TimeServerConfiguration> configurationProvider;
    private final SocketFactory socketFactory;
    private final byte[] buffer;

    /**
     * Create instance of {{@link ServerTimeProvider}} class.
     * @param configuration Configuration provider for getting ntp settings.
     */
    public ServerTimeProvider(ConfigurationProvider<TimeServerConfiguration> configuration,
                              SocketFactory socketFactory) {
        this.configurationProvider = configuration;
        this.socketFactory = socketFactory;
        this.buffer = new byte[BUFFER_SIZE];
    }

    /**
     * Connect by tcp to ntp server and calculate ntp data.
     * @return NTP data.
     */
    @Override
    public NtpData getNtpData() {

        GetConfigurationResult<TimeServerConfiguration> result = configurationProvider.tryGetValidConfiguration();

        if (result.getSuccess()) {
            TimeServerConfiguration configuration = result.getConfiguration();
            try (Socket socket = socketFactory.createSocket()) {
                socket.setReceiveBufferSize(BUFFER_SIZE);
                socket.setKeepAlive(false);
                socket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
                Instant requestTransmission = Instant.now();
                socket.connect(new InetSocketAddress(configuration.getEndpoint().getHost(),
                        configuration.getEndpoint().getPort()), CONNECTION_TIMEOUT_MILLIS);
                try (InputStream inputStream = socket.getInputStream()) {
                    int bytesRead =  inputStream.read(this.buffer);
                    if (bytesRead == BUFFER_SIZE) {
                        Instant responseReception = Instant.now();
                        Instant requestReception = parseInstant(buffer, 0);
                        Instant responseTransmission = parseInstant(buffer, TIMESTAMP_SIZE);

                        return new NtpData(requestTransmission, requestReception,
                                responseTransmission, responseReception);
                    } else {
                        return NtpData.EMPTY;
                    }
                }
            } catch (IOException e) {
                // if could not connect return empty ntp data.
                return NtpData.EMPTY;
            }
        }

        return NtpData.EMPTY;
    }

    private static Instant parseInstant(byte[] buffer, int startIndex) {
        long seconds = BitConverter.byteArrayToLongLE(buffer, startIndex);
        long nanoSeconds = BitConverter.byteArrayToInt(buffer, startIndex + Long.BYTES);
        return Instant.ofEpochSecond(seconds, nanoSeconds);
    }
}
