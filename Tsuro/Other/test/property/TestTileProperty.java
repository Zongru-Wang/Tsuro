package property;

import com.google.gson.JsonParseException;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.tile.Tile;
import game.tile.Port;
import javafx.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.runner.RunWith;
import property.generator.PortGenerator;
import property.generator.TileGenerator;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestTileProperty {
    @Property public void connectedPort(
            @From(TileGenerator.class) Tile t,
            @From(PortGenerator.class) Port p) {
        assertEquals(p, t.connectedPort(t.connectedPort(p)));
    }

    @Property public void asGraph(@From(TileGenerator.class) Tile t) {
        assertEquals(8, t.asGraph().vertexSet().size());
        assertEquals(4, t.asGraph().edgeSet().size());
        assertEquals(new HashSet<Port>(Port.all()), t.asGraph().vertexSet());
        assertEquals(t, Tile.of(t.asGraph()));
    }

    @Property public void connectedPortAsGraph(
            @From(TileGenerator.class) Tile t,
            @From(PortGenerator.class) Port p) {
        assertTrue(t.asGraph().containsEdge(p, t.connectedPort(p)));
    }

    @Property public void equalsHashCode(
            @From(TileGenerator.class) Tile t1,
            @From(TileGenerator.class) Tile t2) {
        if (t1.equals(t2)) {
            assertEquals(t1.hashCode(), t2.hashCode());
        }
    }

    @Property public void toString(@From(TileGenerator.class) Tile t) {
        assertEquals(t.asGraph().toString(), t.toString());
    }

    @Property public void equals(@From(TileGenerator.class) Tile t, Object obj) {
        if (!(obj instanceof Port)) {
            assertNotEquals(t, obj);
        }
    }

    @Property public void rotateEquals(@From(TileGenerator.class) Tile t, int i) {
        assertEquals(t, t.rotate(360 * (i/ 360)));
    }

    @Property public void tileOfTile(@From(TileGenerator.class) Tile t) {
        assertEquals(t, Tile.of(t));
    }

    @Property public void tileOfPairCollection() {
        List<Port> ports = new ArrayList<>(Port.all());
        Collections.shuffle(ports);
        Collection<Pair<Port, Port>> pairCollection = new ArrayList<>();
        for (int i = 0; i < 8; i += 2) {
            pairCollection.add(new Pair<>(ports.get(i), ports.get(i + 1)));
        }
        Tile t = Tile.of(pairCollection);
        for (int i = 0; i < 8; i += 2) {
            assertEquals(t.connectedPort(ports.get(i)), ports.get(i + 1));
            assertEquals(t.connectedPort(ports.get(i + 1)), ports.get(i));
        }
    }

    @Property(trials = 1) public void loadDefaultAll() {
        try {
            assertEquals(Tile.loadAll("/tile.json"), Tile.loadDefaultAll());
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Property(trials = 1) public void loadAllNull() {
        try {
            Tile.loadAll(null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property(trials = 1) public void nonArrayLoadAll() {
        try {
            Tile.loadAll("/testTileJson/nonArrayTile.json");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof JsonParseException);
        }
    }

    @Property(trials = 1) public void fileDNELoadAll() {
        try {
            Tile.loadAll("/doesNotExist.json");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Property(trials = 1) public void nonTileLoadAll() {
        try {
            Tile.loadAll("/testTileJson/nonTile.json");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof JsonParseException);
        }
    }

    @Property public void pairCollection(@From(TileGenerator.class) Tile t) {
        assertEquals(t, Tile.of(t.asPairCollection()));
    }

    @Property public void emptyPairCollection() {
        try {
            Tile.of(new ArrayList<>());
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property public void emptyGraph() {
        try {
            Tile.of(new SimpleGraph<>(DefaultEdge.class));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
