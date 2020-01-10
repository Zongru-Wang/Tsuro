package game.json.serializer;

import com.google.gson.*;
import game.board.InitialPlacement;
import game.board.Token;
import game.tile.Port;
import game.tile.Tile;

import java.lang.reflect.Type;

/**
 * InitialPlacementSerializer that serialize InitialPlacement json in the format [TILE, PORT, int, int].
 * Tile is serialized according to TileSerializer
 * Port is serialized according to PortSerializer
 */
public class InitialPlacementSerializer implements JsonSerializer<InitialPlacement> {
    private final Gson gson;
    public InitialPlacementSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenSerializer());
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        builder.registerTypeAdapter(Port.class, new PortSerializer());
        this.gson = builder.create();
    }

    /**
     * @param initialPlacement the initialPlacement that we want to transfer to json string
     * @param type InitialPlacement.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in the format [TILE, PORT, int, int]
     */
    @Override
    public JsonElement serialize(InitialPlacement initialPlacement, Type type, JsonSerializationContext jsonSerializationContext) {
        Token token = initialPlacement.getToken();
        Tile tile = initialPlacement.getTile();
        Port port = initialPlacement.getPort();
        int x = initialPlacement.getPosition().getKey();
        int y = initialPlacement.getPosition().getValue();
        JsonElement tokenJson = gson.toJsonTree(token, Token.class);
        JsonElement tileJson = gson.toJsonTree(tile, Tile.class);
        JsonElement portJson = gson.toJsonTree(port, Port.class);
        JsonArray result =  new JsonArray();
        result.add(tokenJson);
        result.add(tileJson);
        result.add(portJson);
        result.add(x);
        result.add(y);
        return result;
    }
}
