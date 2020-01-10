package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.InitialPlacement;
import org.junit.runner.RunWith;
import property.generator.InitialPlacementGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitQuickcheck.class)
public class TestInitialPlacementProperty {
    @Property(trials = 500)
    public void equalsHashcode(@From(InitialPlacementGenerator.class) InitialPlacement ip1,
                               @From(InitialPlacementGenerator.class) InitialPlacement ip2) {
        if (ip1.equals(ip2)) {
            assertEquals(ip1.hashCode(), ip2.hashCode());
        }
        if (ip1.hashCode() != ip2.hashCode()) {
            assertNotEquals(ip1, ip2);
        }
    }
}
