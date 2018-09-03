package adv.util;

/**
 * CRC64 checksum calculator based on the polynom specified in ISO 3309. The
 * implementation is based on the following publications:
 *
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Cyclic_redundancy_check</li>
 * <li>http://www.geocities.com/SiliconValley/Pines/8659/crc.htm</li>
 * </ul>
 */
public final class Crc64 {

    private static final long POLY64REV = 0xd800000000000000L;

    private static final long[] LOOKUPTABLE;

    static {
        LOOKUPTABLE = new long[0x100];
        for (int i = 0; i < 0x100; i++) {
            long v = i;
            for (int j = 0; j < 8; j++) {
                if ((v & 1) == 1) {
                    v = (v >>> 1) ^ POLY64REV;
                } else {
                    v = (v >>> 1);
                }
            }
            LOOKUPTABLE[i] = v;
        }
    }

    /**
     * Updates given checksum by given byte.
     *
     * @param sum
     *            initial checksum value
     * @param b
     *            byte to update the checksum with
     * @return updated checksum value
     */
    public static long update(final long sum, final byte b) {
        final int lookupidx = ((int) sum ^ b) & 0xff;
        return (sum >>> 8) ^ LOOKUPTABLE[lookupidx];
    }

    /**
     * Updates given checksum by bytes from given array.
     *
     * @param sum
     *            initial checksum value
     * @param bytes
     *            byte array to update the checksum with
     * @param fromIndexInclusive
     *            start index in array, inclusive
     * @param toIndexExclusive
     *            end index in array, exclusive
     * @return updated checksum value
     */
    public static long update(long sum, final byte[] bytes,
                               final int fromIndexInclusive, final int toIndexExclusive) {
        for (int i = fromIndexInclusive; i < toIndexExclusive; i++) {
            sum = update(sum, bytes[i]);
        }
        return sum;
    }


    private Crc64() {
    }

}
