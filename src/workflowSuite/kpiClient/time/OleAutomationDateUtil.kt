package workflowSuite.kpiClient.time

import java.nio.ByteBuffer
import java.time.Instant
import java.util.*

// Number of 100ns ticks per time unit
private const val TicksPerMillisecond: Long = 10000
private const val TicksPerSecond = TicksPerMillisecond * 1000
private const val TicksPerMinute = TicksPerSecond * 60
private const val TicksPerHour = TicksPerMinute * 60
private const val TicksPerDay = TicksPerHour * 24

private const val MillisPerSecond = 1000
private const val MillisPerMinute = MillisPerSecond * 60
private const val MillisPerHour = MillisPerMinute * 60
private const val MillisPerDay = MillisPerHour * 24

// Number of days in a non-leap year
private const val DaysPerYear = 365
// Number of days in 4 years
private const val DaysPer4Years = DaysPerYear * 4 + 1       // 1461
// Number of days in 100 years
private const val DaysPer100Years = DaysPer4Years * 25 - 1  // 36524
// Number of days in 400 years
private const val DaysPer400Years = DaysPer100Years * 4 + 1 // 146097

private const val DaysTo1899 = DaysPer400Years * 4 + DaysPer100Years * 3 - 367
private const val DoubleDateOffset = DaysTo1899 * TicksPerDay

fun fromOADate(bytes: ByteArray, startIndex: Int): Instant {
    val d = ByteBuffer.wrap(bytes, startIndex, 8).getDouble()
    return fromOADate(d)
}

fun fromOADate(time: Double): Instant {
    //if (time >= 2958466 || time <= -657435)
    //throw
    var millis = (time * MillisPerDay + if (time >= 0.0) 0.5 else -0.5).toLong()
    if (millis < 0)
        millis -= (millis % MillisPerDay) * 2
    millis += DoubleDateOffset / TicksPerMillisecond

    return Date(millis * TicksPerMillisecond).toInstant()

}