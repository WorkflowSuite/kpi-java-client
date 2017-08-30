package workflowsuite.kpi.client.bits;

public final class BitConverter {

    private BitConverter() {

    }

    public static double byteArrayToDoubleLE(byte[] data, int startIndex) {
        return Double.longBitsToDouble(byteArrayToLongLE(data, startIndex));
    }

    protected static long byteArrayToLongLE(byte[] data, int startIndex) {
        return ((long) data[startIndex + 7] << 56)
                | (((long) data[startIndex + 6] & 0xff) << 48)
                | (((long) data[startIndex + 5] & 0xff) << 40)
                | (((long) data[startIndex + 4] & 0xff) << 32)
                | (((long) data[startIndex + 3] & 0xff) << 24)
                | (((long) data[startIndex + 2] & 0xff) << 16)
                | (((long) data[startIndex + 1] & 0xff) <<  8)
                | (((long) data[startIndex ] & 0xff));
    }
}
