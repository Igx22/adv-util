package adv.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class JarUtil {

    public static String getJarPath(Class clazz) throws UnsupportedEncodingException {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path == null) {
            return null;
        }
        return URLDecoder.decode(path, "UTF-8");
    }
}
