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


    public static boolean disjointSorted(int[] a, int[] b) {
        int i = 0, j = 0;

        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) {
                return false;
            } else if (a[i] < b[j]) {
                i++;
            } else {
                j++;
            }
        }

        return true;
    }
}
