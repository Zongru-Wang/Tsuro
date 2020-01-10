package game.common;

/**
 * The enum class that represent the Direction of a port locate in a tile.
 */
public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    private static final String INVALID_DIRECTION = "Invalid direction";

    /**
     * Get the opposite direction of the current direction.
     */
    public Direction opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                throw new RuntimeException(INVALID_DIRECTION);
        }
    }
}
