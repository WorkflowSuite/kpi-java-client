package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.IConfigurationProvider;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class TimeSynhronizer {
    private final IConfigurationProvider<TimeServerConfiguration> configurationProvider;
    private final INtpDataProvider ntpDataProvider;
    private final ITimeOffsetCalculator timeOffsetCalculator;

    private Duration lastOffset;

    public TimeSynhronizer(IConfigurationProvider<TimeServerConfiguration> configurationProvider,
                           INtpDataProvider ntpDataProvider, ITimeOffsetCalculator timeOffsetCalculator) {

        this.configurationProvider = configurationProvider;
        this.ntpDataProvider = ntpDataProvider;
        this.timeOffsetCalculator = timeOffsetCalculator;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long timeoutSeconds = TimeServerConfiguration.DEFAULT_CLIENT_SYNC_INTERVAL_SECONDS;
        GetConfigurationResult<TimeServerConfiguration> configurationResult = configurationProvider.TryGetValidConfiguration();
        if (configurationResult.getSuccess()) {
            timeoutSeconds = configurationResult.getConfiguration().getClientTimeSyncIntervalSeconds();
        }
        scheduler.scheduleWithFixedDelay(this::Synchronize, 0, timeoutSeconds, TimeUnit.SECONDS);
    }

    private void Synchronize() {
        try {
            NtpData ntpData = this.ntpDataProvider.GetNtpData();
            TimeSyncData timeOffset = this.timeOffsetCalculator.calculateTimeOffset(ntpData);
            this.lastOffset = timeOffset.getOffset();
        }
        catch (Exception e) {

        }
    }
}
