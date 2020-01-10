package game.referee;

import game.board.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an entity that distributes Tokens before the start of a game of Tsuro.
 */
public class TokenManager {
    List<Token> availableTokens;
    int nextAvailableTokenIndex;

    TokenManager(Token... availableTokens) {
        this.availableTokens = Arrays.asList(availableTokens);
        this.nextAvailableTokenIndex = 0;
    }

    /**
     * Determines the next available Token to give away and returns it.
     * @return                          the next available Token
     * @throws IllegalStateException    if there are no more Tokens to give away
     */
    public Token getNextAvailableToken() throws IllegalStateException {
        if (this.nextAvailableTokenIndex >= this.availableTokens.size()) {
            throw new IllegalStateException("No more available tokens");
        }
        return this.availableTokens.get(this.nextAvailableTokenIndex++);
    }
}
