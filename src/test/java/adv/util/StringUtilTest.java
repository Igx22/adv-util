package adv.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void getFirstCommaSeparatedValue() throws Exception {
        Assert.assertEquals(null, StringUtil.getFirstCommaSeparatedValue(null));
        Assert.assertEquals("", StringUtil.getFirstCommaSeparatedValue(""));
        Assert.assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1"));
        Assert.assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1, 192.168.1.2"));
        Assert.assertEquals("192.168.1.1", StringUtil.getFirstCommaSeparatedValue("192.168.1.1, 192.168.1.2, 192.168.1.3"));
    }

}