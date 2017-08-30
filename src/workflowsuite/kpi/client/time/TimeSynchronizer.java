package workflowsuite.kpi.client.time;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import workflowsuite.kpi.client.settings.ConfigurationProvider;
import workflowsuite.kpi.client.settings.GetConfigurationResult;

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
        GetConfigurationResult<TimeServerConfiguration> configurationResult
                = configurationProvider.tryGetValidConfiguration();
        if (configurationResult.getSuccess()) {
            timeoutSeconds = configurationResult.getConfiguration().getClientTimeSyncIntervalSeconds();
        }
        scheduler.scheduleWithFixedDelay(this::synchronize, 0, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Duration getOffset() {
        return this.lastOffset;
    }

    private void synchronize() {
        try {
            NtpData ntpData = this.ntpDataProvider.getNtpData();
            TimeSyncData timeOffset = this.timeOffsetCalculator.calculateTimeOffset(ntpData);
            this.lastOffset = timeOffset.getOffset();
        }  catch (Exception e) {

        }
    }
}
