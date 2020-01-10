package game.observer;

import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.observer.image.VectorImage;
import game.tile.Tile;

import java.util.List;

/**
 * Represents an “observing component” which should get updated by the game system and provide renderings of the current game state.
 */
public interface Observer<T> {
    /**
     * Get the updated state from the given board, the choices for each token, the turn, we can also highlight the requested
     * for IntermediatePlacement only
     * @param board Current board with tiles and tokens' locations information
     * @param requested the placement that we want to highlight
     * @param choices the current tiles that token received
     * @param turn whose turn
     */
    void update(Board board, IntermediatePlacement requested, List<Tile> choices, Token turn);

    /**
     * Get the updated state from the given board, the choices for each token, the turn, we can also highlight the requested
     * for InitialPlacement only
     * @param board Current board with tiles and tokens' locations information
     * @param requested the placement that we want to highlight
     * @param choices the current tiles that token received
     * @param turn whose turn
     */
    void update(Board board, InitialPlacement requested, List<Tile> choices, Token turn);

    /**
     * Get the updated state only from the given board
     * for IntermediatePlacement only
     * @param board Current board with tiles and tokens' locations information
     */
    void update(Board board);

    /**
     * After Observer gets the updated, rendering most updated game state that the Observer received
     * @return T
     */
    T render();
}
