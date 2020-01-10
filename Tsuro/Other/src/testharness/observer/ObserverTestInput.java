package testharness.observer;

import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.tile.Tile;

import java.util.List;

public class ObserverTestInput {
    public final Board gameState;
    public final InitialPlacement initialPlacement;
    public final IntermediatePlacement intermediatePlacement;
    public final Token turn;
    public final List<Tile> choices;

    public ObserverTestInput(Board gameState,
                             InitialPlacement initialPlacement,
                             IntermediatePlacement intermediatePlacement,
                             Token turn,
                             List<Tile> choices) {
        this.gameState = gameState;
        this.initialPlacement = initialPlacement;
        this.intermediatePlacement = intermediatePlacement;
        this.turn = turn;
        this.choices = choices;
    }
}
