package game.json.deserializer;

import com.google.gson.*;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TileDeserializer that deserialize tile json in the format [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]].
 * Port is deserialized according to PortDeserializer.
 */
public class TileDeserializer implements JsonDeserializer<Tile> {
    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for tile";
    private final Gson gson;
    public TileDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type Tile.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return Tile object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]]
     */
    @Override
    public Tile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Collection<Pair<Port, Port>> portPair = new ArrayList<>();
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 4)
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        for (JsonElement portConnectionJsonElement: jsonElement.getAsJsonArray()) {
            if (!portConnectionJsonElement.isJsonArray())
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            JsonArray portConnection = portConnectionJsonElement.getAsJsonArray();
            if (portConnection.size() != 2)
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            Port source = gson.fromJson(portConnection.get(0), Port.class);
            Port target = gson.fromJson(portConnection.get(1), Port.class);
            portPair.add(new Pair<>(source, target));
        }
        return Tile.of(portPair);
    }
}
