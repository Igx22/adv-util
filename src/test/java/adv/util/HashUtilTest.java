package adv.util;

import adv.util.plainsocket.Base91;
import org.junit.Assert;
import org.junit.Test;

public class HashUtilTest {

    @Test
    public void md5() {
        System.out.println(HashUtil.md5("blablabla"));
        System.out.println(HashUtil.md5("1111"));
        System.out.println(HashUtil.md5(""));
        System.out.println(HashUtil.md5("0219832-0841-2841-2412-49821-83421-984190284-0284-284823984"));
    }

    @Test
    public void test48() {
        for (int i = 0; i < 100; i++) {
            System.out.println(BitUtil.toBase16(HashUtil.hash48(i + ".")));
        }
    }

    @Test
    public void test24() {
        calculateHashAndDoBase91("0219832-0841-2841-2412-49821-83421-984190284-0284-284823984");
        calculateHashAndDoBase91("blablabla");
        for (int i = 0; i < 100; i++) {
            String input = i + ".";
            calculateHashAndDoBase91(input);
        }
    }

    private void calculateHashAndDoBase91(String input) {
        long value = HashUtil.hash24(input);

        String base91 = Base91.long24ToBase91(value);
        long reverseInput = Base91.base91ToLong24(base91);
        System.out.printf("input: '%s'  " +
                        "hash24: %s  " +
                        "base91: '%s'  " +
                        "base91Reverse: '%s' \n",
                input,
                BitUtil.toBase16(value, 3) + "(" + BitUtil.toBase16(value) + ")",
                base91,
                BitUtil.toBase16(reverseInput, 3) + "(" + BitUtil.toBase16(reverseInput) + ")");
        Assert.assertEquals(value, reverseInput);
    }
}