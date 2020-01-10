package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.tile.Rotatable;
import org.junit.runner.RunWith;
import property.generator.RotatableGenerator;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestRotatableProperty {
    @Property public void rotateEqual(@From(RotatableGenerator.class) Rotatable r, int i) {
        assertEquals(r, r.rotate(360 * (i / 360)));
        assertEquals(r.angularDifference(r.rotate(360 * (i / 360))), 0);
    }

    @Property public void angularDifferenceOnWrongType(
            @From(RotatableGenerator.class) Rotatable r1,
            @From(RotatableGenerator.class) Rotatable r2) {
        if (!r1.getClass().equals(r2.getClass())) {
            try {
                r1.angularDifference(r2);
            } catch (Exception e) {
                assertTrue(e instanceof RuntimeException);
            }
        }
    }

    @Property public void rotateClockwise(@From(RotatableGenerator.class) Rotatable r, int i) {
        int degree = 90 + ((i/360) * 360);
        assertEquals(r.rotate(degree), r.rotateClockwise());
    };
    @Property public void rotateCounterClockwise(@From(RotatableGenerator.class) Rotatable r, int i) {
        int degree = 270 + ((i/360) * 360);
        assertEquals(r.rotate(degree), r.rotateCounterClockwise());
    }

    @Property public void rotateClockwiseThenCounterClockwise(@From(RotatableGenerator.class) Rotatable r) {
        assertEquals(r, r.rotateClockwise().rotateCounterClockwise());
    }
    @Property public void rotateAny(@From(RotatableGenerator.class) Rotatable r, int degrees) {
        if (degrees % 90 != 0) {
            try {
                r.rotate(degrees);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        } else {
            assertEquals(r.rotate(degrees).rotate(-degrees), r);
        }
    }
}
