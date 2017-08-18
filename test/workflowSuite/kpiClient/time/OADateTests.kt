package workflowSuite.kpiClient.time

import org.junit.Assert
import org.junit.Test
import java.time.Instant

class OADateTests {

    @Test
    fun fromOADateTest() {
        val expected = Instant.parse("2017-08-17T18:07:05.00Z")
        val doublePresent = 42964.754918981482
        val d = fromOADate(doublePresent)
        Assert.assertEquals(expected, d)
    }
}