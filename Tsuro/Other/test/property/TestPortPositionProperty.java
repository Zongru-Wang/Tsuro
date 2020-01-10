package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.common.PortPosition;
import game.tile.Port;
import org.junit.runner.RunWith;
import property.generator.PortGenerator;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class TestPortPositionProperty {
    @Property
    public void testInstantiation(@From(PortGenerator.class) Port port, int x, int y) {
        PortPosition portPosition = PortPosition.of(port, x, y);
        assertEquals(port, portPosition.getPort());
        assertEquals(x, portPosition.getX());
        assertEquals(y, portPosition.getY());
    }
}
