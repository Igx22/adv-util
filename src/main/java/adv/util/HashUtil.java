package adv.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String md5(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte hash[] = md.digest();
            return BitUtil.toBase16(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    static long LargestPrime48 = 281474976710597L;


    // https://stackoverflow.com/questions/32912894/how-to-shorten-a-64-bit-hash-value-down-to-a-48-bit-value?noredirect=1&lq=1
    public static long hash48(String input) {
        byte[] data = input.getBytes(CharsetUtil.UTF8);
        long crc64 = Crc64.update(0, data, 0, data.length);
        long hash48 = crc64 % LargestPrime48;
        Check.isTrue((hash48 & 0xFFFFFF) == 0);
        return hash48;
    }
}
