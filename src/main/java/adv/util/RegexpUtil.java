package adv.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Pattern.compile(".*/ghits/(\\d+)/.*").matcher("//www.adskeeper.co.uk/ghits/2656164/i/54461/2/src/123123/pp/1/1?h=EFg-o1").matches()
public class RegexpUtil {

    public static boolean findFirst(String regexp, String input) {
        if (regexp == null || input == null) {
            return false;
        }
        Matcher m = Pattern.compile(regexp).matcher(input);
        return m.find();
    }

    public static String findGroup1(String input, String regexp) {
        if (StringUtil.isEmpty(regexp)) {
            return "";
        }
        return findGroup(Pattern.compile(regexp), input, 1);
    }

    public static String findGroup(Pattern pattern, String input, int groupIndex) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        Matcher m = pattern.matcher(input);
        if (m.find() && m.groupCount() >= groupIndex) {
            return m.group(groupIndex);
        }
        return "";
    }
}
