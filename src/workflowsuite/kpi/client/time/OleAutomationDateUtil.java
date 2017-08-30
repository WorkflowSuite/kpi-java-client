package workflowsuite.kpi.client.time;

import java.time.Instant;

final class OleAutomationDateUtil {

    private OleAutomationDateUtil() {

    }

    private static final long MILLIS_PER_SECOND = 1000;
    private static final long MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;
    private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

    // Number of days in a non-leap year
    private static final long DAYS_PER_YEAR = 365;
    // Number of days in 4 years
    private static final long DAYS_PER_4_YEARS = DAYS_PER_YEAR * 4 + 1;       // 1461
    // Number of days in 100 years
    private static final long DAYS_PER_100_YEARS = DAYS_PER_4_YEARS * 25 - 1;  // 36524
    // Number of days in 400 years
    private static final long DAYS_PER_400_YEARS = DAYS_PER_100_YEARS * 4 + 1; // 146097

    private static final long DAYS_TO_1899 = DAYS_PER_400_YEARS * 4 + DAYS_PER_100_YEARS * 3 - 367;
    private static final long DAYS_TO_1970 =
            DAYS_PER_400_YEARS * 4 + DAYS_PER_100_YEARS * 3 + DAYS_PER_4_YEARS * 17 + DAYS_PER_YEAR; // 719,162

    static Instant fromOADate(double time) {
        //if (time >= 2958466 || time <= -657435)
        //throw
        long millis = (long) (time * MILLIS_PER_DAY + (time >= 0.0 ? 0.5 : -0.5));
        if (millis < 0) {
            millis -= (millis % MILLIS_PER_DAY) * 2;
        }

        millis += (DAYS_TO_1899 - DAYS_TO_1970) * MILLIS_PER_DAY;

        return Instant.ofEpochMilli(millis);
    }
}


