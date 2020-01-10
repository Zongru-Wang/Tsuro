package property;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.common.Position;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class TestPositionProperty {
    @Property
    public void testInstantiation(int x, int y) {
        Position portPosition = Position.of(x, y);
        assertEquals(x, portPosition.getX());
        assertEquals(y, portPosition.getY());
    }
}
