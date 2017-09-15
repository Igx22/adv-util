package adv.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtil {

    public static String formatDouble2Digits(double value) {
        return new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US)).format(value);
    }
}
