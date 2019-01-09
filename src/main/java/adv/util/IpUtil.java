package adv.util;

import com.google.common.net.InetAddresses;

// todo: может быть таскать ip по коду не строкой, а в провалидированной DTO или InetAddress?
public class IpUtil {

    // todo подумать насколько это быстро
    public static int parseIpv4String(String ipv4) {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ipv4));
    }

    public static String ipv4ToString(Long ipv4) {
        if (ipv4 == null) {
            return "";
        }
        return ipv4ToString((int) ipv4.longValue() & 0xFFFFFFFF);
    }

    public static String ipv4ToString(int ipv4) {
        return InetAddresses.toAddrString(InetAddresses.fromInteger(ipv4));
    }

    public static String depersonalizeIpv4String(String ipv4) {
        String[] split = ipv4.split("\\.");
        Check.isTrue(split.length == 4, "Invalid IPv4 address");
        split[3] = "0";
        return split[0] + "." + split[1] + "." + split[2] + "." + split[3];
    }
}
