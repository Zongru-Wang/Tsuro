package game.remote.messages;

import com.google.gson.JsonElement;

import java.io.PrintWriter;

/**
 * AbstractJsonMessageImpl which implements Json Message
 */
abstract class AbstractJsonMessageImpl extends AbstractMessageImpl implements Message {

    /**
     * using Serializer to transfer the game object to json object
     * @return
     */
    abstract JsonElement toJson();

    /**
     * @param printWriter the destination of where the json message send to
     */
    @Override
    void sendTo(PrintWriter printWriter) {
        printWriter.println(toJson().toString());
    }
}
