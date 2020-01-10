package game.remote.messages;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * AbstractMessageImpl which implements Message
 */
abstract class AbstractMessageImpl implements Message {
    /**
     * @param outputStream the destination of where the message send to
     */
    @Override
    public void sendTo(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        this.sendTo(printWriter);
    }

    /**
     * @param printWriter the destination of where the message send to
     */
    abstract void sendTo(PrintWriter printWriter);
}
