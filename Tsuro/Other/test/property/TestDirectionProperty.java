package property;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.common.Direction;
import org.junit.runner.RunWith;

import static game.common.Direction.*;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestDirectionProperty {
    @Property public void opposite(Direction direction) {
        switch (direction) {
            case NORTH:
                assertEquals(SOUTH, direction.opposite());
                break;
            case EAST:
                assertEquals(WEST, direction.opposite());
                break;
            case SOUTH:
                assertEquals(NORTH, direction.opposite());
                break;
            case WEST:
                assertEquals(EAST, direction.opposite());
                break;
            default:
                fail();
        }
    }
}
