package game.common;

import javafx.util.Pair;

/**
 * Represents a position denoted by x and y. extends Position.
 */
public class Position extends Pair<Integer, Integer> {
    private Position(int x, int y) {
        super(x, y);
    }

    /**
     * get x of this position
     *
     * @return x of this position
     */
    public int getX() {
        return this.getKey();
    }

    /**
     * get y of this position
     *
     * @return y of this position
     */
    public int getY() {
        return this.getValue();
    }

    /**
     * instantiate new Position from x and y
     *
     * @param x x to instantiate Position from
     * @param y y to instantiate Position from
     * @return Position instantiated.
     */
    public static Position of(int x, int y) {
        return new Position(x, y);
    }
}
