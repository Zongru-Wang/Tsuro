package game.common;

import game.tile.Port;
import javafx.util.Pair;

/**
 * represents a triple of Port, Integer, Integer, extends PortPosition
 */
public class PortPosition extends Pair<Port, Pair<Integer, Integer>> {
    private static final String ILLEGAL_NULL = "argument must be non null";

    private PortPosition(Port port, int x, int y) {
        super(port, Position.of(x, y));
    }

    /**
     * get Port of this PortPosition
     *
     * @return port of this PortPosition
     */
    public Port getPort() {
        return this.getKey();
    }

    /**
     * get x of this PortPosition
     *
     * @return x of this PortPosition
     */
    public int getX() {
        return this.getValue().getKey();
    }

    /**
     * get y of this PortPosition
     *
     * @return y of this PortPosition
     */
    public int getY() {
        return this.getValue().getValue();
    }

    /**
     * get Position of this PortPosition
     *
     * @return Position of this PortPosition
     */
    public Position getPosition() {
        return Position.of(this.getX(), this.getY());
    }

    /**
     * instantiate portPosition with given port, x, y
     *
     * @param port port for the portPosition, must be non null
     * @param x    x for the portPosition
     * @param y    y  for the portPosition
     * @return PortPosition constructed by port, x, y
     */
    public static PortPosition of(Port port, int x, int y) {
        if (port == null) {
            throw new IllegalArgumentException(ILLEGAL_NULL);
        }
        return new PortPosition(port, x, y);
    }

    public static PortPosition of(Port port, Position position) {
        if (port == null || position == null) {
            throw new IllegalArgumentException(ILLEGAL_NULL);
        }
        return new PortPosition(port, position.getX(), position.getY());
    }
}
