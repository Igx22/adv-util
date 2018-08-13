package adv.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashUtilTest {

    @Test
    public void md5() {
        System.out.println(HashUtil.md5("blablabla"));
        System.out.println(HashUtil.md5("1111"));
        System.out.println(HashUtil.md5(""));
        System.out.println(HashUtil.md5("0219832-0841-2841-2412-49821-83421-984190284-0284-284823984"));
    }
}