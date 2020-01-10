package property;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.common.Position;
import game.common.PositionUtil;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestPositionUtilProperty {
    @Property public void manhattanDistance(int x1, int y1, int x2, int y2) {
        int dist = Math.abs(x1 - x2) + Math.abs(y1 -  y2);
        assertEquals(dist, PositionUtil.manhattanDistance(Position.of(x1, y1), Position.of(x2, y2)));
    }

    @Property public void areDirectNeighbor(int x1, int y1, int x2, int y2) {
        int dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        assertEquals(dist == 1, PositionUtil.areDirectNeighbor(Position.of(x1, y1), Position.of(x2, y2)));
    }

    @Property public void neighborAt(int x, int y) {
        try {
            PositionUtil.neighborAt(Position.of(x, y), null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Property public void euclideanDistance(@InRange(minInt = -1000, maxInt = 1000)int x1,
                                            @InRange(minInt = -1000, maxInt = 1000)int y1,
                                            @InRange(minInt = -1000, maxInt = 1000)int x2,
                                            @InRange(minInt = -1000, maxInt = 1000)int y2) {
        int xDiff = Math.abs(x1 - x2);
        int yDiff = Math.abs(y1 - y2);
        assertTrue(0.00000001 > Math.abs(Math.sqrt(xDiff * xDiff + yDiff * yDiff) - PositionUtil.euclideanDistance(Position.of(x1, y1), Position.of(x2, y2))));
    }

    @Property public void directNeighbors(int x, int y) {
        assertEquals(4, PositionUtil.directNeighbors(Position.of(x, y)).size());
    }

    @Property public void diagonalNeighbors(int x, int y) {
        assertEquals(8, PositionUtil.diagonalNeighbors(Position.of(x, y)).size());
    }
}
