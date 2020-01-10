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

public class PlayerImpl implements Player {
    private static final String NO_NEGATIVE_AGE = "No player can have a negative age.";
    private static final String ALREADY_HAS_TOKEN = "This Player already has a Token assigned to it";
    private String identifier;
    private Optional<Token> token;
    private long age;
    private Strategy strategy;

    public PlayerImpl(String identifier, long age, Strategy strategy) {
        if (age < 0) {
            throw new IllegalStateException(NO_NEGATIVE_AGE);
        }

        this.identifier = identifier;
        this.token = Optional.empty();
        this.age = age;
        this.strategy = strategy;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Optional<Token> getToken() {
        return this.token;
    }

    @Override
    public long getAge() {
        return this.age;
    }

    /**
     * Notifies the player that the game had started.
     */
    @Override
    public void notifyStarted() {
        // Offline players don't need to know when the game has started, just when it is their turn to play
    }

    @Override
    public void giveToken(Token token) throws IllegalStateException {
        if (this.token.isPresent()) {
            throw new IllegalStateException(ALREADY_HAS_TOKEN);
        }
        this.token = Optional.of(token);
    }

    @Override
    public void revokeToken() throws IllegalStateException {
        if (!this.token.isPresent()) {
            throw new IllegalStateException("Cannot revoke a token that hasn't first been given");
        }
        this.token = Optional.empty();
    }

    @Override
    public InitialPlacement notifyInitialTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker) {
        // Assuming token has been assigned, since this should only be called during a game.
        return this.strategy.generateInitialPlacement(board, availableTiles, this.token.get(), ruleChecker);
    }

    @Override
    public IntermediatePlacement notifyIntermediateTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker) {
        // Assuming token has been assigned, since this should only be called during a game.
        return this.strategy.generateIntermediatePlacement(board, availableTiles, this.token.get(), ruleChecker);
    }

    @Override
    public void notifyLoss(Board board) {
        // Offline players don't need to know if they've lost
    }

    @Override
    public void notifyWin(Board board) {
        // Offline players don't need to know if they've won
    }

    @Override
    public void notifyEndOfGame(List<List<String>> winners, List<String> losers, Board board) {
        // Offline players don't need to know when the game has ended
    }
}
