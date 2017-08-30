package workflowsuite.kpi.client.time;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;

import workflowsuite.kpi.client.bits.BitConverter;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

public final class ServerTimeProvider implements INtpDataProvider {
    private static final int BUFFER_SIZE = 16;
    private static final int CONNECTION_TIMEOUT_MILLIS = 2000;
    private static final int SOCKET_TIMEOUT_MILLIS = 5000;

    private final ConfigurationProvider<TimeServerConfiguration> configurationProvider;
    private final byte[] buffer;

    public ServerTimeProvider(ConfigurationProvider<TimeServerConfiguration> configuration) {
        this.configurationProvider = configuration;
        this.buffer = new byte[BUFFER_SIZE];
    }

    public NtpData GetNtpData() throws ConfigurationNotFoundException, IOException {

        GetConfigurationResult<TimeServerConfiguration> result = configurationProvider.tryGetValidConfiguration();

        if (result.getSuccess()) {
            TimeServerConfiguration configuration = result.getConfiguration();
            try(Socket socket = new Socket()) {
                socket.setReceiveBufferSize(BUFFER_SIZE);
                socket.setKeepAlive(false);
                socket.setSoTimeout(SOCKET_TIMEOUT_MILLIS);
                Instant requestTransmission = Instant.now();
                socket.connect(new InetSocketAddress(configuration.getEndpoint().getHost(),
                        configuration.getEndpoint().getPort()), CONNECTION_TIMEOUT_MILLIS);
                try(InputStream inputStream = socket.getInputStream()) {
                    int bytesRead =  inputStream.read(this.buffer);
                    if (bytesRead == BUFFER_SIZE) {
                        Instant responseReception = Instant.now();
                        Instant requestReception = OleAutomationDateUtil.fromOADate(
                                BitConverter.byteArrayToDoubleLE(this.buffer, 0));
                        Instant responseTransmission = OleAutomationDateUtil.fromOADate(
                                BitConverter.byteArrayToDoubleLE(this.buffer, 8));

                        return new NtpData(requestTransmission, requestReception,
                                responseTransmission, responseReception);
                    } else {
                        throw new ConfigurationNotFoundException(); // TODO: make specialized exception
                    }
                }
            }
        }

        throw new ConfigurationNotFoundException();
    }
}
