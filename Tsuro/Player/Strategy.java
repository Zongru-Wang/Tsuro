package game.player;

import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.PlayerBoard;
import game.board.Token;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.util.List;

public interface Strategy {
    static final String ILLEGAL_STRATEGY_NAME = "Illegal Strategy name must be one of: \"dumb\" or \"second\"";
    /**
     * Generates an IInitialPlacement using the given availableTiles and board.
     * @param board             the board on which to make the IInitialPlacement
     * @param availableTiles    the Tiles this Strategy can use to generate their placement
     * @param token             the Token to be placed in the returned IInitialPlacement
     * @return                  some IInitialPlacement
     */
    InitialPlacement generateInitialPlacement(PlayerBoard board, List<Tile> availableTiles, Token token, RuleChecker checker);

    /**
     * Generates an IIntermediatePlacement using the given availableTiles and board.
     * @param board             the board on which to make the IIntermediatePlacement
     * @param availableTiles    the Tiles this Strategy can use to generate their placement
     * @param token             the Token to be placed in the returned IIntermediatePlacement
     * @return                  some IIntermediatePlacement
     */
    IntermediatePlacement generateIntermediatePlacement(PlayerBoard board, List<Tile> availableTiles, Token token, RuleChecker checker);

    static Strategy fromString(String strategyName) {
        if (strategyName.toLowerCase().equals("dumb")) {
            return new Dumb();
        } else if (strategyName.toLowerCase().equals("second")) {
            return new Second();
        } else {
            throw new IllegalArgumentException(ILLEGAL_STRATEGY_NAME);
        }
    }
}
