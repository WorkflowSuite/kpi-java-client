package workflowsuite.kpi.client.time;

public interface ITimeOffsetCalculator {
    /**
     * Calculate time offset between client and server.
     * @param data NTP result.
     * @return Calculated offset.
     */
    TimeSyncData calculateTimeOffset(NtpData data);
}
