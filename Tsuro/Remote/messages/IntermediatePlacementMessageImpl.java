package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.board.IntermediatePlacement;
import game.json.serializer.IntermediatePlacementSerializer;

/**
 * represent the json message (Token, x, y) values of intermediate tile placement
 */
public class IntermediatePlacementMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final IntermediatePlacement placement;

    IntermediatePlacementMessageImpl(IntermediatePlacement placement) {
        this.placement = placement;
    }

    @Override
    JsonElement toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.placement, IntermediatePlacement.class);
    }
}
