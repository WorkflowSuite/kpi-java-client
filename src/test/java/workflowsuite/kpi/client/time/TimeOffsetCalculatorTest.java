package workflowsuite.kpi.client.time;

import org.junit.Assert;
import org.junit.Test;
import java.time.Duration;
import java.time.Instant;

public final class TimeOffsetCalculatorTest {

    @Test
    public void calculateTimeOffset() {
        TimeSyncData expected = new TimeSyncData(Duration.ofSeconds(0), Duration.ofSeconds(1));
        TimeOffsetCalculator calculator = new TimeOffsetCalculator();
        TimeSyncData timeSyncData = calculator.calculateTimeOffset(
                new NtpData(
                        Instant.parse("2018-08-18T16:48:00.00Z"),
                        Instant.parse("2018-08-18T16:48:01.00Z"),
                        Instant.parse("2018-08-18T16:48:02.00Z"),
                        Instant.parse("2018-08-18T16:48:03.00Z")
                )
        );

        Assert.assertEquals(expected.getOffset(), timeSyncData.getOffset());
        Assert.assertEquals(expected.getHalfDelta(), timeSyncData.getHalfDelta());
    }
}