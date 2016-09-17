package adv.util;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 */
public class NetworkInterfaceLogger {

    public static String listInterfaces() {
        try {
            StringBuilder buf = new StringBuilder(1000);
            buf.append("Network interfaces: \n");
            int i = 0;
            for (Enumeration<NetworkInterface> netEnum = NetworkInterface.getNetworkInterfaces(); netEnum.hasMoreElements(); ) {
                NetworkInterface iface = netEnum.nextElement();
                buf.append(String.format("#%s name: '%s' displayName: '%s' ip: %s mac: '%s' mtu: '%s'\n",
                        new Object[]{
                                i++,
                                iface.getName(),
                                iface.getDisplayName(),
                                getIpAddresses(iface),
                                getMac(iface),
                                iface.getMTU()
                        }));
            }
            return buf.toString();
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String getIpAddresses(NetworkInterface iface) {
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (InterfaceAddress ia : iface.getInterfaceAddresses()) {
            buf.append("  address").append(i).append(": (")
                    .append(ia.getAddress())
                    .append(")  broadcast").append(i).append(": (")
                    .append(ia.getBroadcast())
                    .append("/")
                    .append(ia.getNetworkPrefixLength())
                    .append(")  ");
            i++;
        }
        if (iface.getInterfaceAddresses().isEmpty()) {
            buf.append("NONE");
        }
        return buf.toString();
    }

    private static String getMac(NetworkInterface iface) throws SocketException {
        if (iface.getHardwareAddress() == null || iface.getHardwareAddress().length == 0) {
            return "NONE";
        }
        return BitUtil.toBase16(iface.getHardwareAddress());
    }
}
