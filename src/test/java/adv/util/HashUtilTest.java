package adv.util;

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
        for(int i=0;i<100;i++) {
            System.out.println(BitUtil.toBase16(HashUtil.hash48(i + ".")));
        }
    }
}