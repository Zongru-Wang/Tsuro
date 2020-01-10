package game.json.deserializer;

import com.google.gson.*;
import game.board.InitialPlacement;
import game.board.Token;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import java.lang.reflect.Type;

/**
 * InitialPlacementDeserializer that deserialize InitialPlacement json in the format [TILE, PORT, int, int].
 * Tile is deserialized according to TileDeserializer
 * Port is deserialized according to PortDeserializer
 */
public class InitialPlacementDeserializer implements JsonDeserializer<InitialPlacement> {
    private final Gson gson;

    public InitialPlacementDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        this.gson = builder.create();
    }

    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for initial placement";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type InitialPlacement.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return InitialPlacement object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [TILE, PORT, int, int]
     */
    @Override
    public InitialPlacement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 5) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        JsonElement tokenJson = jsonElement.getAsJsonArray().get(0);
        JsonElement tileJson = jsonElement.getAsJsonArray().get(1);
        JsonElement portJson = jsonElement.getAsJsonArray().get(2);
        JsonElement xJson = jsonElement.getAsJsonArray().get(3);
        JsonElement yJson = jsonElement.getAsJsonArray().get(4);
        if (!xJson.isJsonPrimitive() || !yJson.isJsonPrimitive()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        Token token = this.gson.fromJson(tokenJson, Token.class);
        Tile tile = this.gson.fromJson(tileJson, Tile.class);
        Port port = this.gson.fromJson(portJson, Port.class);
        int x = xJson.getAsInt();
        int y = yJson.getAsInt();
        return InitialPlacement.of(token, tile, port, Position.of(x, y));
    }
}
