package adv.util;

/**
 * todo можно поддержать дробное кол-во байт
 */
public class SizeUtil {
    public static final int KB_BYTES = 1024;
    public static final int MB_BYTES = KB_BYTES * 1024;
    public static final int GB_BYTES = MB_BYTES * 1024;

    public static long parseSize(String text) {
        text = text.toLowerCase();
        if (text.endsWith("k")) {
            return Long.parseLong(text.substring(0, text.length() - "K".length())) * KB_BYTES;
        } else if (text.endsWith("kb")) {
            return Long.parseLong(text.substring(0, text.length() - "kb".length())) * KB_BYTES;
        } else if (text.endsWith("kib")) {
            return Long.parseLong(text.substring(0, text.length() - "kib".length())) * KB_BYTES;
        } else if (text.endsWith("m")) {
            return Long.parseLong(text.substring(0, text.length() - "m".length())) * MB_BYTES;
        } else if (text.endsWith("mb")) {
            return Long.parseLong(text.substring(0, text.length() - "mb".length())) * MB_BYTES;
        } else if (text.endsWith("mib")) {
            return Long.parseLong(text.substring(0, text.length() - "mib".length())) * MB_BYTES;
        } else if (text.endsWith("g")) {
            return Long.parseLong(text.substring(0, text.length() - "g".length())) * GB_BYTES;
        } else if (text.endsWith("gb")) {
            return Long.parseLong(text.substring(0, text.length() - "gb".length())) * GB_BYTES;
        } else if (text.endsWith("gib")) {
            return Long.parseLong(text.substring(0, text.length() - "gib".length())) * GB_BYTES;
        }
        return Long.parseLong(text);
    }
}
