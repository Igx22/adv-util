package adv.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 */
public class CharsetUtil {
    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Charset ASCII = StandardCharsets.US_ASCII;
    public static final Charset CP866 = Charset.forName("CP866");
    public static final Charset WINDOWS1251 = Charset.forName("WINDOWS-1251");
    public static final Charset WINDOWS1252 = Charset.forName("WINDOWS-1252");
}
