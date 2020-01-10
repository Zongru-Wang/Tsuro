package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.IntermediatePlacement;
import game.common.Position;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.IntermediatePlacementGenerator;
import property.generator.TileGenerator;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestIntermediatePlacementProperty {
    @Property(trials = 500)
    public void equalsHashcode(@From(IntermediatePlacementGenerator.class) IntermediatePlacement ip1,
                               @From(IntermediatePlacementGenerator.class) IntermediatePlacement ip2) {
        if (ip1.equals(ip2)) {
            assertEquals(ip1.hashCode(), ip2.hashCode());
        }
        if (ip1.hashCode() != ip2.hashCode()) {
            assertNotEquals(ip1, ip2);
        }
    }
    @Property public void nullTile(@InRange(minInt = 0, maxInt = 9) int x,
                                   @InRange(minInt = 0, maxInt = 9) int y) {
        try {
            IntermediatePlacement.of(null, Position.of(x, y));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property public void nullPosition(@From(TileGenerator.class) Tile tile) {
        try {
            IntermediatePlacement.of(tile, null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property public void outOfBound(@From(TileGenerator.class) Tile tile, int x, int y) {
        try {
            if (!(0 <= x && x <= 9 && 0 <= y && y <= 9)) {
                IntermediatePlacement.of(tile, Position.of(x, y));
                fail();
            }
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
