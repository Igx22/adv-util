package adv.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * todo переделать на такой шаблон
 * private final static DynamicBooleanProperty usePrimary = DynamicPropertyFactory.getInstance().getBooleanProperty("primarySecondary.usePrimary", true);
 */
public class SysPropUtils {
    private static final Logger log = LoggerFactory.getLogger(SysPropUtils.class);

    public static boolean getBooleanOrFail(String propertyName) {
        String val = System.getProperty(propertyName);
        final boolean result = Boolean.parseBoolean(val);
        log.debug("{}={}", propertyName, result);
        return result;
    }

    public static boolean getBoolean(String propertyName, boolean defaultValue) {
        String val = System.getProperty(propertyName);
        final boolean result = val == null ? defaultValue : Boolean.parseBoolean(val);
        log.debug("{}={}", propertyName, result);
        return result;
    }

    public static String getStringOrFail(String propertyName) {
        String val = System.getProperty(propertyName, null);
        Validate.notNull(val);
        log.debug("{}={}", propertyName, val);
        return val;
    }

    public static String getString(String propertyName, String defaultValue) {
        String val = System.getProperty(propertyName, defaultValue);
        log.debug("{}={}", propertyName, val);
        return val;
    }

    public static int getUnsignedInteger(String propertyName, int defaultValue) {
        String val = System.getProperty(propertyName);
        final int intValue;
        if (val == null) {
            intValue = defaultValue;
        } else {
            intValue = Integer.parseInt(val);
        }
        Validate.isTrue(intValue >= 0, String.format("property: %s should be > 0, " +
                "actual value: %s", propertyName, val));
        log.debug("{}={}", propertyName, intValue);
        return intValue;
    }

    public static long getUnsignedLong(String propertyName, long defaultValue) {
        String val = System.getProperty(propertyName);
        final long longVal;
        if (val == null) {
            longVal = defaultValue;
        } else {
            longVal = Long.parseLong(val);
        }
        Validate.isTrue(longVal >= 0, String.format("property: %s should be > 0, " +
                "actual value: %s", propertyName, val));
        log.debug("{}={}", propertyName, longVal);
        return longVal;
    }

    public static boolean isWindows() {
        return getStringOrFail("os.name").toLowerCase().startsWith("win");
    }

    public static String getEnv(String env) {
        String result = System.getenv(env);
        Validate.notNull(result, "Error getting parameter %s", env);
        return result;
    }

    public static String propertiesToString(Properties props) {
        StringBuilder sb = new StringBuilder(2000);
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
}
