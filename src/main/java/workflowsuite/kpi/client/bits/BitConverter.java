package workflowsuite.kpi.client.bits;

public final class BitConverter {

    private BitConverter() {

    }

    /**
     * Reading byte array as a int value.
     * <p> Reads four bytes at the given index,
     * composing them into a int value according to the little endian byte order.</p>
     * @param data The buffer from which it is necessary to read long.
     * @param startIndex The index from which the bytes will be read; must be non-negative and
     *         no larger than <tt>array.length - 4</tt>.
     * @return The int value at the buffer's current position.
     */
    public static int byteArrayToInt(byte[] data, int startIndex) {
        return (data[startIndex + 3] << 24)
                | ((data[startIndex + 2] & 0xff) << 16)
                | ((data[startIndex + 1] & 0xff) <<  8)
                | ((data[startIndex ] & 0xff));
    }

    /**
     * Reading byte array as a long value.
     * <p> Reads eight bytes at the given index,
     * composing them into a long value according to the little endian byte order.</p>
     * @param data The buffer from which it is necessary to read long.
     * @param startIndex The index from which the bytes will be read; must be non-negative and
     *         no larger than <tt>array.length - 8</tt>.
     * @return The long value at the buffer's current position.
     */
    public static long byteArrayToLongLE(byte[] data, int startIndex) {
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
