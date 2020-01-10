package game.json.serializer;

import com.google.gson.*;
import game.json.deserializer.TileDeserializer;
import game.tile.Tile;

import java.lang.reflect.Type;

/**
 * OptionalTileSerializer that serialize tile to json string in null or in the format [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]].
 * Port is serialized according to PortSerializer.
 */
public class OptionalTileSerializer implements JsonSerializer<Tile> {
    private final Gson gson;
    public OptionalTileSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        this.gson = builder.create();
    }

    /**
     * @param tile the tile that we want to transfer to json string
     * @param type Tile.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in JsonNull or in the format [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]]
     */
    @Override
    public JsonElement serialize(Tile tile, Type type, JsonSerializationContext jsonSerializationContext) {
        if (tile == null) {
            return JsonNull.INSTANCE;
        } else {
            return this.gson.toJsonTree(tile, Tile.class);
        }
    }
}
