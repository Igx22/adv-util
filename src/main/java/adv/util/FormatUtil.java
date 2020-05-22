package adv.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtil {
    private static ThreadLocal<DecimalFormat> d2 = new ThreadLocal<>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        }
    };

    private static ThreadLocal<DecimalFormat> d4 = new ThreadLocal<>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.0000", new DecimalFormatSymbols(Locale.US));
        }
    };

    private static ThreadLocal<DecimalFormat> d5 = new ThreadLocal<>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.00000", new DecimalFormatSymbols(Locale.US));
        }
    };

    private static ThreadLocal<DecimalFormat> d5s = new ThreadLocal<>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.#####", new DecimalFormatSymbols(Locale.US));
        }
    };

    private static ThreadLocal<DecimalFormat> d8s = new ThreadLocal<>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.########", new DecimalFormatSymbols(Locale.US));
        }
    };

    public static String formatDouble2Digits(double value) {
        return d2.get().format(value);
    }

    public static String formatDouble4Digits(double value) {
        return d4.get().format(value);
    }

    public static String formatDouble5Digits(double value) {
        return d5.get().format(value);
    }

    public static String formatDouble5DigitsSoft(double value) {
        return d5s.get().format(value);
    }

    public static String formatDouble8DigitsSoft(double value) {
        return d8s.get().format(value);
    }
}
