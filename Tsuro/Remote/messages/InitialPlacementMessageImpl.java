package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.board.InitialPlacement;
import game.json.serializer.InitialPlacementSerializer;

/**
 * represent the json message (Token, Port, x, y) values of initial token position
 */
public class InitialPlacementMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final InitialPlacement initialPlacement;
    InitialPlacementMessageImpl(InitialPlacement initialPlacement) {
        this.initialPlacement = initialPlacement;
    }

    @Override
    JsonElement toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.initialPlacement, InitialPlacement.class);
    }
}
