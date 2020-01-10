package game.player;

import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.referee.TurnHandler;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.util.List;
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
     * Notifies the player that the game had started.
     */
    void notifyStarted();

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
     * Tells this Player that it is their turn to make an initial placement on a Tsuro board.  This player
     * should call the given TurnHandler's takeInitialTurn method when the player's turn has been completed.
     * @param board             the Board this Player is to make its initial placement on
     * @param availableTiles    the possible Tiles this Player can place on the board
     * @param ruleChecker       the RuleChecker this Player can use to determine if they're following the rules
     */
    InitialPlacement notifyInitialTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker);

    /**
     * Tells this Player that it is their turn to make an intermediate placement on a Tsuro board.  This player
     * should call the given TurnHandler's takeInitialTurn method when the player's turn has been completed.
     * @param board             the Board this Player is to make its intermediate placement on
     * @param availableTiles    the possible Tiles this Player can place on the board
     * @param ruleChecker       the RuleChecker this Player can use to determine if they're following the rules
     */
    IntermediatePlacement notifyIntermediateTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker);

    /**
     * Tells this Player that they have lost the game of Tsuro that they are a part of.
     * @param board     the Board when this Player has lost the game
     */
    void notifyLoss(Board board);

    /**
     * Tells this Player that they have won the game of Tsuro that they are a part of.
     * @param board     the Board when this Player has won the game
     */
    void notifyWin(Board board);


    /**
     * Tells this Player that the game of Tsuro they are a part of has ended
     * @param winners
     * @param losers
     * @param board
     */
    void notifyEndOfGame(List<List<String>> winners, List<String> losers, Board board);
}
