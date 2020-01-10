package property.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MockTestSocket extends Socket {
    private final InputStream inputStream;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public MockTestSocket(String receivedMessage) {
        this.inputStream = new ByteArrayInputStream(receivedMessage.getBytes());
    }

    public MockTestSocket() {
        this.inputStream = new ByteArrayInputStream("".getBytes());
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public String getSentMessage() {
        return this.outputStream.toString();
    }
}
