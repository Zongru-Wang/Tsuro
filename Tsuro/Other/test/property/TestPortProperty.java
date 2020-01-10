package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.common.Direction;
import game.tile.Port;
import org.junit.runner.RunWith;
import property.generator.PortGenerator;

import java.util.Arrays;

import static org.junit.Assert.*;
import static game.common.Direction.*;

@RunWith(JUnitQuickcheck.class)
public class TestPortProperty {
    @Property public void portNumericEquality(@From(PortGenerator.class) Port port1,
                                                        @From(PortGenerator.class) Port port2) {
        if(port1.asNumericIndex() == port2.asNumericIndex()) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }

    @Property public void portAlphabeticEquality(@From(PortGenerator.class) Port port1,
                                              @From(PortGenerator.class) Port port2) {
        if(port1.asAlphabeticIndex() == port2.asAlphabeticIndex()) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }

    @Property public void portDirectionSideIndexEquality(@From(PortGenerator.class) Port port1,
                                                 @From(PortGenerator.class) Port port2) {
        if(port1.getDirection() == port2.getDirection() && port1.getSideIndex() == port2.getSideIndex()) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }

    @Property public void portNumericAlphabeticEquality(@From(PortGenerator.class) Port port1,
                                                          @From(PortGenerator.class) Port port2) {
        if((port1.asNumericIndex() == 0 && port2.asAlphabeticIndex() == 'A') ||
                (port1.asNumericIndex() == 1 && port2.asAlphabeticIndex() == 'B') ||
                (port1.asNumericIndex() == 2 && port2.asAlphabeticIndex() == 'C') ||
                (port1.asNumericIndex() == 3 && port2.asAlphabeticIndex() == 'D') ||
                (port1.asNumericIndex() == 4 && port2.asAlphabeticIndex() == 'E') ||
                (port1.asNumericIndex() == 5 && port2.asAlphabeticIndex() == 'F') ||
                (port1.asNumericIndex() == 6 && port2.asAlphabeticIndex() == 'G') ||
                (port1.asNumericIndex() == 7 && port2.asAlphabeticIndex() == 'H')) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }
    @Property public void portNumericDirectionSideIndexEquality(@From(PortGenerator.class) Port port1,
                                                               @From(PortGenerator.class) Port port2) {
        if((port1.asNumericIndex() == 0 && port2.getDirection() == NORTH && port2.getSideIndex() == 0) ||
                (port1.asNumericIndex() == 1 && port2.getDirection() == NORTH && port2.getSideIndex() == 1) ||
                (port1.asNumericIndex() == 2 && port2.getDirection() == EAST && port2.getSideIndex() == 0) ||
                (port1.asNumericIndex() == 3 && port2.getDirection() == EAST && port2.getSideIndex() == 1) ||
                (port1.asNumericIndex() == 4 && port2.getDirection() == SOUTH && port2.getSideIndex() == 0) ||
                (port1.asNumericIndex() == 5 && port2.getDirection() == SOUTH && port2.getSideIndex() == 1) ||
                (port1.asNumericIndex() == 6 && port2.getDirection() == WEST && port2.getSideIndex() == 0) ||
                (port1.asNumericIndex() == 7 && port2.getDirection() == WEST && port2.getSideIndex() == 1)) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }
    @Property public void portAlphabeticDirectionSideIndexEquality(@From(PortGenerator.class) Port port1,
                                                                @From(PortGenerator.class) Port port2) {
        if((port1.asAlphabeticIndex() == 'A' && port2.getDirection() == NORTH && port2.getSideIndex() == 0) ||
                (port1.asAlphabeticIndex() == 'B' && port2.getDirection() == NORTH && port2.getSideIndex() == 1) ||
                (port1.asAlphabeticIndex() == 'C' && port2.getDirection() == EAST && port2.getSideIndex() == 0) ||
                (port1.asAlphabeticIndex() == 'D' && port2.getDirection() == EAST && port2.getSideIndex() == 1) ||
                (port1.asAlphabeticIndex() == 'E' && port2.getDirection() == SOUTH && port2.getSideIndex() == 0) ||
                (port1.asAlphabeticIndex() == 'F' && port2.getDirection() == SOUTH && port2.getSideIndex() == 1) ||
                (port1.asAlphabeticIndex() == 'G' && port2.getDirection() == WEST && port2.getSideIndex() == 0) ||
                (port1.asAlphabeticIndex() == 'H' && port2.getDirection() == WEST && port2.getSideIndex() == 1)) {
            assertEquals(port1, port2);
        } else {
            assertNotEquals(port1, port2);
        }
    }
    @Property public void portNumericOf(int i) {
        assertEquals(Port.of(Math.abs(i) % 8), Port.of(Math.abs(i) % 8));
        assertEquals(Port.of(Math.abs(i) % 8).asNumericIndex(), Math.abs(i) % 8);
    }
    @Property public void portAlphabeticOf(int i) {
        char c = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H').get(Math.abs(i) % 8);
        assertEquals(Port.of(c), Port.of(c));
        assertEquals(Port.of(c).asAlphabeticIndex(), c);
    }
    @Property public void portStringAlphabeticOf(int i) {
        String s = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H").get(Math.abs(i) % 8);
        assertEquals(Port.of(s), Port.of(s));
        assertEquals(Port.of(s).asAlphabeticIndex(), s.charAt(0));
    }
    @Property public void portPortOf(@From(PortGenerator.class) Port p) {
        assertEquals(Port.of(p), p);
    }
    @Property public void PortDirectionAndSideIndexOf(Direction d, int i) {
        assertEquals(Port.of(d, Math.abs(i) % 2), Port.of(d, Math.abs(i) % 2));
        assertEquals(Port.of(d, Math.abs(i) % 2).getDirection(), d);
        assertEquals(Port.of(d, Math.abs(i) % 2).getSideIndex(), Math.abs(i) % 2);
    }
    @Property public void portIllegalLengthStringAlphabeticOf(String s) {
        if (s.length() != 1) {
            try {
                Port.of(s);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }
    @Property public void portIllegalNumericOf(int i) {
        if (i < 0 || i >= 8) {
            try {
                Port.of(i);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }
    @Property public void portIllegalAlphabeticOf(char c) {
        if (!Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H').contains(c)) {
            try {
                Port.of(c);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }
    @Property public void portIllegalDirectionAndSideIndexOf(Direction d, int i) {
        if (!(d != null && i >= 0 && i <= 1)) {
            try {
                Port.of(d, i);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }
    @Property public void portAll(@From(PortGenerator.class) Port port) {
        assertTrue(Port.all().contains(port));
        assertEquals(8, Port.all().size());
    }
    @Property public void rotateClockwise90Degree(@From(PortGenerator.class) @When(seed = -7230599325571059717L) Port p) {
        assertEquals(90, p.angularDifference(p.rotateClockwise()));
    }
    @Property public void rotateCounterClockwise270Degree(@From(PortGenerator.class) Port p) {
        assertEquals(270, p.angularDifference(p.rotateCounterClockwise()));
    }
    @Property public void equalsHashCode(
            @From(PortGenerator.class) Port port1,
            @From(PortGenerator.class) Port port2) {
        if (port1.equals(port2)) {
            assertEquals(port1.hashCode(), port2.hashCode());
        }
    }
    @Property public void toString(@From(PortGenerator.class) Port port) {
        assertEquals(port.toString().charAt(0), port.asAlphabeticIndex());
    }
    @Property public void equals(@From(PortGenerator.class) Port port, Object obj) {
        if (!(obj instanceof Port)) {
            assertNotEquals(port, obj);
        }
    }
}
