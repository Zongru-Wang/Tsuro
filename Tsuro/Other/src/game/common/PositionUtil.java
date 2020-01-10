package game.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Utility class for Position
 */
public class PositionUtil {
    private static final String INVALID_DIRECTION = "Invalid direction";

    /**
     * Get the positions that diagonal to the given position
     * @param pos  The current position
     * @return     A collection of  positions that diagonal to the current position
     */
    public static Collection<Position> diagonalNeighbors(Position pos) {
        Collection<Position> result = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) {
                    result.add(Position.of(pos.getX() + i, pos.getY() + j));
                }
            }
        }
        return result;
    }

    /**
     * Get the positions nearby against the given position.
     * @param pos  The current position
     * @return     The positions nearby against the given position.
     */
    public static Collection<Position> directNeighbors(Position pos) {
        Collection<Position> result = new ArrayList<>();
        result.add(Position.of(pos.getX() + 1, pos.getY()));
        result.add(Position.of(pos.getX() - 1, pos.getY()));
        result.add(Position.of(pos.getX(), pos.getY() + 1));
        result.add(Position.of(pos.getX(), pos.getY() - 1));
        return result;
    }

    /**
     * Give a position and max bound for x-axis and y y-axis and check if the given position is out of bound
     */
    public static boolean withinBoundary(Position pos, int xMin, int xMax, int yMin, int yMax) {
        return xMin <= pos.getX() && xMax >= pos.getX() &&
                yMin <= pos.getY() && yMax >= pos.getY();
    }

    /**
     * Give a position and max bound for x-axis and y y-axis
     * And check if the given position is at the bound(against the wall)
     */
    public static boolean withinPeriphery(Position pos, int xMin, int xMax, int yMin, int yMax) {
        return xMin == pos.getX() || xMax == pos.getX() ||
                yMin == pos.getY() || yMax >= pos.getY();
    }

    /**
     * Get the distance between two position.
     */
    public static double euclideanDistance(Position pos1, Position pos2) {
        int xDiff = Math.abs(pos1.getX() - pos2.getX());
        int yDiff = Math.abs(pos1.getY() - pos2.getY());
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Return a position that against the given direction of given location.
     */
    public static Position neighborAt(Position pos, Direction direction) {
        switch (direction) {
            case NORTH:
                return Position.of(pos.getX(), pos.getY() - 1);
            case EAST:
                return Position.of(pos.getX() + 1, pos.getY());
            case SOUTH:
                return Position.of(pos.getX(), pos.getY() + 1);
            case WEST:
                return Position.of(pos.getX() - 1, pos.getY());
            default:
                throw new NullPointerException(INVALID_DIRECTION);
        }
    }

    /**
     * Check the difference between the X-s values and Y-s values from two positions.
     */
    public static int manhattanDistance(Position pos1, Position pos2) {
        return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY());
    }

    /**
     * Check if two positions are neighbor.
     */
    public static boolean areDirectNeighbor(Position pos1, Position pos2) {
        return manhattanDistance(pos1, pos2) == 1;
    }
}
