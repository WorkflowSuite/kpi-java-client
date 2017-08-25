package workflowsuite.kpi.client.time;

import org.jetbrains.annotations.NotNull;
import workflowsuite.kpi.client.bits.BitConverter;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;

public final class ServerTimeProvider implements INtpDataProvider {

    private final ConfigurationProvider<TimeServerConfiguration> _configurationProvider;

    public ServerTimeProvider(@NotNull ConfigurationProvider<TimeServerConfiguration> configuration) {
        _configurationProvider = configuration;
    }

    @NotNull
    public final NtpData GetNtpData() throws ConfigurationNotFoundException, IOException {

        GetConfigurationResult<TimeServerConfiguration> result = _configurationProvider.tryGetValidConfiguration();

        if (result.getSuccess()) {
            TimeServerConfiguration configuration = result.getConfiguration();
            Socket socket = new Socket();
            Instant requestTransmission = Instant.now();
            socket.connect(new InetSocketAddress(configuration.getEndpoint().getHost(), configuration.getEndpoint().getPort()), 1000);
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[16];
            int bytesRead =  inputStream.read(bytes);
            if (bytesRead == 16) {
                Instant responseReception = Instant.now();
                Instant requestReception = OleAutomationDateUtil.fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 0));
                Instant responseTransmission = OleAutomationDateUtil.fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 8));

                return new NtpData(requestTransmission, requestReception,
                        responseTransmission, responseReception);
            }
            else {
                throw new ConfigurationNotFoundException();
            }
        }

        throw new ConfigurationNotFoundException();
    }
}