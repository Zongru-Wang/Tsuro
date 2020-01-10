package game.json.deserializer;

import com.google.gson.*;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * OptionalTileDeserializer that deserialize Tile json as one of TILE or null
 */
public class OptionalTileDeserializer implements JsonDeserializer<Tile> {
    private final Gson gson;
    public OptionalTileDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type Tile.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return Tile object jsonElement represents
     * @throws JsonParseException when jsonElement is not one of TILE or null
     */
    @Override
    public Tile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        } else {
            return this.gson.fromJson(jsonElement, Tile.class);
        }
    }
}
