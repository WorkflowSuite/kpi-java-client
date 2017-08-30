package workflowsuite.kpi.client.time;

import java.io.IOException;

import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;

interface INtpDataProvider {
    NtpData GetNtpData() throws ConfigurationNotFoundException, IOException;
}
