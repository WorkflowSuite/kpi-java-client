package workflowSuite.kpiClient.time

import org.junit.Assert
import org.junit.Test
import java.time.Duration
import java.time.Instant

class TimeOffsetCalculatorTest {

    @Test
    fun calculateTimeOffset() {
        val expected = TimeSyncData(Duration.ofSeconds(0), Duration.ofSeconds(1))
        val calculator = TimeOffsetCalculator()
        val timeSyncData = calculator.calculateTimeOffset(
                NtpData(
                        Instant.parse("2018-08-18T16:48:00.00Z"),
                        Instant.parse("2018-08-18T16:48:01.00Z"),
                        Instant.parse("2018-08-18T16:48:02.00Z"),
                        Instant.parse("2018-08-18T16:48:03.00Z")
                )
        )

        Assert.assertEquals(expected.offset, timeSyncData.offset)
        Assert.assertEquals(expected.halfDelta, timeSyncData.halfDelta)
    }
}