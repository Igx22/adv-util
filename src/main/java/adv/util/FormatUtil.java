package adv.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtil {

    public static String formatDouble2Digits(double value) {
        return new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US)).format(value);
    }

    public static String formatDouble4Digits(double value) {
        return new DecimalFormat("0.0000", new DecimalFormatSymbols(Locale.US)).format(value);
    }

    public static String formatDouble5Digits(double value) {
        return new DecimalFormat("0.00000", new DecimalFormatSymbols(Locale.US)).format(value);
    }

    public static String formatDouble5DigitsSoft(double value) {
        return new DecimalFormat("0.#####", new DecimalFormatSymbols(Locale.US)).format(value);
    }
}
