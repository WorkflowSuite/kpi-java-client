package workflowsuite.kpi.client.rabbitmq;

import workflowsuite.kpi.client.DurationMetricMessage;

final class DurationMetricMessageSerializer extends MessageSerializerBase {
    private static final byte[] HEADER_BYTES = new byte[]{13, -16, 0, 0};

    protected byte[] serialize(final DurationMetricMessage message) {
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
        // metric code
        pos = putUtf8String(result, pos, message.getMetricCode());
        // duration
        putLongLE(result, pos, message.getDuration().toMillis());

        return result;
    }

    private static short calculatePayloadSize(DurationMetricMessage message) {
        int length =  TIMESTAMP_SIZE
                + TIMESTAMP_SIZE
                + UTF8_LENGTH_SIZE + utf8EncodedLength(message.getMetricCode())
                + Long.BYTES;
        return (short) length;
    }
}
