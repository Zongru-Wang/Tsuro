package game.board;

import game.common.PortPosition;

import java.util.Optional;

/**
 * Represent the status of a token.
 * Can check if a token exited the board/collided
 */
public class TokenStatus {
    private final Token token;
    private final Token collided;
    private final PortPosition position;

    private static final String INVALID_INPUTS = "Invalid Inputs";
    private static final String INVALID_TOKEN_POSITION = "Invalid token position";
    private static final String NOT_AT_PERIPHERY = "Port is not at Periphery";

    /**
     * Make a token status that represent the status of the token
     */
    private TokenStatus(Token token, Token collided, PortPosition position) {
        if (token == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        this.token = token;
        this.collided = collided;
        this.position = position;
    }

    /**
     * Return a boolean shows if the token exit the board.
     */
    public boolean didExit() {
        return BoardUtil.atPeriphery(position.getPosition(), position.getPort()) || didCollide();
    }

    /**
     * Return a boolean shows if the token collided
     */
    public boolean didCollide() {
        return collided != null;
    }

    public PortPosition tokenPosition() {
        return this.position;
    }

    public Token getToken() {
        return this.token;
    }

    /**
     * Get the token that collided with
     */
    public Optional<Token> collidedWith() {
        if (collided == null) {
            return Optional.empty();
        }
        return Optional.of(collided);
    }

    /**
     * Represent a exited status of a token
     */
    public static TokenStatus exited(Token token, PortPosition position) {
        if (token == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        if (!BoardUtil.atPeriphery(position.getPosition(), position.getPort()))
            throw new IllegalArgumentException(NOT_AT_PERIPHERY);
        return new TokenStatus(token, null, position);
    }

    /**
     * Represent a collided status of a token
     */
    public static TokenStatus collided(Token token, PortPosition position, Token collidedWith) {
        if (token == null || collidedWith == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        if (!BoardUtil.atPeriphery(position.getPosition(), position.getPort()))
            throw new IllegalArgumentException(NOT_AT_PERIPHERY);
        return new TokenStatus(token, collidedWith, position);
    }

    /**
     * Represent a in-Board status of a token
     */
    public static TokenStatus inBoard(Token token, PortPosition position) {
        if (token == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        if (BoardUtil.atPeriphery(position.getPosition(), position.getPort()))
            throw new IllegalArgumentException(NOT_AT_PERIPHERY);
        return new TokenStatus(token, null, position);
    }
}
