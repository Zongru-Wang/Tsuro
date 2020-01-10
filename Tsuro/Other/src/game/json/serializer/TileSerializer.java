package game.json.serializer;

import com.google.gson.*;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;

import java.lang.reflect.Type;

/**
 * TileSerializer that serialize tile to json string in the format [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]].
 * Port is serialized according to PortSerializer.
 */
public class TileSerializer implements JsonSerializer<Tile> {
    private final Gson gson;
    public TileSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Port.class, new PortSerializer());
        this.gson = builder.create();
    }

    /**
     * @param tile the tile that we want to transfer to json string
     * @param type Tile.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in form of [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]]
     */
    @Override
    public JsonElement serialize(Tile tile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray result = new JsonArray();
        for (Pair<Port, Port> portPair: tile.asPairCollection()) {
            JsonArray portPairJson = new JsonArray();
            portPairJson.add(this.gson.toJsonTree(portPair.getKey(), Port.class));
            portPairJson.add(this.gson.toJsonTree(portPair.getValue(), Port.class));
            result.add(portPairJson);
        }
        return result;
    }
}
