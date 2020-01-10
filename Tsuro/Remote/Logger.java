package game.remote;

import game.remote.messages.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * logger for server-client communication that can output log to output stream;
 */
public class Logger {
    private static final String FROM_SERVER = "server=>client-%s:\n";
    private static final String FROM_CLIENT = "client-%s=>server:\n";
    private final StringBuilder builder;

    public Logger() {
        this.builder = new StringBuilder();
    }

    public void logSentMessage(Message message, String clientId) {
        this.builder.append(String.format(FROM_SERVER, clientId));
        OutputStream stream = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);

            }

            @Override
            public String toString() {
                return string.toString();
            }
        };
        message.sendTo(stream);
        this.builder.append(stream.toString());
    }

    public void logReceivedMessage(String message, String clientId) {
        this.builder.append(String.format(FROM_CLIENT, clientId));
        this.builder.append(message);
        this.builder.append(System.lineSeparator());
    }

    public void outputTo(OutputStream outputStream) {
        PrintStream printStream = new PrintStream(outputStream);
        printStream.print(builder.toString());
    }
}
