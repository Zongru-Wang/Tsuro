package game.remote;

import game.board.RefereeBoard;
import game.tile.Tile;

import java.util.HashSet;
import java.util.List;

/**
 * represent the IntermediateRequest game state
 */
public class IntermediateRequest {
    private final RefereeBoard board;
    private final List<Tile> choices;
    public IntermediateRequest(RefereeBoard board, List<Tile> choices) {
        this.board = board;
        this.choices = choices;
    }
    /**
     * get the current game state board
     * @return the current game state board
     */
    public RefereeBoard getBoard() {
        return this.board;
    }

    /**
     * get the current game state choices
     * @return the current game state choices
     */
    public List<Tile> getChoices() {
        return this.choices;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntermediateRequest) {
            IntermediateRequest other = (IntermediateRequest)obj;
            return this.board.equals(other.getBoard()) &&
                    new HashSet<>(this.choices).equals(new HashSet<>(other.getChoices()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.board.hashCode() * new HashSet<>(this.choices).hashCode();
    }
}
