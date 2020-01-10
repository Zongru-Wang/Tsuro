package game.referee;

import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.player.Player;
import game.rulechecker.RuleChecker;

/**
 * Represents an entity that is notified when a Player takes their turn in a game of Tsuro.
 */
public interface TurnHandler {
    /**
     * Tells this TurnHandler when the given Player has taken their initial turn.
     * @param player        the Player who has taken their turn
     * @param placement     the IInitialPlacement the given Player is making for their initial turn
     * @return              true if the given IInitialPlacement updated the game board, false otherwise
     */
    boolean takeInitialTurn(Player player, InitialPlacement placement);

    /**
     * Tells this TurnHandler when the given Player has taken their intermediate turn.
     * @param player        the Player who has taken their turn
     * @param placement     the IIntermediatePlacement the given Player is making for their intermediate turn
     * @return              true if the given IIntermediatePlacement updated the game board, false otherwise
     */
    boolean takeIntermediateTurn(Player player, IntermediatePlacement placement);

    /**
     * The IRuleChecker used by this TurnHandler to determine if a given tile placement is valid.
     * @return
     */
    RuleChecker getRuleChecker();
}
