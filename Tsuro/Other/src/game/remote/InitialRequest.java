package game.remote;

import game.board.Board;
import game.tile.Tile;

import java.util.HashSet;
import java.util.List;

/**
 * represent the InitialRequest game state
 */
public class InitialRequest {
    private final Board board;
    private final List<Tile> choices;

    public InitialRequest(Board board, List<Tile> choices) {
        this.board = board;
        this.choices = choices;
    }

    /**
     * get the current game state board
     * @return the current game state board
     */
    public Board getBoard() {
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
        if (obj instanceof InitialRequest) {
            InitialRequest other = (InitialRequest)obj;
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
