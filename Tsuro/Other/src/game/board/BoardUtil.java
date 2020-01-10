package game.board;

import game.common.Position;
import game.tile.Port;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static game.common.PositionUtil.*;

/**
 * Utility class for board.
 * Used to check if placement is valid.
 */
public class BoardUtil {
    private static final int SIZE = 10;

    /**
     * Check if we place a tile against the wall, return boolean to announce the result.
     */
    public static boolean withinBoardPeriphery(Position position) {
        return withinPeriphery(position, 0, SIZE - 1, 0, SIZE - 1);
    }

    /**
     * Check if we place a tile within the board, return boolean to announce the result.
     */
    public static boolean withinBoardBoundary(Position position) {
        return withinBoundary(position, 0, SIZE - 1, 0, SIZE - 1);
    }

    /**
     * Check if port is against the wall. If the port's neighbour is not in the board, then the port
     * itself is against the wall.
     */
    public static boolean atPeriphery(Position position, Port port) {
        return !withinBoardBoundary(neighborAt(position, port.getDirection()));
    }

    /**
     * Get all locations in x,y for board. (x, y) = ([0-9], [0-9])
     */
    public static Collection<Position> allBoardPosition() {
        Collection<Position> result = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                result.add(Position.of(x, y));
            }
        }
        return result;
    }

    /**
     * Get all x, y locations that against the wall
     * @return      A collection of positions that against the wall.
     */
    public static Collection<Position> allPeripheryPosition() {
        return allBoardPosition().stream().filter(position ->
                position.getX() == 0 ||
                        position.getX() == SIZE - 1 ||
                        position.getX() == 0 ||
                        position.getY() == SIZE - 1).collect(Collectors.toList());
    }
}
