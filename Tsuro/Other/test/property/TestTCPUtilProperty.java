package property;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.remote.TCPUtil;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestTCPUtilProperty {
    @Property
    public void parsePort(@InRange(minInt = 0, maxInt = 65535) int port) {
        assertEquals(port, TCPUtil.getPort(String.valueOf(port)));
    }

    @Property
    public void parseLocalhost() {
        try {
            assertEquals(InetAddress.getByName("localhost"), TCPUtil.getHost("localhost"));
        } catch (UnknownHostException e) {
            fail();
        }
    }

    @Property
    public void parsePortError(String str) {
        try {
            int port = Integer.parseInt(str);
        } catch (NumberFormatException ignore) {
            try {
                TCPUtil.getPort(str);
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
        try {
            assertEquals(InetAddress.getByName("localhost"), TCPUtil.getHost("localhost"));
        } catch (UnknownHostException e) {
            fail();
        }
    }

    @Property
    public void illegalPortNumber(int port) {
        if (port < 0 || port > 65535) {
            try {
                TCPUtil.getPort(String.valueOf(port));
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }

    @Property
    public void illegalHostName() {
        try {
            TCPUtil.getHost("illegalhost");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
