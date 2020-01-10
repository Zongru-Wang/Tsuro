package game.json.deserializer;

import com.google.gson.*;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import java.lang.reflect.Type;

/**
 * IntermediatePlacementDeserializer that deserialize IntermediatePlacement json in the format [TILE, int, int].
 * Tile is deserialized according to TileDeserializer
 */
public class IntermediatePlacementDeserializer implements JsonDeserializer<IntermediatePlacement> {
    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for intermediate placement";
    private final Gson gson;

    public IntermediatePlacementDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type IntermediatePlacement.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return IntermediatePlacement object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [TILE, int, int]
     */
    @Override
    public IntermediatePlacement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 3) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        JsonElement tileJson = jsonElement.getAsJsonArray().get(0);
        JsonElement xJson = jsonElement.getAsJsonArray().get(1);
        JsonElement yJson = jsonElement.getAsJsonArray().get(2);
        if (!xJson.isJsonPrimitive() || !yJson.isJsonPrimitive()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        Tile tile = this.gson.fromJson(tileJson, Tile.class);
        int x = xJson.getAsInt();
        int y = yJson.getAsInt();
        return IntermediatePlacement.of(tile, Position.of(x, y));
    }
}
