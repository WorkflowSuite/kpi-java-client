package workflowsuite.kpi.client.bits;

import org.junit.Assert;
import org.junit.Test;

public final class BitConverterTest {
    @Test
    public void byteArrayToLongTest() {
        long expected = 4676137844535450131L;
        byte[] bytes = new byte[] {19, (byte)218, 75, 40, (byte)152, (byte)250, (byte)228, 64};

        long actual = BitConverter.byteArrayToLongLE(bytes, 0);
        Assert.assertEquals(expected, actual);
    }
}
