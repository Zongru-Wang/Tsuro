package testharness.rulechecker;

import game.board.Board;
import game.board.Token;
import game.common.Position;
import game.tile.Tile;

import java.util.List;

public class RuleCheckerTestInput {
    public final Board gameState;
    public final Token token;
    public final Tile tile;
    public final Position position;
    public final List<Tile> choices;

    public RuleCheckerTestInput(Board gameState, Token token, Tile tile, Position position, List<Tile> choices) {
        this.gameState = gameState;
        this.token = token;
        this.tile = tile;
        this.position = position;
        this.choices = choices;
    }
}
