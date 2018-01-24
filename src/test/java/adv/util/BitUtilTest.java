package adv.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitUtilTest {

    @Test
    public void testSetNLowerBits() throws Exception {
        assertEquals(0b1, BitUtil.setNLowerBits(1));
        assertEquals(0b11, BitUtil.setNLowerBits(2));
        assertEquals(0b111, BitUtil.setNLowerBits(3));
        assertEquals(0b1111111111, BitUtil.setNLowerBits(10));
        assertEquals(0xFFFFFFFFFFFFFFFFL, BitUtil.setNLowerBits(64));
    }
}