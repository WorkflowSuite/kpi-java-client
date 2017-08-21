package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.ConfigurationNotFoundException;

import java.io.IOException;

interface INtpDataProvider {
    NtpData GetNtpData() throws ConfigurationNotFoundException, IOException;
}