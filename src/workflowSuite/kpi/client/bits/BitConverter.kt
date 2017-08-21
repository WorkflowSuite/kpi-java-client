package workflowsuite.kpi.client.bits

import java.nio.ByteBuffer
import java.nio.ByteOrder


class BitConverter {
    companion object {

        fun byteArrayToDoubleLE(data: ByteArray, startIndex: Int): Double {

            return java.lang.Double.longBitsToDouble(byteArrayToLongLE(data, startIndex))
        }

        fun byteArrayToLongLE(data: ByteArray, startIndex: Int): Long {
            // TODO: remove allocation
            val b = ByteBuffer.wrap(data, startIndex, 8)
            b.order(ByteOrder.LITTLE_ENDIAN)
            return b.long
        }
    }
}