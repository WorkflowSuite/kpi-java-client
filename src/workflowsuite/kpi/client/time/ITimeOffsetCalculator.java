package workflowsuite.kpi.client.time;

public interface ITimeOffsetCalculator {
    TimeSyncData calculateTimeOffset(NtpData data);
}
