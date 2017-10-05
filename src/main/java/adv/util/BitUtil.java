package adv.util;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class BitUtil {
    public static final long INT8_MAX = Byte.MAX_VALUE;
    public static final long INT16_MAX = Short.MAX_VALUE;
    public static final long INT32_MAX = Integer.MAX_VALUE;
    public static final long INT64_MAX = Long.MAX_VALUE;
    public static final long UINT8_MAX = 0xFFL;
    public static final long UINT16_MAX = 0xFFFFL;
    public static final long UINT32_MAX = 0xFFFFFFFFL;
    public static final long UINT64_MAX = 0xFFFFFFFFFFFFFFFFL;
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toBase16(byte[] data) {
        if (data == null) {
            return "";
        }
        return toBase16(data, 0, data.length);
    }

    public static byte fromBCD(byte arg) {
        return (byte) (((arg & 0xF0) >> 4) * 10 + (arg & 0x0F));
    }

    public static byte toBCD(byte arg) {
        return (byte) (((arg / 10) << 4) | (arg % 10));
    }

    public static String toBase16(Byte value) {
        if (value == null) {
            return "";
        }
        return toBase16(value.byteValue());
    }

    public static String toBase16Separate(byte[] data, String separator) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(toBase16(b));
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    public static String toBase16(byte value) {
        return toBase16(value & 0xFF, 1);
    }

    public static String toBase16(short value) {
        return toBase16(value & 0xFFFF, 2);
    }

    public static String toBase16(int value) {
        return toBase16((int) value, 4);
    }

    public static String toBase16(long value) {
        return toBase16(value, 8);
    }

    public static String toBase16(int value, int byteCount) {
        final char[] hexChars = new char[byteCount * 2];
        int v;
        for (int j = 0; j < byteCount; j++) {
            int shiftSizeToGetByte = 8 * (byteCount - 1 - j);
            v = ((byte) (value >> shiftSizeToGetByte)) & 0xff; // get byte at pos [j]
            hexChars[j * 2] = HEX[v / 16];
            hexChars[j * 2 + 1] = HEX[v % 16];
        }
        return new String(hexChars);
    }

    public static String toBase16(long value, int byteCount) {
        final char[] hexChars = new char[byteCount * 2];
        int v;
        for (int j = 0; j < byteCount; j++) {
            int shiftSizeToGetByte = 8 * (byteCount - 1 - j);
            v = ((byte) (value >> shiftSizeToGetByte)) & 0xff; // get byte at pos [j]
            hexChars[j * 2] = HEX[v / 16];
            hexChars[j * 2 + 1] = HEX[v % 16];
        }
        return new String(hexChars);
    }

    public static String toBase16(byte[] data, int offset, int size) {
        if (data == null || data.length == 0 || size <= 0 || offset < 0) {
            return "";
        }
        if (offset >= size || size + offset > data.length) {
            throw new IndexOutOfBoundsException("" + offset + "," + size + "," + data.length);
        }
        final char[] hexChars = new char[size * 2];
        int v;
        for (int j = offset; j < size; j++) {
            v = data[j] & 0xFF;
            hexChars[j * 2] = HEX[v / 16];
            hexChars[j * 2 + 1] = HEX[v % 16];
        }
        return new String(hexChars);
    }

    public static byte toByte(int value) {
        return (byte) (value & 0xFF);
    }

    public static int toIntUnsigned(byte value) {
        return value & 0xFF;
    }

    public static long toLongUnsigned(int value) {
        return value & 0xFFFFFFFFL;
    }


    public static short toShort(int value) {
        return (short) (value & 0xFFFF);
    }

    public static int toInt(long value) {
        return (int) (value);
    }

    public static byte toByte(long value) {
        return (byte) (value & 0xFF);
    }

    public static byte base16ToByte(String s) {
        Validate.notNull(s);
        Validate.isTrue(s.length() == 2);
        return (byte) ((Character.digit(s.charAt(0), 16) << 4) + Character.digit(s.charAt(1), 16));
    }

    public static byte[] base16ToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int high = Character.digit(s.charAt(i), 16);
            int low = Character.digit(s.charAt(i + 1), 16);
            if (high == -1 || low == -1) {
                throw new IllegalArgumentException("String '" + s + "' is not valid Base16 value, wrong chars: " + s.charAt(i) + s.charAt(i + 1));
            }
            data[i / 2] = (byte) ((high << 4) + low);
        }
        return data;
    }

    public static boolean isBase16String(String base16StringToCheck, int expectedLength) {
        int len = base16StringToCheck.length();
        if ((len % 2) != 0) {
            return false;
        }
        if (expectedLength > 0) {
            if (len != expectedLength) {
                return false;
            }
        }
        for (int i = 0; i < len; i += 2) {
            int high = Character.digit(base16StringToCheck.charAt(i), 16);
            int low = Character.digit(base16StringToCheck.charAt(i + 1), 16);
            if (high == -1 || low == -1) {
                return false;
            }
        }
        return true;
    }

    // это имя кажется более удобным
    public static byte[] toBytes(String base16String) {
        return base16ToBytes(base16String);
    }

    // это имя кажется более удобным
    public static byte[] toArray(String base16String) {
        return base16ToBytes(base16String);
    }

    public static long base16ToLong(final String valueInUnsignedHex) {
        if (StringUtil.isEmptyOrSpaces(valueInUnsignedHex)) {
            throw new IllegalStateException();
        }
        long value = 0;
        final int hexLength = valueInUnsignedHex.length();
        if (hexLength == 0) {
            throw new NumberFormatException("For input string: \"\"");
        }
        for (int i = Math.max(0, hexLength - 16); i < hexLength; i++) {
            final char ch = valueInUnsignedHex.charAt(i);

            if (ch >= '0' && ch <= '9') {
                value = (value << 4) | (ch - '0');
            } else if (ch >= 'A' && ch <= 'F') {
                value = (value << 4) | (ch - ('A' - 0xaL));
            } else if (ch >= 'a' && ch <= 'f') {
                value = (value << 4) | (ch - ('a' - 0xaL));
            } else {
                throw new NumberFormatException("For input string: \"" + valueInUnsignedHex + "\"");
            }
        }
        return value;
    }

    /**
     * Finds the first occurrence of the pattern in the text.
     */
    public static int searchBytes(byte[] data, byte[] pattern) {
        int[] failure = computeFailure(pattern);
        int j = 0;
        if (data.length == 0) {
            return -1;
        }
        for (int i = 0; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];
        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }
        return failure;
    }

    public static String getBits32(int value) {
        int displayMask = 1 << 31;
        StringBuilder buf = new StringBuilder(35);

        for (int c = 1; c <= 32; c++) {
            buf.append((value & displayMask) == 0 ? '0' : '1');
            value <<= 1;

            if (c % 8 == 0)
                buf.append(' ');
        }

        return buf.toString();
    }

    public static String toBitString(int value) {
        return toBitString(value, 4);
    }

    public static String toBitString(int value, int byteCount) {
        Validate.isTrue(byteCount >= 0 && byteCount <= 4);
        value = value << ((4 - byteCount) * 8);
        int displayMask = 1 << 31;
        StringBuilder buf = new StringBuilder(byteCount * 8 + 4);
        for (int c = 1; c <= byteCount * 8; c++) {
            if (c > 1 && (c % 8) == 1) {
                buf.append(' ');
            }
            buf.append((value & displayMask) == 0 ? '0' : '1');
            value <<= 1;
        }
        return buf.toString();
    }

    /**
     * Reverses bit order in the byte. More more information see Henry S.Warren "Hacker's Delight"
     *
     * @param value byte to be reversed
     * @return Reversed value of input byte. E.g. if input was <code>0xAC (10101100b)</code> then output will be
     * <code>0x35 (00110101b)</code>
     */
    public static byte reverse(byte value) {
        value = (byte) ((value & 0x55) << 1 | (value & 0xAA) >> 1);
        value = (byte) ((value & 0x33) << 2 | (value & 0xCC) >> 2);
        value = (byte) ((value & 0x0F) << 4 | (value & 0xF0) >> 4);

        return value;
    }

    /**
     * Reverses bit order in the short. More more information see Henry S.Warren "Hacker's Delight"
     *
     * @param value short to be reversed
     * @return Reversed value of input short. E.g. if input was <code>0x62AE
     * (0110001010101110b)</code> then output will be <code>0x7546
     * (0111010101000110b)</code>
     */
    public static short reverse(short value) {
        value = (short) ((value & 0x5555) << 1 | (value & 0xAAAA) >> 1);
        value = (short) ((value & 0x3333) << 2 | (value & 0xCCCC) >> 2);
        value = (short) ((value & 0x0F0F) << 4 | (value & 0xF0F0) >> 4);
        value = (short) ((value & 0x00FF) << 8 | (value & 0xFF00) >> 8);

        return value;
    }

    /**
     * Reverses bit order in the int. More more information see Henry S.Warren "Hacker's Delight"
     *
     * @param value int to be reversed
     * @return Reversed value of input int. E.g. if input was <code>0x000062AE
     * (00000000000000000110001010101110b)</code> then output will be <code>0x75460000
     * (01110101010001100000000000000000b)</code>
     */
    public static int reverse(int value) {
        value = (value & 0x55555555) << 1 | (value & 0xAAAAAAAA) >>> 1;
        value = (value & 0x33333333) << 2 | (value & 0xCCCCCCCC) >>> 2;
        value = (value & 0x0F0F0F0F) << 4 | (value & 0xF0F0F0F0) >>> 4;
        value = (value & 0x00FF00FF) << 8 | (value & 0xFF00FF00) >>> 8;
        value = (value & 0x0000FFFF) << 16 | (value & 0xFFFF0000) >>> 16;

        return value;
    }

    public static short swapBytes(short value) {
        value = (short) ((value & 0x00FF) << 8 | (value & 0xFF00) >> 8);
        return value;
    }

    public static int swapWords(int value) {
        return ((value & 0xFFFF) << 16) | ((value & 0xFFFF0000) >>> 16);
    }

    public static <T> T[] concatAll(T[]... rest) {
        T[] first = rest[0];
        int totalLength = first.length;
        for (int i = 1; i < rest.length; i++) {
            totalLength += rest[i].length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (int i = 1; i < rest.length; i++) {
            T[] array = rest[i];
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] concatBytes(byte[]... rest) {
        byte[] first = rest[0];
        int totalLength = first.length;
        for (int i = 1; i < rest.length; i++) {
            totalLength += rest[i].length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (int i = 1; i < rest.length; i++) {
            byte[] array = rest[i];
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] concatBytes(List<byte[]> rest) {
        byte[] first = rest.get(0);
        int totalLength = first.length;
        for (int i = 1; i < rest.size(); i++) {
            totalLength += rest.get(i).length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (int i = 1; i < rest.size(); i++) {
            byte[] array = rest.get(i);
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static int setIntFlag(int flags, int bitMask, boolean value) {
        if (value) {
            flags |= bitMask;
        } else {
            flags &= ~bitMask;
        }
        return flags;
    }

    public static boolean getIntFlag(int flags, int bitMask) {
        return (flags & bitMask) == bitMask;
    }

    public static byte getByteWithOffset(int value, int zeroBasedByteOffset) {
        return (byte) ((value >>> (8 * zeroBasedByteOffset)) & 0xFF);
    }

    public static int setByteWithOffset(int value, int byteValue, int zeroBasedByteOffset) {
        return value | ((byteValue & 0xFF) << (8 * zeroBasedByteOffset));
    }


    public static byte[] writeBEInt(int value) {
        int size = 4;
        byte[] result = new byte[size];
        writeBEInt(result, value);
        return result;
    }

    public static int readBEInt(byte[] data) {
        if (data == null || data.length == 0 || data.length != 4) {
            throw new IllegalStateException("no data");
        }
        return readBEInt(data, 0);
    }

    public static long readBELong(byte[] data) {
        return readBELong(data, 0);
    }

    public static long readBELong(byte[] data, int offset) {
        long result = 0;
        result |= ((long) (data[offset++] & 0xFF)) << 56;
        result |= ((long) (data[offset++] & 0xFF)) << 48;
        result |= ((long) (data[offset++] & 0xFF)) << 40;
        result |= ((long) (data[offset++] & 0xFF)) << 32;
        result |= ((long) (data[offset++] & 0xFF)) << 24;
        result |= ((long) (data[offset++] & 0xFF)) << 16;
        result |= ((long) (data[offset++] & 0xFF)) << 8;
        result |= ((long) (data[offset] & 0xFF));

        return result;
    }

    @SuppressWarnings("UnusedAssignment")
    public static byte[] writeBELong(long value) {
        int size = 8;
        byte[] out = new byte[size];
        int offset = 0;
        out[offset++] = (byte) ((int) ((value >>> 56) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 48) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 40) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 32) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 24) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 16) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 8) & 0xFF));
        out[offset++] = (byte) ((int) (value & 0xFF));
        return out;
    }

    public static long readBENumberUnsigned(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalStateException("no data");
        }
        long result = 0;
        for (byte aData : data) {
            result = (result << 8) + (aData & 0xff);
        }
        return result;
    }

    public static boolean startsWith(byte[] data, byte[] headerToCheck) {
        if (data.length < headerToCheck.length) {
            return false;
        }
        for (int i = 0; i < headerToCheck.length; i++) {
            if (headerToCheck[i] != data[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] skipBytes(byte[] src, int countToRemove) {
        if (countToRemove > src.length) {
            throw new IllegalStateException("invalid offset: " + countToRemove);
        }
        if (countToRemove == 0) {
            return src;
        }
        int newLen = src.length - countToRemove;
        byte[] result = new byte[newLen];
        System.arraycopy(src, countToRemove, result, 0, newLen);
        return result;
    }

    public static byte getByte1(int crcValue) {
        return (byte) ((crcValue >> 8) & 0xff);
    }

    public static byte getByte0(int crcValue) {
        return (byte) (crcValue & 0xff);
    }

    public static void fillRandomBytes(Random rnd, byte[] packet, int start, int size) {
        for (int pos = start; pos < size; pos++) {
            packet[pos] = (byte) (rnd.nextInt(256) & 0xFF);
        }
    }

    public static byte[] getRandomBytes(int size) {
        byte[] result = new byte[size];
        Random rnd = new Random();
        BitUtil.fillRandomBytes(rnd, result, 0, result.length);
        return result;
    }

    public static int getByteUnsigned(byte value) {
        return value & 0xFF;
    }

    /**
     * Быстрая проверка на то является ли данная строка целым числом
     *
     * @param value              входное значение
     * @param expectedDigitCount сколько цифр в числе максимально, -1 - не проверять
     * @param allowNegative      позволяем вводить отрицательные числа
     * @return true, если текст это число и он прошел проверку
     */
    public static boolean isInteger(String value, int expectedDigitCount, boolean allowNegative) {
        if (value == null) {
            return false;
        }
        int length = value.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (value.charAt(0) == '-') {
            if (!allowNegative) {
                return false;
            }
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        int digitCount = 0;
        for (; i < length; i++) {
            digitCount++;
            char c = value.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return expectedDigitCount < 0 || digitCount == expectedDigitCount;
    }

    public static boolean isBitOn(byte flags, int zeroBasedBitOffset) {
        Validate.isTrue(zeroBasedBitOffset >= 0 && zeroBasedBitOffset <= 7);
        return (((flags >> zeroBasedBitOffset) & 0x01) == 1);
    }

    public static boolean isBitOn(int flags, int zeroBasedBitOffset) {
        Validate.isTrue(zeroBasedBitOffset >= 0 && zeroBasedBitOffset <= 31);
        return (((flags >> zeroBasedBitOffset) & 0x01) == 1);
    }

    public static boolean isMatchesAnyBitmask(int flags, @NotNull int... bitmasks) {
        Validate.isTrue(bitmasks != null && bitmasks.length > 0);
        for (int bitmask : bitmasks) {
            Validate.isTrue(bitmask != 0, "bitmask cannot be 0");
            if ((flags & bitmask) != 0) {
                return true;
            }
        }
        return false;
    }

    public static byte setBitOn(byte flags, int zeroBasedBitOffset) {
        Validate.isTrue(zeroBasedBitOffset >= 0 && zeroBasedBitOffset <= 7);
        flags |= (1 << zeroBasedBitOffset);
        return flags;
    }

    public static int setBitOn(int flags, int zeroBasedBitOffset) {
        Validate.isTrue(zeroBasedBitOffset >= 0 && zeroBasedBitOffset <= 31);
        flags |= (1 << zeroBasedBitOffset);
        return flags;
    }

    public static void setByteBit(byte[] bytes, int byteToSet, int bitNum, boolean setBit) {
        if (setBit) {
            setBitOn(bytes, byteToSet, bitNum);
        } else {
            setBitOff(bytes, byteToSet, bitNum);
        }
    }

    public static void setBitOn(byte[] bytes, int byteToSet, int bitNum) {
        bytes[byteToSet] = (byte) (bytes[byteToSet] | (1 << bitNum));
    }

    public static byte[] subarray(byte[] command, int startIndex, int endIndex) {
        byte[] result = new byte[endIndex - startIndex];

        System.arraycopy(command, startIndex, result, 0, endIndex - startIndex);

        return result;
    }

    public static void setBitOff(byte[] bytes, int byteToSet, int bitNum) {
        bytes[byteToSet] = (byte) (bytes[byteToSet] & ~(1 << bitNum));
    }

    public static boolean isHexSymbol(char symbol) {
        return (symbol >= 0x30 && symbol <= 0x39)
                || (symbol >= 'a' && symbol <= 'f')
                || (symbol >= 'A' && symbol <= 'F');
    }

    public static void writeBEShort(ByteArrayOutputStream out, int value) {
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }

    public static int writeBEShort(byte[] out, int value) {
        return writeBEShort(out, 0, value);
    }

    public static int writeBEShort(byte[] out, int offset, int value) {
        out[offset++] = (byte) ((value >>> 8) & 0xFF);
        out[offset++] = (byte) (value & 0xFF);
        return offset;
    }

    public static void writeLEShort(ByteArrayOutputStream out, int value) {
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
    }

    public static int writeLEShort(byte[] out, int value) {
        return writeLEShort(out, 0, value);
    }

    public static int writeLEShort(byte[] out, int offset, int value) {
        out[offset++] = (byte) (value & 0xFF);
        out[offset++] = (byte) ((value >>> 8) & 0xFF);
        return offset;
    }

    public static void writeBEInt(ByteArrayOutputStream out, int value) {
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }

    public static int writeBEInt(byte[] out, int value) {
        return writeBEInt(out, 0, value);
    }

    public static int writeBEInt(byte[] out, int offset, int value) {
        out[offset++] = (byte) ((value >>> 24) & 0xFF);
        out[offset++] = (byte) ((value >>> 16) & 0xFF);
        out[offset++] = (byte) ((value >>> 8) & 0xFF);
        out[offset++] = (byte) (value & 0xFF);
        return offset;
    }

    public static void writeLEInt(ByteArrayOutputStream out, int value) {
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 24) & 0xFF);
    }

    public static int writeLEInt(byte[] out, int value) {
        return writeLEInt(out, 0, value);
    }

    public static int writeLEInt(byte[] out, int offset, int value) {
        out[offset++] = (byte) (value & 0xFF);
        out[offset++] = (byte) ((value >>> 8) & 0xFF);
        out[offset++] = (byte) ((value >>> 16) & 0xFF);
        out[offset++] = (byte) ((value >>> 24) & 0xFF);
        return offset;
    }

    public static void writeBELong(ByteArrayOutputStream out, long value) {
        out.write((int) ((value >>> 56) & 0xFF));
        out.write((int) ((value >>> 48) & 0xFF));
        out.write((int) ((value >>> 40) & 0xFF));
        out.write((int) ((value >>> 32) & 0xFF));
        out.write((int) ((value >>> 24) & 0xFF));
        out.write((int) ((value >>> 16) & 0xFF));
        out.write((int) ((value >>> 8) & 0xFF));
        out.write((int) (value & 0xFF));
    }

    public static int writeBELong(byte[] out, long value) {
        return writeBELong(out, 0, value);
    }

    public static int writeBELong(byte[] out, int offset, long value) {
        out[offset++] = (byte) ((int) ((value >>> 56) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 48) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 40) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 32) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 24) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 16) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 8) & 0xFF));
        out[offset++] = (byte) ((int) (value & 0xFF));
        return offset;
    }

    public static void writeLELong(ByteArrayOutputStream out, long value) {
        out.write((int) (value & 0xFF));
        out.write((int) ((value >>> 8) & 0xFF));
        out.write((int) ((value >>> 16) & 0xFF));
        out.write((int) ((value >>> 24) & 0xFF));
        out.write((int) ((value >>> 32) & 0xFF));
        out.write((int) ((value >>> 40) & 0xFF));
        out.write((int) ((value >>> 48) & 0xFF));
        out.write((int) ((value >>> 56) & 0xFF));
    }

    public static int writeLELong(byte[] out, long value) {
        return writeLELong(out, 0, value);
    }

    public static int writeLELong(byte[] out, int offset, long value) {
        out[offset++] = (byte) ((int) (value & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 8) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 16) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 24) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 32) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 40) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 48) & 0xFF));
        out[offset++] = (byte) ((int) ((value >>> 56) & 0xFF));
        return offset;
    }

    public static int readBEShort(byte[] data) {
        return readBEShort(data, 0);
    }

    public static int readBEShort(ByteArrayInputStream input) {
        int result = 0;
        for (int i = 0; i <= 1; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= (value & 0xFF) << ((1 - i) * 8);
        }

        return result;
    }

    public static int readBEShort(byte[] data, int offset) {
        int result = 0;
        result |= (data[offset++] & 0xFF) << 8;
        result |= (data[offset] & 0xFF);

        return result;
    }

    public static int readLEShort(byte[] data) {
        return readLEShort(data, 0);
    }

    public static int readLEShort(ByteArrayInputStream input) {
        int result = 0;
        for (int i = 0; i <= 1; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= (value & 0xFF) << (i * 8);
        }

        return result;
    }

    public static int readLEShort(byte[] data, int offset) {
        int result = 0;
        result |= (data[offset++] & 0xFF);
        result |= (data[offset] & 0xFF) << 8;
        return result;
    }

    public static int readBEInt(ByteArrayInputStream input) {
        int result = 0;
        for (int i = 0; i <= 3; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= (value & 0xFF) << ((3 - i) * 8);
        }

        return result;
    }

    public static int readBEInt(byte[] data, int offset) {
        int result = 0;
        result |= (data[offset++] & 0xFF) << 24;
        result |= (data[offset++] & 0xFF) << 16;
        result |= (data[offset++] & 0xFF) << 8;
        result |= (data[offset] & 0xFF);

        return result;
    }

    public static int readLEInt(byte[] data) {
        return readLEInt(data, 0);
    }

    public static int readLEInt(ByteArrayInputStream input) {
        int result = 0;
        for (int i = 0; i <= 3; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= (value & 0xFF) << (i * 8);
        }

        return result;
    }

    public static long readBELong(ByteArrayInputStream input) {
        long result = 0;
        for (int i = 0; i <= 7; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= ((long) (value & 0xFF)) << ((7 - i) * 8);
        }

        return result;
    }

    public static long readLELong(byte[] data) {
        return readLELong(data, 0);
    }

    public static long readLELong(ByteArrayInputStream input) {
        long result = 0;
        for (int i = 0; i <= 7; i++) {
            int value = input.read();
            if (value == -1) {
                throw new IllegalStateException("Unexpected end of stream");
            }
            result |= ((long) (value & 0xFF)) << (i * 8);
        }

        return result;
    }

    public static long readLELong(byte[] data, int offset) {
        long result = 0;
        result |= ((long) (data[offset++] & 0xFF));
        result |= ((long) (data[offset++] & 0xFF)) << 8;
        result |= ((long) (data[offset++] & 0xFF)) << 16;
        result |= ((long) (data[offset++] & 0xFF)) << 24;
        result |= ((long) (data[offset++] & 0xFF)) << 32;
        result |= ((long) (data[offset++] & 0xFF)) << 40;
        result |= ((long) (data[offset++] & 0xFF)) << 48;
        result |= ((long) (data[offset] & 0xFF)) << 56;

        return result;
    }

    public static void writeString(ByteArrayOutputStream target, String str) {
        if (str == null) {
            target.write(0);
            return;
        }

        byte[] bytes = str.getBytes();
        target.write(bytes.length);
        target.write(bytes, 0, bytes.length);
    }

    public static List<byte[]> splitBySize(byte[] bytes, int size) {
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; ) {
            int remainder = Math.min(bytes.length - i, size);
            byte[] piece = new byte[remainder];

            System.arraycopy(bytes, i, piece, 0, remainder);
            result.add(piece);

            i += remainder;
        }
        return result;
    }

    public static byte[] shortToByteArray(short value) {
        return new byte[]{
                (byte) value,
                (byte) (value >>> 8)
        };
    }

    public static byte[] floatToByteArray(float value) {
        return intToByteArray(Float.floatToIntBits(value));
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) value,
                (byte) (value >>> 8),
                (byte) (value >>> 16),
                (byte) (value >>> 24)
        };
    }

    public static byte[] doubleToByteArray(double value) {
        return longToByteArray(Double.doubleToLongBits(value));
    }

    public static byte[] longToByteArray(long value) {
        return new byte[]{
                (byte) value,
                (byte) (value >>> 8),
                (byte) (value >>> 16),
                (byte) (value >>> 24),
                (byte) (value >>> 32),
                (byte) (value >>> 40),
                (byte) (value >>> 48),
                (byte) (value >>> 56)
        };
    }

    public static int toBCD(int a) { //Binary Coded Decimal
        return ((a / 10) << 4) | (a % 10);
    }

    public static int fromBCD(int a) {
        return ((a >> 4) * 10) + (a & 0x0f);
    }


    public static int networkByteOrderToInt(byte[] buf, int start, int count) {
        if (count > 4) {
            throw new IllegalArgumentException(
                    "Cannot handle more than 4 bytes");
        }

        int result = 0;

        for (int i = 0; i < count; i++) {
            result <<= 8;
            result |= (buf[start + i] & 0xff);
        }

        return result;
    }

    public static int readLEInt(byte[] data, int offset) {
        int result = 0;
        result |= (data[offset++] & 0xFF);
        result |= (data[offset++] & 0xFF) << 8;
        result |= (data[offset++] & 0xFF) << 16;
        result |= (data[offset] & 0xFF) << 24;

        return result;
    }

    public static int readLE3BytesInt(byte[] data, int offset) {
        int result = 0;
        result |= (data[offset++] & 0xFF);
        result |= (data[offset++] & 0xFF) << 8;
        result |= (data[offset++] & 0xFF) << 16;

        return result;
    }

    public static void bytesRev(byte[] bytes) {
        if (bytes.length < 2) {
            return;
        }

        int center = bytes.length / 2;
        for (int i = 0; i < center; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
    }


    public static Calendar fromPulsarTime(byte[] time) {
        return new GregorianCalendar(time[0] + 2000, time[1] - 1, time[2], time[3], time[4], time[5]);
    }

    public static boolean isAllZeros(byte[] array) {
        for (byte b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean between(int value, int minInclusive, int maxExclusive) {
        return value >= minInclusive && value < maxExclusive;
    }

    public static boolean probably(Random rnd, int percentValueForTrue) {
        Validate.isTrue(percentValueForTrue >= 0 && percentValueForTrue <= 100);
        int value = rnd.nextInt(100);
        return between(value, 0, percentValueForTrue);
    }

    public static <T> T getRandomItem(Random rnd, List<T> list) {
        Validate.isTrue(list.size() > 0, "list is empty");
        return list.get(rnd.nextInt(list.size()));
    }

    /**
     * Encodes a value using the variable-length encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html">
     * Google Protocol Buffers</a>. It uses zig-zag encoding to efficiently
     * encode signed values. If values are known to be nonnegative,
     * {@link #writeUnsignedVarLong(long, DataOutput)} should be used.
     *
     * @param value value to encode
     * @param out   to write bytes to
     * @throws IOException if {@link DataOutput} throws {@link IOException}
     */
    public static void writeSignedVarLong(long value, DataOutput out) throws IOException {
        // Great trick from http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
        writeUnsignedVarLong((value << 1) ^ (value >> 63), out);
    }

    /**
     * Encodes a value using the variable-length encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html">
     * Google Protocol Buffers</a>. Zig-zag is not used, so input must not be negative.
     * If values can be negative, use {@link #writeSignedVarLong(long, DataOutput)}
     * instead. This method treats negative input as like a large unsigned value.
     *
     * @param value value to encode
     * @param out   to write bytes to
     * @throws IOException if {@link DataOutput} throws {@link IOException}
     */
    public static void writeUnsignedVarLong(long value, DataOutput out) throws IOException {
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            out.writeByte(((int) value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte((int) value & 0x7F);
    }

    /**
     * @see #writeSignedVarLong(long, DataOutput)
     */
    public static void writeSignedVarInt(int value, DataOutput out) throws IOException {
        // Great trick from http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
        writeUnsignedVarInt((value << 1) ^ (value >> 31), out);
    }

    /**
     * @see #writeUnsignedVarLong(long, DataOutput)
     */
    public static void writeUnsignedVarInt(int value, DataOutput out) throws IOException {
        while ((value & 0xFFFFFF80) != 0L) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value & 0x7F);
    }

    public static byte[] writeSignedVarInt(int value) {
        // Great trick from http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
        return writeUnsignedVarInt((value << 1) ^ (value >> 31));
    }

    /**
     * @see #writeUnsignedVarLong(long, DataOutput)
     * <p/>
     * This one does not use streams and is much faster.
     * Makes a single object each time, and that object is a primitive array.
     */
    public static byte[] writeUnsignedVarInt(int value) {
        byte[] byteArrayList = new byte[10];
        int i = 0;
        while ((value & 0xFFFFFF80) != 0L) {
            byteArrayList[i++] = ((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        byteArrayList[i] = ((byte) (value & 0x7F));
        byte[] out = new byte[i + 1];
        for (; i >= 0; i--) {
            out[i] = byteArrayList[i];
        }
        return out;
    }

    /**
     * @param in to read bytes from
     * @return decode value
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 9 bytes have been read
     * @see #writeSignedVarLong(long, DataOutput)
     */
    public static long readSignedVarLong(DataInput in) throws IOException {
        long raw = readUnsignedVarLong(in);
        // This undoes the trick in writeSignedVarLong()
        long temp = (((raw << 63) >> 63) ^ raw) >> 1;
        // This extra step lets us deal with the largest signed values by treating
        // negative results from read unsigned methods as like unsigned values
        // Must re-flip the top bit if the original read value had it set.
        return temp ^ (raw & (1L << 63));
    }

    /**
     * @param in to read bytes from
     * @return decode value
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 9 bytes have been read
     * @see #writeUnsignedVarLong(long, DataOutput)
     */
    public static long readUnsignedVarLong(DataInput in) throws IOException {
        long value = 0L;
        int i = 0;
        long b;
        while (((b = in.readByte()) & 0x80L) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            if (i > 63) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        return value | (b << i);
    }

    /**
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 5 bytes have been read
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     * @see #readSignedVarLong(DataInput)
     */
    public static int readSignedVarInt(DataInput in) throws IOException {
        int raw = readUnsignedVarInt(in);
        // This undoes the trick in writeSignedVarInt()
        int temp = (((raw << 31) >> 31) ^ raw) >> 1;
        // This extra step lets us deal with the largest signed values by treating
        // negative results from read unsigned methods as like unsigned values.
        // Must re-flip the top bit if the original read value had it set.
        return temp ^ (raw & (1 << 31));
    }

    /**
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 5 bytes have been read
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     * @see #readUnsignedVarLong(DataInput)
     */
    public static int readUnsignedVarInt(DataInput in) throws IOException {
        int value = 0;
        int i = 0;
        int b;
        while (((b = in.readByte()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            if (i > 35) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        return value | (b << i);
    }

    public static int readSignedVarInt(byte[] bytes) {
        int raw = readUnsignedVarInt(bytes);
        // This undoes the trick in writeSignedVarInt()
        int temp = (((raw << 31) >> 31) ^ raw) >> 1;
        // This extra step lets us deal with the largest signed values by treating
        // negative results from read unsigned methods as like unsigned values.
        // Must re-flip the top bit if the original read value had it set.
        return temp ^ (raw & (1 << 31));
    }

    public static int readUnsignedVarInt(byte[] bytes) {
        int value = 0;
        int i = 0;
        byte rb = Byte.MIN_VALUE;
        for (byte b : bytes) {
            rb = b;
            if ((b & 0x80) == 0) {
                break;
            }
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 35) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        return value | (rb << i);
    }

    public static String bitSetToBitmaskStr(BitSet bitSet, int significatBits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < significatBits; i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    public static String bitSetToString(BitSet bitSet) {
        if (bitSet.isEmpty()) {
            return "{}";
        }
        return bitSet.toString();
    }


}
