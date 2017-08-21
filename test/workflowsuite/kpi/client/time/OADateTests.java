package workflowsuite.kpi.client.time;

import org.junit.Assert;
import org.junit.Test;
import java.time.Instant;

public final class OADateTests {

    @Test
    public void fromOADateTest() {
        Instant expected = Instant.parse("2017-08-17T18:07:05.00Z");
        double doublePresent = 42964.754918981482;
        Instant d = OleAutomationDateUtil.fromOADate(doublePresent);
        Assert.assertEquals(expected, d);
    }
}