package game.board;

import game.common.Position;
import game.common.PositionUtil;
import game.tile.Tile;

/**
 * The class represent the intermediate placements.
 * intermediate placements, the placements must within the board.
 */
public class IntermediatePlacement {
    private final Tile tile;
    private final Position position;

    private static final String INVALID_INPUTS = "Invalid Inputs";
    private static final String NOT_WITHIN_BOARD = "Position is not within Board";

    /**
     * Make an intermediate placement from a tile and position,
     * Throws exceptions if the tile or position is null, and if the placement is out of the board.
     */
    private IntermediatePlacement(Tile tile, Position position) {
        if (tile == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        if (!PositionUtil.withinBoundary(position, 0, 9, 0, 9))
            throw new IllegalArgumentException(NOT_WITHIN_BOARD);
        this.tile = tile;
        this.position = position;

    }

    public Tile getTile() {
        return this.tile;
    }

    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntermediatePlacement) {
            IntermediatePlacement other = (IntermediatePlacement) obj;
            return this.tile.equals(other.getTile()) &&
                    this.position.equals(other.getPosition());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.tile.hashCode() * this.position.hashCode();
    }

    /**
     * Instantiation method to make a new instance of intermediate placements.
     */
    public static IntermediatePlacement of(Tile tile, Position position) {
        return new IntermediatePlacement(tile, position);
    }
}
