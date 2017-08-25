package workflowsuite.kpi.client.time;

import workflowsuite.kpi.client.settings.GetConfigurationResult;
import workflowsuite.kpi.client.settings.ConfigurationProvider;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class TimeSynchronizer {
    private final INtpDataProvider ntpDataProvider;
    private final ITimeOffsetCalculator timeOffsetCalculator;

    private Duration lastOffset;

    public TimeSynchronizer(ConfigurationProvider<TimeServerConfiguration> configurationProvider,
                            INtpDataProvider ntpDataProvider, ITimeOffsetCalculator timeOffsetCalculator) {

        this.ntpDataProvider = ntpDataProvider;
        this.timeOffsetCalculator = timeOffsetCalculator;
        this.lastOffset = Duration.ofSeconds(0);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long timeoutSeconds = TimeServerConfiguration.DEFAULT_CLIENT_SYNC_INTERVAL_SECONDS;
        GetConfigurationResult<TimeServerConfiguration> configurationResult = configurationProvider.tryGetValidConfiguration();
        if (configurationResult.getSuccess()) {
            timeoutSeconds = configurationResult.getConfiguration().getClientTimeSyncIntervalSeconds();
        }
        scheduler.scheduleWithFixedDelay(this::Synchronize, 0, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Duration getOffset() {return this.lastOffset;}

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
