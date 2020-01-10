package property;


import com.google.gson.*;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.json.deserializer.TileDeserializer;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.junit.runner.RunWith;
import property.generator.JsonElementOrArrayGenerator;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static testharness.schema.JsonSchema.bounded;
import static testharness.schema.JsonSchema.port;

@RunWith(JUnitQuickcheck.class)
public class TestTileDeserializerProperty {
    @Property public void TileJson(@From(JsonElementOrArrayGenerator.class) JsonElement jsonElement) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Tile.class, new TileDeserializer());
        Gson gson = gsonBuilder.create();
        if (bounded(port, port, port, port, port, port, port, port).validate(jsonElement)) {
            Collection<Pair<Port, Port>> portCollection = new ArrayList<>();
            JsonArray array = jsonElement.getAsJsonArray();
            for (int i = 0; i < 8; i+=2) {
                portCollection.add(new Pair<>(Port.of(array.get(i).getAsString()), Port.of(array.get(i).getAsString())));
            }
            assertEquals(Tile.of(portCollection), gson.fromJson(jsonElement, Tile.class));
        } else {
            try {
                gson.fromJson(jsonElement, Tile.class);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof JsonParseException);
            }
        }
    }
}