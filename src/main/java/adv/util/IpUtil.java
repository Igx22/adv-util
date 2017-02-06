package adv.util;

import com.google.common.net.InetAddresses;

public class IpUtil {

    // todo подумать насколько это быстро
    public static int parseIpv4String(String ipv4) {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ipv4));
    }
}
