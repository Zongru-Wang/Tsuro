package game.remote;

import java.util.Optional;

/**
 * get the state of the game
 */
public class IntermediateRequestOrEndOfGame {
    private static final String NOT_XOR_NULL_ARGUMENT = "Argument must be given in one of them being null";
    private final IntermediateRequest intermediateRequest;
    private final EndOfGame endOfGame;

    public IntermediateRequestOrEndOfGame(IntermediateRequest intermediateRequest, EndOfGame endOfGame) {
        if ((intermediateRequest != null && endOfGame != null) ||
                (intermediateRequest == null && endOfGame == null)) {
            throw new IllegalArgumentException(NOT_XOR_NULL_ARGUMENT);
        }
        this.intermediateRequest = intermediateRequest;
        this.endOfGame = endOfGame;
    }

    /**
     * get the EndOfGame if this is end of the game
     * @return Optional<EndOfGame>
     */
    public Optional<EndOfGame> getEndOfGame() {
        if (endOfGame == null) {
            return Optional.empty();
        }
        return Optional.of(endOfGame);
    }

    /**
     * get the EndOfGame if this is intermediateRequest
     * @return Optional<IntermediateRequest>
     */
    public Optional<IntermediateRequest> getIntermediateRequest() {
        if (intermediateRequest == null) {
            return Optional.empty();
        }
        return Optional.of(intermediateRequest);
    }

    /**
     * determine whether the obj is equals to this
     *
     * @param obj Object to compare
     * @return whether the obj is equals to this
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntermediateRequestOrEndOfGame) {
            IntermediateRequestOrEndOfGame other = (IntermediateRequestOrEndOfGame) obj;
            return this.getIntermediateRequest().equals(other.getIntermediateRequest()) &&
                    this.getEndOfGame().equals(other.getEndOfGame());
        }
        return false;
    }

    /**
     * compute the hashCode of this
     *
     * @return hashCode of this
     */
    @Override
    public int hashCode() {
        return this.getEndOfGame().hashCode() * this.getIntermediateRequest().hashCode();
    }
}
