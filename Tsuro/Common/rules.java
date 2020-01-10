import java.util.Collections;
import java.util.List;

public interface IRuleChecker {

    boolean checkInitialPlacement(IRefereeBoard board, IInitialPlacement initialPlacement, List<ITile> choices);

    boolean checkIntermediatePlacement(IRefereeBoard board, Token token, ITile tile, int x, int y, List<ITile> choices);

}

public class RuleChecker implements IRuleChecker {

    @Override
    public boolean checkInitialPlacement(IRefereeBoard board, IInitialPlacement initialPlacement, List<ITile> choices) {
        IRefereeBoard testBoard = board.copy();
        ITile.Port port = initialPlacement.getPort();
        ITile tile = initialPlacement.getTile();
        Token token = initialPlacement.getToken();
        int x = initialPlacement.getX();
        int y = initialPlacement.getY();

        // check if they are null
        if (port == null || tile == null || token == null)
            return false;
        if (choices.size() != 3) {
            return false;
        }
        if (!(choices.contains(tile))) {
            return false;
        }
        if (!BoardUtil.withinBoard(x, y)) {
            return false;
        }
        if (!(BoardUtil.isPeriphery(x, y))) {
            return false;
        }
        // check if the location is valid
        if (testBoard.get(x, y).isPresent()) {
            return false;
        }
        // check if the neighbor occupied
        if (isNeighborOccupied(board, x, y)) {
            return false;
        }
        // check if the port is Periphery
        if (!BoardUtil.isPortPeriphery(x, y, port)) {
            return false;
        }

        ITile considered;
        boolean hasChoice = false;
        for (ITile option : choices) {
            considered = option;
            for (int i = 0; i < 3; i++) {
                testBoard = board.copy();
                IInitialPlacement testInitialPlacement = new InitialPlacement(considered, port, token, x, y);
                testBoard.placeFirsts(Collections.singletonList(testInitialPlacement));
                if (!testBoard.advanceToken(token).didExit()) {
                    hasChoice = true;
                }
                considered = considered.rotate();
            }
        }
        if (hasChoice) {
            testBoard = board.copy();
            IInitialPlacement testInitialPlacement = new InitialPlacement(tile, port, token, x, y);
            testBoard.placeFirsts(Collections.singletonList(testInitialPlacement));
            return !testBoard.advanceToken(token).didExit();
        } else {
            return true;
        }
    }


    @Override
    public boolean checkIntermediatePlacement(IRefereeBoard board, Token token, ITile tile, int x, int y, List<ITile> choices) {
        IRefereeBoard testBoard = board.copy();

        if (tile == null || token == null)
            return false;
        if (choices.size() != 2) {
            return false;
        }
        if (!(choices.contains(tile))) {
            return false;
        }
        if (!BoardUtil.withinBoard(x, y)) {
            return false;
        }
        if (testBoard.get(x, y).isPresent()) {
            return false;
        }
        if (!isNeighborOfToken(board.getLocationOf(token), x, y)){
            return false;
        }

        ITile considered;
        boolean hasChoice = false;
        for (ITile option : choices) {
            considered = option;
            for (int i = 0; i < 2; i++) {
                testBoard = board.copy();
                testBoard.place(tile, x, y);
                if (!testBoard.advanceToken(token).didExit()) {
                    hasChoice = true;
                }
                considered = considered.rotate();
            }
        }
        if (hasChoice) {
            testBoard = board.copy();
            testBoard.place(tile, x, y);
            return !testBoard.advanceToken(token).didExit();
        } else {
            return true;
        }
    }


    private boolean isNeighborOccupied(IRefereeBoard board, int x, int y) {
        if (BoardUtil.withinBoard(x + 1, y))
            if (board.get(x + 1, y).isPresent())
                return true;
        if (BoardUtil.withinBoard(x - 1, y))
            if (board.get(x - 1, y).isPresent())
                return true;
        if (BoardUtil.withinBoard(x, y + 1))
            if (board.get(x, y + 1).isPresent())
                return true;
        if (BoardUtil.withinBoard(x, y - 1))
            if (board.get(x, y - 1).isPresent())
                return true;
        return false;
    }

    private boolean isNeighborOfToken(Board.TokenLocation tokenLocation, int x, int y) {
        if (x == tokenLocation.getX() + 1 && y == tokenLocation.getY()) {
            return true;
        }
        if (x == tokenLocation.getX() && y == tokenLocation.getY() + 1) {
            return true;
        }
        if (x == tokenLocation.getX() - 1 && y == tokenLocation.getY()) {
            return true;
        }
        if (x == tokenLocation.getX() && y == tokenLocation.getY() - 1) {
            return true;
        }
        return false;
    }

}

public class BoardUtil {
    public static boolean withinBoard(int x, int y) {
        return 0 <= x && x < 10 && 0 <= y && y < 10;
    }

    public static boolean isPeriphery(int x, int y) {
        return x == 0 || x == 9 || y == 0 || y == 9;
    }

    public static boolean isPortPeriphery(int x, int y, ITile.Port port) {
        if (!BoardUtil.isPeriphery(x, y)) {
            return false;
        }
        return (x == 0 && port.getDirection().equals(ITile.Port.Direction.WEST)) ||
                (x == 9 && port.getDirection().equals(ITile.Port.Direction.EAST)) ||
                (y == 0 && port.getDirection().equals(ITile.Port.Direction.NORTH)) ||
                (y == 9 && port.getDirection().equals(ITile.Port.Direction.SOUTH));
    }
}