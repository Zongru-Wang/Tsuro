package game.rulechecker;

import game.board.*;
import game.common.Direction;
import game.common.PortPosition;
import game.common.Position;
import game.common.PositionUtil;
import game.tile.Port;
import game.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static game.board.BoardUtil.atPeriphery;
import static game.board.BoardUtil.withinBoardBoundary;
import static game.common.PositionUtil.areDirectNeighbor;
import static game.common.PositionUtil.neighborAt;

/**
 * Represents a third party that determines the legality of a given move.
 * The information needed to determine a move depends on the phase of the game and is passed into this interface's methods when they are called.
 * Implementing classes are free to decide what counts as a legal or illegal move.
 */
public class RuleChecker {
    private static final String ILLEGAL_NULL_ARGUMENT = "Illegal null argument given";
    private static final String INVALID_POSITION = "Invalid position";

    // the rulechecker for InitialPlacement based on the state of game
    // no suicide allowed
    public boolean checkInitialPlacement(Board board, InitialPlacement initialPlacement, List<Tile> choices) {
        // check if the given board/InitialPlacement/choices is/are null
        if (board == null || initialPlacement == null || choices == null) {
            throw new IllegalArgumentException(ILLEGAL_NULL_ARGUMENT);
        }
        Port port = initialPlacement.getPort();
        Tile tile = initialPlacement.getTile();
        Token token = initialPlacement.getToken();
        Position position = initialPlacement.getPosition();

        // after get the port, tile, token, position from initialPlacement,
        // we want to check if they are null
        if (port == null || tile == null || token == null) return false;
        // the choices for InitialPlacement have to be 3
        if (choices.size() != 3) return false;

        // it is common check for both InitialPlacement and IntermediatePlacement
        // check if the tile is one of the choices, if the position is within board, if the location is valid
        if (commonChecker(tile, position, choices, board)) return false;

        // check if the neighbor occupied
        if (isNeighborOccupied(board, position)) return false;

        // check if the port is Periphery
        if (!atPeriphery(position, port)) return false;

        // the following is checking if the tile placement may not cause the player’s suicide
        // unless this is the only possible option, based on the supplied choices
        boolean hasChoice = hasChoice(choices, considered -> board.placeInitial(InitialPlacement.of(token, considered, port, position)), token);
        // if there is a choice, then check if the initialPlacement lead it suicide
        if (hasChoice) {
            Board testBoard = board.placeInitial(initialPlacement);
            return this.isLegal(testBoard, token);
        } else {
            return true;
        }
    }

    // the rulechecker for InitialPlacement based on the state of game
    // no suicide allowed
    public boolean checkIntermediatePlacement(Board board, Token token, IntermediatePlacement intermediatePlacement, List<Tile> choices) {
        Tile tile = intermediatePlacement.getTile();
        Position position = intermediatePlacement.getPosition();
        // check if the tile and token from the IntermediatePlacement are null
        if (tile == null || token == null) return false;
        // the choices for IntermediatePlacement have to be 2
        if (choices.size() != 2) return false;
        // it is common check for both InitialPlacement and IntermediatePlacement
        // check if the tile is one of the choices, if the position is within board, if the location is valid
        if (commonChecker(tile, position, choices, board)) return false;

        // check if the token is on the InitialPortPosition
        if (!board.getInitialPortPosition(token).isPresent()) return false;
        // get the token position
        PortPosition tokenPosition = board.getPortPosition(token).get().tokenPosition();
        // make sure that tile is placed next to the token position
        if (!position.equals(PositionUtil.neighborAt(tokenPosition.getPosition(), tokenPosition.getKey().getDirection())))
            return false;
        // check the port position of token is token or not
        if (board.getPortPosition(token).isPresent()) {
            TokenStatus status = board.getPortPosition(token).get();
            if (!status.didExit()) {
                if (!areDirectNeighbor(status.tokenPosition().getPosition(), position)) {
                    return false;
                }
            }
        }

        // the following is checking if the tile placement may not cause the player’s suicide
        // unless this is the only possible option, based on the supplied choices
        boolean hasChoice = hasChoice(choices, considered -> board.place(IntermediatePlacement.of(considered, position)), token);
        // if there is a choice, then check if the initialPlacement lead it suicide
        if (hasChoice) {
            Board testBoard = board.place(intermediatePlacement);
            return this.isLegal(testBoard, token);
        } else {
            return true;
        }
    }

    // the function to check if the neighbor of the position on the board is occupied
    // since it would be used for both initialPlacement and intermediatePlacement, we made this as help function
    private boolean isNeighborOccupied(Board board, Position position) {
        for (Direction direction : Direction.values()) {
            Position neighbor = neighborAt(position, direction);
            if (withinBoardBoundary(neighbor)) {
                if (board.getTile(neighbor).isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }

    // it is common check for both InitialPlacement and IntermediatePlacement
    // check if the tile is one of the choices, if the position is within board, if the location is valid
    private boolean commonChecker(Tile tile, Position position, List<Tile> choices, Board board) {
        // since we want to check if the tile work for all rotation
        List<Tile> rotationInsensitive = new ArrayList<>();
        for (Tile choice : choices) {
            for (int i = 0; i < 360; i += 90) {
                rotationInsensitive.add(choice.rotate(i));
            }
        }
        // check if the tile is one of the choices
        if (!(rotationInsensitive.contains(tile))) return true;
        // check if the position is within board
        if (!(withinBoardBoundary(position))) return true;
        // check if the location is valid
        return board.getTile(position).isPresent();
    }

    // check if there is any choice make the user do not suicide
    private boolean hasChoice(List<Tile> choices, Function<Tile, Board> place, Token token) {
        Tile considered;
        for (Tile option : choices) {
            considered = option;
            for (int i = 0; i < 4; i++) {
                try {
                    Board testBoard = place.apply(considered);
                    if (testBoard.getPortPosition(token).isPresent()) {
                        TokenStatus status = testBoard.getPortPosition(token).get();
                        if (!status.didExit()) {
                            return true;
                        }
                    } else {
                        throw new RuntimeException(INVALID_POSITION);
                    }
                    considered = considered.rotateClockwise();
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return false;
    }

    // if there is a choice, then check if the Placement is legal
    private boolean isLegal(Board testBoard, Token token) {
        if (testBoard.getPortPosition(token).isPresent()) {
            TokenStatus status = testBoard.getPortPosition(token).get();
            return !status.didExit();
        } else {
            throw new RuntimeException(INVALID_POSITION);
        }
    }

}