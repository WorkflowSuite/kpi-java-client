package workflowsuite.kpi.client.rabbitmq;

import org.junit.Assert;
import org.junit.Test;
import workflowsuite.kpi.client.KpiMessage;

import java.time.Instant;

public class KpiMessageSerializerTest {
    @Test
    public void serialize() throws Exception {
        byte[] expected = new byte[] { -19, -2, 0, 0, 1, 0, 90, 0, -89, 34, -96, 89, 0, 0, 0, 0, 0, -31, -11, 5, -89, 34, -96, 89, 0, 0, 0, 0, -128, -78, -26, 14, 32, 0, 48, 50, 98, 101, 54, 48, 101, 51, 56, 57, 100, 50, 52, 52, 50, 53, 98, 98, 54, 101, 54, 50, 53, 52, 102, 98, 102, 101, 49, 99, 97, 101, 14, 0, 84, 101, 115, 116, 67, 104, 101, 99, 107, 112, 111, 105, 110, 116, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0 };
        KpiMessageSerializer s = new KpiMessageSerializer();
        KpiMessage msg = new KpiMessage();
        msg.setSessionId("02be60e389d24425bb6e6254fbfe1cae");
        msg.setClientEventTime(Instant.parse("2017-08-25T13:14:15.250Z"));
        msg.setSynchronizedEventTime(Instant.parse("2017-08-25T13:14:15.100Z"));
        msg.setCheckpointCode("TestCheckpoint");
        msg.setUnreachable(true);
        byte[] actual = s.serialize(msg);
        Assert.assertArrayEquals(expected, actual);
    }

}
