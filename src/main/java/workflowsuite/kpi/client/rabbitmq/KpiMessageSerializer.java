package workflowsuite.kpi.client.rabbitmq;

import workflowsuite.kpi.client.KpiMessage;

final class KpiMessageSerializer extends MessageSerializerBase {
    private static final byte[] HEADER_BYTES = new byte[]{-19, -2, 0, 0};

    protected byte[] serialize(final KpiMessage message) {
        short payloadSize = calculatePayloadSize(message);

        int messageSize = HEADER_SIZE + PACKAGE_VERSION_SIZE + PAYLOAD_LENGTH_SIZE + payloadSize;
        byte[] result = new byte[messageSize];
        int pos = 0;

        pos = putBytes(result, pos, HEADER_BYTES);
        pos = putShortLE(result, pos, PACKAGE_VERSION);
        pos = putShortLE(result, pos, payloadSize);
        // payload
        // synchronized event time
        pos = putLongLE(result, pos, message.getSynchronizedEventTime().getEpochSecond());
        pos = putIntLE(result, pos, message.getSynchronizedEventTime().getNano());
        // client event time
        pos = putLongLE(result, pos, message.getClientEventTime().getEpochSecond());
        pos = putIntLE(result, pos, message.getClientEventTime().getNano());
        // session id
        pos = putUtf8String(result, pos, message.getSessionId());
        // check point code
        pos = putUtf8String(result, pos, message.getCheckpointCode());
        // is unreacheble
        pos = putBoolean(result, pos, message.isUnreachable());
        // process type id
        pos = putZeroLong(result, pos);
        // len of activity code = 0
        pos = putZeroShort(result, pos);
        // is before activity not set
        pos = putByte(result, pos, (byte) -1);
        // len of pid = 0
        pos = putZeroShort(result, pos);
        // len of root pid = 0
        putZeroShort(result, pos);

        return result;
    }

    private static short calculatePayloadSize(KpiMessage message) {
        int length =  TIMESTAMP_SIZE
                + TIMESTAMP_SIZE
                + UTF8_LENGTH_SIZE + utf8EncodedLength(message.getSessionId())
                + UTF8_LENGTH_SIZE + utf8EncodedLength(message.getCheckpointCode())
                + Byte.BYTES
                // we don't send rest fields.
                + Long.BYTES + Short.BYTES + Byte.BYTES + Short.BYTES + Short.BYTES;
        return (short) length;
    }
}
