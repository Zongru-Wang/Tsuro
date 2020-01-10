package game.remote;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * the class represent the TCP utility
 */
public class TCPUtil {

    private static final String ILLEGAL_PORT_FORMAT =
            "Illegal Parameter at <port>: must be integer";
    private static final String ILLEGAL_PORT_RANGE =
            "Illegal Parameter at <port>: must be integer between 0-65535";
    private static final String ILLEGAL_HOST =
            "Illegal Parameter at <host>: not a valid host name";

    /**
     * @param arg the address for the Host
     * @return InetAddress
     * @throws IllegalArgumentException if there is no such address
     */
    public static InetAddress getHost(String arg) throws IllegalArgumentException {
        try {
            return InetAddress.getByName(arg);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(ILLEGAL_HOST);
        }
    }

    /**
     * @param arg the port
     * @return current port
     * @throws IllegalArgumentException if port is not in the range or if there is format error
     */
    public static int getPort(String arg) throws IllegalArgumentException {
        try {
            int port = Integer.parseInt(arg);
            if (0 <= port && port <= 65535) {
                return port;
            } else {
                throw new IllegalArgumentException(ILLEGAL_PORT_RANGE);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ILLEGAL_PORT_FORMAT);
        }
    }
}
