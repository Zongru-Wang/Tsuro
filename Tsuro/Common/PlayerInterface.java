package game;

import game.board.IInitialPlacement;
import game.board.IIntermediatePlacement;
import game.board.IPlayerBoard;
import game.board.Token;
import game.tile.Tile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an an entity playing a game of Tsuro.
 */
public interface Player {
    /**
     * The String identifying this Player.
     * @return  the string identifying this Player
     */
    String getIdentifier();

    /**
     * The Token that this Player will be using, or empty if this player is not yet a part of a Tsuro Game.
     * @return  this Player's token
     */
    Optional<Token> getToken();

    /**
     * A long representing the age of this Player.
     * @return  this Player's age
     */
    long getAge();

    /**
     * Sets this Player's token to the given Token once they have joined the game.
     * @param token     the Token this Player will be playing with
     * @throws IllegalStateException    if this Player's token has already been set
     */
    void giveToken(Token token) throws IllegalStateException;

    /**
     * Revokes this Player's token when the game is over for them.
     * @throws IllegalStateException    if this Player has not yet been given a Token
     */
    void revokeToken() throws IllegalStateException;

    /**
     * Tells this Player what colored Tokens they are playing against.
     * @param tokens
     */
    void notifyOfOtherPlayers(List<Token> tokens);

    /**
     * Tells this Player that it is their turn to make an initial placement on a Tsuro board.  This player
     * should call the given TurnHandler's takeInitialTurn method when the player's turn has been completed.
     * @param board             the IPlayerBoard this Player is to make its initial placement on
     * @param availableTiles    the possible Tiles this Player can place on the board
     * @param turnHandler       the TurnHandler this player should notify when they are taking their turn
     */
    IInitialPlacement notifyInitialTurn(IPlayerBoard board, List<Tile> availableTiles, TurnHandler turnHandler);

    /**
     * Tells this Player that it is their turn to make an intermediate placement on a Tsuro board.  This player
     * should call the given TurnHandler's takeInitialTurn method when the player's turn has been completed.
     * @param board             the IPlayerBoard this Player is to make its intermediate placement on
     * @param availableTiles    the possible Tiles this Player can place on the board
     * @param turnHandler       the TurnHandler this player should notify when they are taking their turn
     */
    IIntermediatePlacement notifyIntermediateTurn(IPlayerBoard board, List<Tile> availableTiles, TurnHandler turnHandler);

    /**
     * Tells this Player that they have lost the game of Tsuro that they are a part of.
     * @param board     the IPlayerBoard when this Player has lost the game
     */
    void notifyLoss(IPlayerBoard board);

    /**
     * Tells this Player that they have won the game of Tsuro that they are a part of.
     * @param board     the IPlayerBoard when this Player has won the game
     */
    void notifyWin(IPlayerBoard board);

    /**
     * Tells this Player that the game of Tsuro they are a part of has ended
     * @param board
     */
    void notifyEndOfGame(IPlayerBoard board);
}
