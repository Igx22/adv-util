package adv.util;

// этот класс не должен выделять память ни при каких условиях,
// вдохновлен org.apache.commons.lang3.Validate
public class Check {

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("The validated expression is false");
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, String message, int value1) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1));
        }
    }

    public static void isTrue(boolean expression, String message, long value1) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1));
        }
    }

    public static void isTrue(boolean expression, String message, double value1) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1));
        }
    }

    public static void isTrue(boolean expression, String message, Object value1) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1));
        }
    }

    public static void isTrue(boolean expression, String message, double value1, double value2) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1, value2));
        }
    }

    public static void isTrue(boolean expression, String message, Object value1, Object value2) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1, value2));
        }
    }

    public static void isTrue(boolean expression, String message, Object value1, Object value2, Object value3) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1, value2, value3));
        }
    }

    public static void isTrue(boolean expression, String message, Object value1, Object value2, Object value3, Object value4) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1, value2, value3, value4));
        }
    }

    public static void isTrue(boolean expression, String message, Object value1, Object value2, Object value3, Object value4, Object value5) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value1, value2, value3, value4, value5));
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("The validated object is null");
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    public static void notNull(Object obj, String message, Object value1) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1));
        }
    }

    public static void notNull(Object obj, String message, int value1) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1));
        }
    }

    public static void notNull(Object obj, String message, long value1) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1));
        }
    }

    public static void notNull(Object obj, String message, double value1) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1));
        }
    }

    public static void notNull(Object obj, String message, Object value1, Object value2) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1, value2));
        }
    }

    public static void notNull(Object obj, String message, Object value1, Object value2, Object value3) {
        if (obj == null) {
            throw new NullPointerException(String.format(message, value1, value2, value3));
        }
    }


}
