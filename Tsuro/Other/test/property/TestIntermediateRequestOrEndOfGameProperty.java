package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.remote.IntermediateRequestOrEndOfGame;
import org.junit.runner.RunWith;
import property.generator.IntermediateRequestOrEndOfGameGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnitQuickcheck.class)
public class TestIntermediateRequestOrEndOfGameProperty {
    @Property
    public void equalsHashcode(@From(IntermediateRequestOrEndOfGameGenerator.class) IntermediateRequestOrEndOfGame iore,
                               @From(IntermediateRequestOrEndOfGameGenerator.class) IntermediateRequestOrEndOfGame iore2) {
        if (iore.equals(iore2)) {
            assertEquals(iore.hashCode(), iore2.hashCode());
        }
        if (iore.hashCode() != iore2.hashCode()) {
            assertNotEquals(iore, iore2);
        }
    }
}