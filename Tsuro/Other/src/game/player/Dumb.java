package game.player;

import game.board.*;
import game.common.Direction;
import game.common.PortPosition;
import game.common.Position;
import game.rulechecker.RuleChecker;
import game.tile.Port;
import game.tile.Tile;

import java.util.List;
import java.util.Optional;

import static game.common.PositionUtil.neighborAt;

public class Dumb implements Strategy {
    private static final String INVALID_LOCATION_FOR_INITIAL_TILE = "No valid locations to place initial Tile :(";
    private static final String INVALID_CURRENT_LOCATION_GIVEN = "Invalid current location given";
    private static final String LOCATION_SHOULD_BE_ON_BOARDER_OF_BOARD = "Location must be on border of board.";

    @Override
    public InitialPlacement generateInitialPlacement(Board board, List<Tile> availableTiles, Token token, RuleChecker checker) throws RuntimeException {
        Position startLocation = Position.of(0, 0);
        Position bounds = Position.of(10, 10);
        Position location = Position.of(0, 0);

        do {
            InitialPlacement placement = InitialPlacement.of(
                    token,
                    availableTiles.get(availableTiles.size() - 1),
                    getFirstPortForBorderLocation(location),
                    location);
            if (checker.checkInitialPlacement(board, placement, availableTiles)) {
                return placement;
            }
            location = this.getNextClockwiseLocationForBounds(location, bounds);
        } while (location.getX() != startLocation.getX() || location.getX() != startLocation.getY());

        throw new RuntimeException(INVALID_LOCATION_FOR_INITIAL_TILE);
    }

    @Override
    public IntermediatePlacement generateIntermediatePlacement(Board board, List<Tile> availableTiles, Token token, RuleChecker checker) {
        Optional<TokenStatus> tokenStatus = board.getPortPosition(token);
        assert tokenStatus.isPresent();
        PortPosition portPosition = tokenStatus.get().tokenPosition();
        Tile tile = availableTiles.get(0);
        Position location = neighborAt(portPosition.getPosition(), portPosition.getPort().getDirection());

        return IntermediatePlacement.of(tile, location);
    }

    /**
     * Calculates the next location counterclockwise to location based on the given bounds (exclusively).
     * @param current   the current location
     * @param bounds    the bounds of some grid
     * @return          the next location counterclockwise to the given location
     */
    private Position getNextClockwiseLocationForBounds(Position current, Position bounds) {
        int x = current.getX();
        int y = current.getY();
        int maxX = bounds.getX() - 1;
        int maxY = bounds.getY() - 1;

        // 4 cases, one for each side current could be on
        if (x >= 0 && x < maxX && y == 0)           x++;    // top
        else if (x == maxX && y >= 0 && y < maxY)   y++;    // right
        else if (x <= maxX && x > 0 && y == maxY)   x--;    // bottom
        else if (x == 0 && y <= maxY && y > 0)      y--;    // left
        else throw new IllegalArgumentException(INVALID_CURRENT_LOCATION_GIVEN);

        return Position.of(x, y);
    }

    /**
     * Is the given location empty on the given board without any neighbors?
     * @param board     the board we are checking the location of
     * @param location  the location we are checking if we can place at the given location
     * @return boolean true if can place the initial tile at the given location, otherwise false
     */
    /*
    private boolean canPlaceInitialTileAtLocation(Board board, Position location, RuleChecker checker) {
        // keeping code around because it might be helpful, but not calling it because I forgot we could just use the
        // rule checker here (yay!)
        final int x = location.getKey();
        final int y = location.getValue();
        final int maxX = 9;
        final int maxY = 9;

        boolean currentCheck    =                   board.getTile(Position.of(x, y)).isPresent();
        boolean xMinusOneCheck  = (x != 0)      &&  board.getTile(Position.of(x - 1, y)).isPresent();
        boolean xPlusOneCheck   = (x != maxX)   &&  board.getTile(Position.of(x + 1, y)).isPresent();
        boolean yMinusOneCheck  = (y != 0)      &&  board.getTile(Position.of(x, y - 1)).isPresent();
        boolean yPlusOneCheck   = (y != maxY)   &&  board.getTile(Position.of(x, y + 1)).isPresent();

        return !(currentCheck || xMinusOneCheck || xPlusOneCheck || yMinusOneCheck || yPlusOneCheck);
    }
     */

    /**
     * Returns the "first" port at the given border location
     * @param location      the location we are checking
     * @return              the clockwise port (???)
     */
    private Port getFirstPortForBorderLocation(Position location) {
        final int x = location.getX();
        final int y = location.getY();
        final int maxX = 9;
        final int maxY = 9;

        Direction direction;
        if      (x >= 0 && x < maxX && y == 0)      direction = Direction.NORTH;    // top
        else if (x == maxX && y >= 0 && y < maxY)   direction = Direction.EAST;     // right
        else if (x <= maxX && x > 0 && y == maxY)   direction = Direction.SOUTH;    // bottom
        else if (x == 0 && y <= maxY && y > 0)      direction = Direction.WEST;     // left
        else throw new IllegalArgumentException(LOCATION_SHOULD_BE_ON_BOARDER_OF_BOARD);

        return Port.of(direction, 0);
    }
}
