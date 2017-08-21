package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.bits.BitConverter;
import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;
import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.IConfigurationProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.Instant;

public final class ServerTimeProvider implements INtpDataProvider {

    private final IConfigurationProvider<TimeServerConfiguration> _configurationProvider;

    public ServerTimeProvider(IConfigurationProvider<TimeServerConfiguration> configuration) {

        _configurationProvider = configuration;
    }

    public final NtpData GetNtpData() throws ConfigurationNotFoundException, IOException {

        GetConfigurationResult<TimeServerConfiguration> result = _configurationProvider.TryGetValidConfiguration();

        if (result.getSuccess()) {
            TimeServerConfiguration configuration = result.getConfiguration();
            Socket socket = new Socket(configuration.getEndpoint().getHost(), configuration.getEndpoint().getPort());
            Instant requestTransmission = Instant.now();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[16];
            inputStream.read(bytes);
            Instant responseReception = Instant.now();
            Instant requestReception = OleAutomationDateUtil.fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 0));
            Instant responseTransmission = OleAutomationDateUtil.fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 8));

            return new NtpData(requestTransmission, requestReception,
                    responseTransmission, responseReception);
        }

        throw new ConfigurationNotFoundException();
    }
}