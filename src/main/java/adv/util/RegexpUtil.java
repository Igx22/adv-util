package adv.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtil {

    public static boolean findFirst(String regexp, String input) {
        if (regexp == null || input == null) {
            return false;
        }
        Matcher m = Pattern.compile(regexp).matcher(input);
        return m.find();
    }
}
