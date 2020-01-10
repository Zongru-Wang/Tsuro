package game.remote;

import java.util.Optional;

/**
 * get the state of the game
 */
public class IntermediateRequestOrEndOfGame {
    private final Optional<IntermediateRequest> intermediateRequest;
    private final Optional<EndOfGame> endOfGame;
    IntermediateRequestOrEndOfGame(Optional<IntermediateRequest> intermediateRequest, Optional<EndOfGame> endOfGame) {
        this.intermediateRequest = intermediateRequest;
        this.endOfGame = endOfGame;
    }

    /**
     * get the EndOfGame if this is end of the game
     * @return Optional<EndOfGame>
     */
    public Optional<EndOfGame> getEndOfGame() {
        return endOfGame;
    }

    /**
     * get the EndOfGame if this is intermediateRequest
     * @return Optional<IntermediateRequest>
     */
    public Optional<IntermediateRequest> getIntermediateRequest() {
        return intermediateRequest;
    }
}
