package adv.util;

import org.junit.Test;

import static adv.util.ArrayUtil.disjointSorted;
import static adv.util.ArrayUtil.mergeSorted;
import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void disjointSortedTest() {
        assertTrue(disjointSorted(new int[]{}, new int[]{}));
        assertTrue(disjointSorted(new int[]{1}, new int[]{}));
        assertTrue(disjointSorted(new int[]{}, new int[]{1}));

        assertTrue(disjointSorted(new int[]{2,3}, new int[]{1,4}));
        assertTrue(disjointSorted(new int[]{1,3}, new int[]{2,4}));
        assertTrue(disjointSorted(new int[]{2,4}, new int[]{1,3}));
        assertTrue(disjointSorted(new int[]{1,4}, new int[]{2,3}));

        assertTrue(disjointSorted(new int[]{2,4,4,4,5,6}, new int[]{1,7}));
        assertFalse(disjointSorted(new int[]{1,5}, new int[]{2,4,4,4,5,6}));
    }

    @Test
    public void mergeSortedTest() {
        assertArrayEquals(new int[]{}, mergeSorted(new int[]{}, new int[]{}));
        assertArrayEquals(new int[]{1}, mergeSorted(new int[]{1}, new int[]{}));
        assertArrayEquals(new int[]{1}, mergeSorted(new int[]{}, new int[]{1}));

        assertArrayEquals(new int[]{1,2,3,4}, mergeSorted(new int[]{2,3}, new int[]{1,4}));
        assertArrayEquals(new int[]{1,2,3,4}, mergeSorted(new int[]{1,3}, new int[]{2,4}));
        assertArrayEquals(new int[]{1,2,3,4}, mergeSorted(new int[]{2,4}, new int[]{1,3}));
        assertArrayEquals(new int[]{1,2,3,4}, mergeSorted(new int[]{1,4}, new int[]{2,3}));

        assertArrayEquals(new int[]{1,2,4,5,6,7}, mergeSorted(new int[]{2,4,5,6}, new int[]{1,5,7}));
        assertArrayEquals(new int[]{1,2,4,5,6}, mergeSorted(new int[]{1,5}, new int[]{2,4,5,6}));
    }
}