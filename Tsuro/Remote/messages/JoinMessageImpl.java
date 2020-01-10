package game.remote.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * represent the message (client-i=>server) that the player join the game
 */
class JoinMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final String name;
    JoinMessageImpl(String name) {
        this.name = name;
    }

    /**
     * using Serializer to transfer the game object to json object
     *
     * @return
     */
    @Override
    JsonElement toJson() {
        return new JsonPrimitive(name);
    }
}
