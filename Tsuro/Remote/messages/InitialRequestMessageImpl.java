package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.json.serializer.InitialRequestSerializer;
import game.remote.InitialRequest;

/**
 * Represent json message of initial turn request to players
 */
public class InitialRequestMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final InitialRequest initialRequest;

    InitialRequestMessageImpl(InitialRequest initialRequest) {
        this.initialRequest = initialRequest;
    }

    @Override
    JsonElement toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.initialRequest, InitialRequest.class);
    }
}
