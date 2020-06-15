package adv.util;

import java.util.Arrays;

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

    public static int[] mergeSorted(int[] a, int[] b) {
        int[] merge = new int[a.length + b.length];
        int i = 0, j = 0, l = 0;

        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) {
                merge[l] = a[i];
                i++;
                j++;
            } else if (a[i] < b[j]) {
                merge[l] = a[i];
                i++;
            } else {
                merge[l] = b[j];
                j++;
            }
            l++;
        }

        if (i < a.length) {
            System.arraycopy(a, i, merge, l, a.length - i);
            l += a.length - i;
        } else if (j < b.length) {
            System.arraycopy(b, j, merge, l, b.length - j);
            l += b.length - j;
        }

        return l < merge.length ? Arrays.copyOf(merge, l) : merge;
    }
}
