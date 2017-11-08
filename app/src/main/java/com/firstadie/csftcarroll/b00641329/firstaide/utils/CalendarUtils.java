package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by tigh on 07/11/17.
 */

public class CalendarUtils {
    public static int calculateDifferenceInMinutes(long startTime, long endTime) {
        return (int) TimeUnit.MINUTES.convert(endTime - startTime, TimeUnit.MILLISECONDS);
    }

    public static long getDayBracketInMillis(boolean dayStart) {
        int hourOfDay = dayStart ? 0 : 23;
        int minute = dayStart ? 0 : 59;
        int second = dayStart ? 0 : 59;
        int millisecond = dayStart ? 0 : 9999;

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.SECOND, second);
        time.set(Calendar.MILLISECOND, millisecond);

        return time.getTimeInMillis();
    }
}
