package adv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;


public class StringUtil {
    private static final int COORDINATES_DISPLAY_PRECISION = 6;

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    public static boolean isEmptyOrSpaces(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static byte[] toUtfBytes(String str) {
        return stringToArray(str);
    }

    public static String toUtf(byte[] data) {
        return arrayToString(data);
    }

    public static String toASCII(byte[] data) {
        return new String(data, CharsetUtil.ASCII);
    }

    public static String arrayToString(byte[] data) {
        if (data == null) {
            return null;
        }
        return new String(data, CharsetUtil.UTF8);
    }

    public static byte[] stringToArray(String strValue) {
        return strValue.getBytes(CharsetUtil.UTF8);
    }

    public static String removeNonWordChars(String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        return value.replaceAll("[^\\w\\-_\\.]", "");
    }

    public static String toOneLine(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\n", "");
    }

    public static String toWinString(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("(?<!\\r)\\n", "\r\n");
    }

    public static String readInputStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //ignored
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignored
                }
            }
        }
        return sb.toString();
    }

    public static String toHumanReadable(long bytes) {
        final int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("kMGTPE").charAt(exp - 1) + ("");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // экспериментальный zero-copy конвертер StringBuilder в
    public static byte[] getBytesUtf8Fast(StringBuilder sb) {
        try {
            return CharsetUtil.UTF8.newEncoder().encode(CharBuffer.wrap(sb)).array();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] getBytesUtf8Slow(StringBuilder sb) {
        return sb.toString().getBytes(CharsetUtil.UTF8);
    }

    public static String appendIfNotEmpty(String value, String suffix) {
        return StringUtil.isEmpty(value) ? value : value + suffix;
    }

    private static String coordinates(BigDecimal latitude, BigDecimal longitude) {
        StringBuilder builder = new StringBuilder();
        builder.append(latitude.signum() >= 0 ? "N" : "S");
        builder.append(latitude.abs().setScale(COORDINATES_DISPLAY_PRECISION, BigDecimal.ROUND_HALF_EVEN));
        builder.append(" ");
        builder.append(longitude.signum() >= 0 ? "E" : "W");
        builder.append(longitude.abs().setScale(COORDINATES_DISPLAY_PRECISION, BigDecimal.ROUND_HALF_EVEN));
        return builder.toString();
    }

    public static String coordinates(double latitude, double longitude) {
        return coordinates(new BigDecimal(latitude), new BigDecimal(longitude));
    }

    public static String toStringNullSafe(Object obj) {
        return obj == null ? "null" : obj.toString();
    }

    public static String toUtfStringSafe(byte[] data) {
        try {
            if (data == null || data.length == 0) {
                return "";
            }
            CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
            utf8Decoder.onMalformedInput(CodingErrorAction.REPLACE);
            utf8Decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            utf8Decoder.replaceWith("?");
            final CharBuffer cb = utf8Decoder.decode((ByteBuffer.wrap(data)));
            final StringBuilder sb = new StringBuilder(cb);
            return sb.toString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}


