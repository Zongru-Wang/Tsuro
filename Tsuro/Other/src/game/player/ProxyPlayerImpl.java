package game.player;

import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.remote.*;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// REFACTORS -- parts of this look very similar to PlayerImpl, just without the use of a strategy, should use that
public class ProxyPlayerImpl implements Player {
    private static final int DEFAULT_BUFFER_CAPACITY = 512;

    private final TsuroSocket socket;
    private final String identifier;
    private final Logger logger;
    private Optional<Token> token = Optional.empty();
    private final long age;

    public ProxyPlayerImpl(TsuroSocket socket, long age, Logger logger) throws IOException {
        this.socket = socket;
        this.logger = logger;
        this.identifier = this.socket.expect(this.logger, "?");
        this.age = age;
    }

    /**
     * The String identifying this Player.
     *
     * @return the string identifying this Player
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * The Token that this Player will be using, or empty if this player is not yet a part of a Tsuro Game.
     *
     * @return this Player's token
     */
    @Override
    public Optional<Token> getToken() {
        return this.token;
    }

    /**
     * A long representing the age of this Player.
     *
     * @return this Player's age
     */
    @Override
    public long getAge() {
        return this.age;
    }

    /**
     * notifies the player that the game had started.
     */
    @Override
    public void notifyStarted() {
        this.socket.send("started", logger, identifier);
    }

    @Override
    public void giveToken(Token token) throws IllegalStateException {
        this.socket.send(Token.class, token, logger, identifier);
        this.token = Optional.of(token);
    }

    @Override
    public void revokeToken() throws IllegalStateException {
        // QUESTION -- does this need to be implemented?
    }

    @Override
    public InitialPlacement notifyInitialTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker) {
        InitialRequest request = new InitialRequest((Board) board, availableTiles);
        try {
            this.socket.send(InitialRequest.class, request, logger, identifier);
            InitialPlacement placement = this.socket.expect(InitialPlacement.class, this.logger, this.identifier);
            return placement;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Dumb().generateInitialPlacement(board, availableTiles, this.getToken().get(), ruleChecker);
    }

    @Override
    public IntermediatePlacement notifyIntermediateTurn(Board board, List<Tile> availableTiles, RuleChecker ruleChecker) {
        IntermediateRequest request = new IntermediateRequest((Board) board, availableTiles);
        try {
            this.socket.send(IntermediateRequest.class, request, this.logger, this.identifier);
            IntermediatePlacement placement = this.socket.expect(IntermediatePlacement.class, this.logger, this.identifier);
            return placement;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Dumb().generateIntermediatePlacement(board, availableTiles, this.getToken().get(), ruleChecker);
    }

    @Override
    public void notifyLoss(Board board) {

    }

    @Override
    public void notifyWin(Board board) {

    }

    @Override
    public void notifyEndOfGame(List<List<String>> winners, List<String> losers, Board board) {
        EndOfGame end = new EndOfGame(winners, losers, (Board) board);
        this.socket.send(EndOfGame.class, end, this.logger, this.identifier);
    }
}
