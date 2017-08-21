package workflowsuite.kpi.client.bits;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class BitConverter {

    public static double byteArrayToDoubleLE(byte[] data, int startIndex) {
        return Double.longBitsToDouble(byteArrayToLongLE(data, startIndex));
    }

    public static long byteArrayToLongLE(byte[] data, int startIndex) {
        ByteBuffer b = ByteBuffer.wrap(data, startIndex, 8);
        b.order(ByteOrder.LITTLE_ENDIAN);
        return b.getLong();
    }
}