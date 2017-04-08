package adv.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

/**
 * http://www.journaldev.com/2800/java-8-date-localdate-localdatetime-instant
 */
public class TimeUtil {
    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER_HHMM = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter FORMATTER_DDMMM_HHMM = DateTimeFormatter.ofPattern("dd.MMM HH:mm");
    public static final DateTimeFormatter FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMATTER_DDMM = new DateTimeFormatterBuilder().appendPattern("dd.MM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter();
    public static final Locale LOCALE_RU = Locale.forLanguageTag("ru-RU");
    public static final TemporalField weekOfYear = WeekFields.of(LOCALE_RU).weekOfWeekBasedYear();

    public static String formatInstantToYear(Instant dateBegin) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(dateBegin);
    }

    public static LocalDate legacyDateToLocalDate(Date value) {
        return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Недели нумеруются всегда с 1 даже если в начале года мы видим огрызок недели
    // это несколько отличается от РФ календаря в котором может быть 52 неделя а потом 1я
    // http://www.yp.ru/calendar/2017/
    // https://docs.oracle.com/javase/8/docs/api/java/time/temporal/WeekFields.html#weekOfWeekBasedYear-
    public static int getWeekOfYear(ZonedDateTime dateTime) {
        return dateTime.get(TimeUtil.weekOfYear);
    }

}
