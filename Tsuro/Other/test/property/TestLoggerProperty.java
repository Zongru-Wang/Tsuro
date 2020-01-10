package property;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.remote.Logger;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertFalse;

@RunWith(JUnitQuickcheck.class)
public class TestLoggerProperty {
    @Property
    public void loggerLogsSent(String str, String identifier) {
        Logger logger = new Logger();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        logger.logSentMessage(str, identifier);
        logger.outputTo(outputStream);
        assertFalse(outputStream.toString().isEmpty());
    }

    @Property
    public void loggerLogsReceived(String str, String identifier) {
        Logger logger = new Logger();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        logger.logReceivedMessage(str, identifier);
        logger.outputTo(outputStream);
        assertFalse(outputStream.toString().isEmpty());
    }
}
