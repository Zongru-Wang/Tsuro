package game.json.serializer;

import com.google.gson.*;
import game.board.IntermediatePlacement;
import game.tile.Tile;

import java.lang.reflect.Type;

/**
 * IntermediatePlacementSerializer that serialize IntermediatePlacement json in the format [TILE, int, int].
 * Tile is serialized according to TileSerializer
 */
public class IntermediatePlacementSerializer implements JsonSerializer<IntermediatePlacement> {
    private final Gson gson;
    public IntermediatePlacementSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        this.gson = builder.create();
    }

    /**
     * @param intermediatePlacement the intermediatePlacement that we want to transfer to json string
     * @param type IntermediatePlacement.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in the format [TILE, int, int]
     */
    @Override
    public JsonElement serialize(IntermediatePlacement intermediatePlacement, Type type, JsonSerializationContext jsonSerializationContext) {
        Tile tile = intermediatePlacement.getTile();
        int x = intermediatePlacement.getPosition().getKey();
        int y = intermediatePlacement.getPosition().getValue();
        JsonElement tileJson = gson.toJsonTree(tile, Tile.class);
        JsonArray result = new JsonArray();
        result.add(tileJson);
        result.add(x);
        result.add(y);
        return result;
    }
}
