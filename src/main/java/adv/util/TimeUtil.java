package adv.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

public class TimeUtil {
    public static final DateTimeFormatter FORMATTER_HHMM = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter FORMATTER_DDMMM_HHMM = DateTimeFormatter.ofPattern("dd.MMM HH:mm");
    public static final DateTimeFormatter FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMATTER_DDMM = new DateTimeFormatterBuilder().appendPattern("dd.MM")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter();

    public static String formatInstantToYear(Instant dateBegin) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(dateBegin);
    }

    public static LocalDate legacyDateToLocalDate(Date value) {
        return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
