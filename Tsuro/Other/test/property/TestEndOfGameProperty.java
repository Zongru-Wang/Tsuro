package property;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.json.serializer.EndOfGameSerializer;
import game.remote.EndOfGame;
import org.junit.runner.RunWith;
import property.generator.EndOfGameGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitQuickcheck.class)
public class TestEndOfGameProperty {
    @Property
    public void equalsHashcode(@From(EndOfGameGenerator.class) EndOfGame endOfGame,
                               @From(EndOfGameGenerator.class) EndOfGame endOfGame2) {
        if (endOfGame.equals(endOfGame2)) {
            assertEquals(endOfGame.hashCode(), endOfGame2.hashCode());
        }
        if (endOfGame.hashCode() != endOfGame2.hashCode()) {
            assertNotEquals(endOfGame, endOfGame2);
        }
    }

    @Property
    public void toString(@From(EndOfGameGenerator.class) EndOfGame endOfGame) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        Gson gson = builder.create();
        assertEquals(gson.toJsonTree(endOfGame, EndOfGame.class).toString(), endOfGame.toString());
    }
}