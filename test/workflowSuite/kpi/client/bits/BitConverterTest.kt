package workflowsuite.kpi.client.bits

import org.junit.Assert
import org.junit.Test

class BitConverterTest {
    @Test
    fun byteArrayToLongTest() {
        val expected = 4676137844535450131
        val bytes = byteArrayOf(19, 218.toByte(), 75, 40, 152.toByte(), 250.toByte(), 228.toByte(), 64)

        val actual = BitConverter.byteArrayToLongLE(bytes, 0)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun byteArrayToDoubleTest() {
        val expected = 42964.754918981482
        val bytes = byteArrayOf(19, 218.toByte(), 75, 40, 152.toByte(), 250.toByte(), 228.toByte(), 64)

        val actual = BitConverter.byteArrayToDoubleLE(bytes, 0)
        Assert.assertEquals(expected, actual, 0.0)
    }
}