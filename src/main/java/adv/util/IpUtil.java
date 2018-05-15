package adv.util;

import com.google.common.net.InetAddresses;

// todo: может быть таскать ip по коду не строкой, а в провалидированной DTO или InetAddress?
public class IpUtil {

    // todo подумать насколько это быстро
    public static int parseIpv4String(String ipv4) {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ipv4));
    }

    public static String depersonalizeIpv4String(String ipv4) {
        String[] split = ipv4.split("\\.");
        split[3] = "0";
        return split[0] + "." + split[1] + "." + split[2] + "." + split[3];
    }
}
