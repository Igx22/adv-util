package adv.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IpUtilTest {

    @Test
    public void testDepersonalizeIpv4String() {
        assertEquals("192.168.0.0", IpUtil.depersonalizeIpv4String("192.168.0.1"));
    }

    @Test(expected = Exception.class)
    public void testDepersonalizeBadIpv4String() {
        IpUtil.depersonalizeIpv4String("192.168.1");
    }
}