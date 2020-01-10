package game.tile;

import game.common.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 *  The class represent the ports in tiles. Ports are represented by the numericIndex from 0 - 7.
 *  An port can have a direction, which is enum type, NORTH, WEST, SOUTH AND EAST.
 */
public class Port extends Rotatable<Port> {
    private static final String INVALID_NUMERIC_INDEX = "Invalid numeric index";
    private static final String INVALID_PORT_INDEX = "Invalid port index";
    private final int numericIndex;

    /**
     * Generate new port by the given port, serve as a copy method.
     */
    private Port(Port port) {
        this.numericIndex = port.asNumericIndex();
    }

    /**
     * Generate a port by giving a numericIndex from 0 - 7.
     */
    private Port(int numericIndex) {
        if (numericIndex < 0 || numericIndex > 7)
            throw new IllegalArgumentException(INVALID_NUMERIC_INDEX);
        this.numericIndex = numericIndex;
    }

    /**
     * Generate a port by an alphabeticIndex, from A to H.
     */
    private Port(char alphabeticIndex) {
        this((int) alphabeticIndex - 'A');
    }

    /**
     * Gerate a port by giving a direction and the sideIndex
     * @param direction The direction of the port locates
     * @param sideIndex The Index that beside the port we want to create.
     */
    private Port(Direction direction, int sideIndex) {
        this(Arrays.asList(Direction.values()).indexOf(direction) * 2 + sideIndex);
    }

    /**
     * Return a numericIndex of a port.
     */
    public int asNumericIndex() {
        return this.numericIndex;
    }

    /**
     *Return a alphabeticIndex of a port.
     */
    public char asAlphabeticIndex() {
        return (char) ((int) 'A' + this.numericIndex);
    }

    /**
     * Return the direction of the port locates.
     */
    public Direction getDirection() {
        return Direction.values()[this.numericIndex / 2];
    }

    /**
     * Get the index of the port beside the current port at the same direction.
     */
    public int getSideIndex() {
        return this.numericIndex % 2;
    }

    /**
     * Given the times of 90 degree clockwise rotation
     * @param x        The times of clockwise rotation on object
     * @return         A new port that rotates by given times.
     */
    @Override
    public Port rotateXTimes(int x) {
        return new Port((this.numericIndex + (x * 2)) % 8);
    }

    @Override
    public String toString() {
        return String.valueOf(this.asAlphabeticIndex());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Port) {
            Port other = (Port) obj;
            return this.numericIndex == other.asNumericIndex();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.numericIndex;
    }

    /**
     * Belows of methods are our instantiation methods that make new instances of a port.
     */
    public static Port of(Port port) {
        return new Port(port);
    }

    public static Port of(int numericIndex) {
        return new Port(numericIndex);
    }

    public static Port of(char alphabeticIndex) {
        return new Port(alphabeticIndex);
    }

    public static Port of(String alphabeticIndex) {
        if (alphabeticIndex.length() != 1) {
            throw new IllegalArgumentException(INVALID_PORT_INDEX);
        }
        return new Port(alphabeticIndex.charAt(0));
    }

    public static Port of(Direction direction, int sideIndex) {
        return new Port(direction, sideIndex);
    }

    /**
     * Return a Collection of all the ports.
     */
    public static Collection<Port> all() {
        Collection<Port> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(new Port(i));
        }
        return result;
    }
}
