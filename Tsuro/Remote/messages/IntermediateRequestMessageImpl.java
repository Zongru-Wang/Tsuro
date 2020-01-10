package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.json.serializer.IntermediateRequestSerializer;
import game.remote.IntermediateRequest;

/**
 * Represent json message of intermediate turn request to players
 */
class IntermediateRequestMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final IntermediateRequest intermediateRequest;

    IntermediateRequestMessageImpl(IntermediateRequest intermediateRequest) {
        this.intermediateRequest = intermediateRequest;
    }

    @Override
    JsonElement toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.intermediateRequest, IntermediateRequest.class);
    }
}
