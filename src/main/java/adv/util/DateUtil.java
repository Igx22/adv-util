package adv.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Форматирует дату в сокращенном виде.
 * // todo: переписать, это
 * <p/>
 * YYMMDD HHMMSS.SSS
 * 130613 153923.984
 *
 * see https://gist.github.com/mscharhag/9195718
 */
public class DateUtil {
    public static final int LENGTH = 17;
    public static final long SECOND_MS = 1000;
    public static final long MINUTE_MS = 60 * SECOND_MS;
    public static final long HOUR_MS = 60 * MINUTE_MS;
    public static final long DAY_MS = 24 * HOUR_MS;
    public static final long WEEK_MS = 7 * DAY_MS;

    public static String formatShort(final Calendar calendar) {
        return calendar == null ? "null" : formatShort(calendar.getTime());
    }

    public static String formatShort(final Date date) {
        return date == null ? "null" : formatShort(date.getTime());
    }

    public static String formatShort(final ZonedDateTime date) {
        return date == null ? "null" : formatShort(date.toInstant().toEpochMilli());
    }

    public static void formatShort(final StringBuilder output, final long timestamp) {
        formatShort(output, timestamp, true);
    }

    public static String formatShort(final LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        long timestamp = zonedDateTime.toInstant().toEpochMilli();
        return localDateTime == null ? "null" : formatShort(timestamp);
    }

    public static void formatShort(final StringBuilder output, final long timestamp, boolean useSpaces) {
        if (timestamp <= 0) {
            output.append("NONE");
            return;
        }
        final ZonedDateTime dt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        output.append(fill2(dt.getYear() % 100))
                .append(fill2(dt.getMonthValue()))
                .append(fill2(dt.getDayOfMonth()))
                .append(useSpaces ? ' ' : "_")
                .append(fill2(dt.getHour()))
                .append(fill2(dt.getMinute()))
                .append(fill2(dt.getSecond()))
                .append('.')
                .append(fill3(dt.get(ChronoField.MILLI_OF_SECOND)));
    }

    public static String formatShortNoSpaces(final long timestamp) {
        StringBuilder buf = new StringBuilder(LENGTH);
        formatShort(buf, timestamp, false);
        return buf.toString();
    }

    public static String formatShort(final long timestamp) {
        StringBuilder buf = new StringBuilder(LENGTH);
        formatShort(buf, timestamp, true);
        return buf.toString();
    }

    private static String fill2(int value) {
        String text = Integer.toString(value);
        if (text.length() >= 2) {
            return text;
        } else {
            return "00".substring(text.length()) + text;
        }
    }

    private static String fill3(int value) {
        String text = Integer.toString(value);
        if (text.length() >= 3) {
            return text;
        } else {
            return "000".substring(text.length()) + text;
        }
    }

    // true = еще не наступил timeout задаваемый allowedDelta
    // между моментами времени @checkedTime(проверяемое) и @currentTime(сейчас)
    public static boolean checkNoTimeout(long currentTime, long checkedTime, long allowedDeltaWCurrent, TimeUnit deltaTimeUnit) {
        return currentTime - checkedTime < deltaTimeUnit.toMillis(allowedDeltaWCurrent);
    }


    public static boolean checkNoTimeout(long checkedTime, long allowedDeltaWCurrent, TimeUnit deltaTimeUnit) {
        return checkNoTimeout(System.currentTimeMillis(), checkedTime, allowedDeltaWCurrent, deltaTimeUnit);
    }

    // true = еще не наступил timeout задаваемый allowedDelta
    public static boolean checkNoTimeout(long checkedTime, long allowedDeltaWCurrent) {
        return checkNoTimeout(System.currentTimeMillis(), checkedTime, allowedDeltaWCurrent, TimeUnit.MILLISECONDS);
    }

    public static Calendar dateAsCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

}
