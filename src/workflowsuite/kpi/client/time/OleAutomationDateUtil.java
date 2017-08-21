package workflowsuite.kpi.client.time;

import java.time.Instant;

public final class OleAutomationDateUtil {

    private static final long MillisPerSecond = 1000;
    private static final long MillisPerMinute = MillisPerSecond * 60;
    private static final long MillisPerHour = MillisPerMinute * 60;
    private static final long MillisPerDay = MillisPerHour * 24;

    // Number of days in a non-leap year
    private static final long DaysPerYear = 365;
    // Number of days in 4 years
    private static final long DaysPer4Years = DaysPerYear * 4 + 1;       // 1461
    // Number of days in 100 years
    private static final long DaysPer100Years = DaysPer4Years * 25 - 1;  // 36524
    // Number of days in 400 years
    private static final long DaysPer400Years = DaysPer100Years * 4 + 1; // 146097

    private static final long DaysTo1899 = (DaysPer400Years * 4 + DaysPer100Years * 3 - 367);
    private static final long DaysTo1970 = (DaysPer400Years * 4 + DaysPer100Years * 3 + DaysPer4Years * 17 + DaysPerYear); // 719,162

    public static Instant fromOADate(double time){
        //if (time >= 2958466 || time <= -657435)
        //throw
        long millis = (long)(time * MillisPerDay + (time >= 0.0 ? 0.5 : -0.5));
        if (millis < 0)
            millis -= (millis % MillisPerDay) * 2;

        millis += (DaysTo1899 - DaysTo1970) * MillisPerDay;

        return Instant.ofEpochMilli(millis);

    }
}


