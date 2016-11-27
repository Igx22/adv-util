package adv.util;

public class ArrayUtil {

    public static int getLenghNullSafe(long[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int getLenghNullSafe(int[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int getLenghNullSafe(byte[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int getLenghNullSafe(Object[] arr) {
        return arr == null ? 0 : arr.length;
    }
}
