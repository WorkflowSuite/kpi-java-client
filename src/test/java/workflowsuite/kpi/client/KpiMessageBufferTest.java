package workflowsuite.kpi.client;

import org.junit.Assert;
import org.junit.Test;

public class KpiMessageBufferTest {
    @Test
    public void offer() throws Exception {
        KpiMessageBuffer buffer = new KpiMessageBuffer(2, org.slf4j.LoggerFactory.getILoggerFactory());
        buffer.offer(new KpiMessage());
        Assert.assertEquals(1, buffer.size());
        buffer.offer(new KpiMessage());
        Assert.assertEquals(2, buffer.size());
        buffer.offer(new KpiMessage());
        Assert.assertEquals(2, buffer.size());
    }

    @Test
    public void take() throws Exception {
        KpiMessageBuffer buffer = new KpiMessageBuffer(2, org.slf4j.LoggerFactory.getILoggerFactory());
        KpiMessage m = new KpiMessage();
        m.setSessionId("0");
        buffer.offer(m);
        m = new KpiMessage();
        m.setSessionId("1");
        buffer.offer(m);
        m = new KpiMessage();
        m.setSessionId("2");
        buffer.offer(m);
        Assert.assertEquals(2, buffer.size());

        m = buffer.poll();
        buffer.remove(m);
        Assert.assertEquals("1", m.getSessionId());
        Assert.assertEquals(1, buffer.size());

        m = buffer.poll();
        buffer.remove(m);
        Assert.assertEquals("2", m.getSessionId());
        Assert.assertEquals(0, buffer.size());
    }
}
