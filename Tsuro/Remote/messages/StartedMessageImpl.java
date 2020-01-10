package game.remote.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Represent message of game start
 */
public class StartedMessageImpl extends AbstractJsonMessageImpl implements Message {
    /**
     * using Serializer to transfer the game object to json object
     *
     * @return
     */
    @Override
    JsonElement toJson() {
        return new JsonPrimitive("started");
    }
}
