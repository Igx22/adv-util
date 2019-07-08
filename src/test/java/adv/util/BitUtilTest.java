package adv.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BitUtilTest {

    @Test
    public void testSetNLowerBits() throws Exception {
        assertEquals(0b1, BitUtil.setNLowerBits(1));
        assertEquals(0b11, BitUtil.setNLowerBits(2));
        assertEquals(0b111, BitUtil.setNLowerBits(3));
        assertEquals(0b1111111111, BitUtil.setNLowerBits(10));
        assertEquals(0xFFFFFFFFFFFFFFFFL, BitUtil.setNLowerBits(64));
    }

    @Test
    public void testIsBitOnLong() {
        long data = 1L << Integer.SIZE;
        for (int i = 0; i < Integer.SIZE; i++) {
            assertFalse(BitUtil.isBitOn(data, i));
        }
        assertTrue(BitUtil.isBitOn(data, Integer.SIZE));
        for (int i = Integer.SIZE + 1; i < Long.SIZE; i++) {
            assertFalse(BitUtil.isBitOn(data, i));
        }
    }

    @Test
    public void testWriteBits() {
        long data = 0;
        data = BitUtil.writeBits(data, 0b1101, 4, 3);
        assertEquals(0b1101000L, data);
        data = BitUtil.writeBits(data, 0b1101, 4, 2);
        assertEquals(0b1110100L, data);
    }

    @Test
    public void testReadBits() {
        long data = 0b10110101;
        assertEquals(0b10110L, BitUtil.readBits(data, 5, 3));
        assertEquals(0b10101L, BitUtil.readBits(data, 5, 0));
    }
}