package workflowsuite.kpi.client.rabbitmq;

import java.time.Duration;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;
import workflowsuite.kpi.client.DurationMetricMessage;

public class DurationMetricMessageSerializerTest {
    @Test
    public void serialize() throws Exception {
        byte[] expected = new byte[] { 13, -16, 0, 0, 1, 0, 44, 0, -89, 34, -96, 89, 0, 0, 0, 0, 0, -31, -11, 5, -89, 34, -96, 89, 0, 0, 0, 0, -128, -78, -26, 14, 10, 0, 84, 101, 115, 116, 77, 101, 116, 114, 105, 99, 16, 39, 0, 0, 0, 0, 0, 0};
        DurationMetricMessageSerializer s = new DurationMetricMessageSerializer();
        DurationMetricMessage msg = new DurationMetricMessage();
        msg.setMetricCode("TestMetric");
        msg.setClientEventTime(Instant.parse("2017-08-25T13:14:15.250Z"));
        msg.setSynchronizedEventTime(Instant.parse("2017-08-25T13:14:15.100Z"));
        msg.setDuration(Duration.ofSeconds(10));
        byte[] actual = s.serialize(msg);
        Assert.assertArrayEquals(expected, actual);
    }

}
