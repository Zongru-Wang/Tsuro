package game.observer;

import game.board.Token;

/**
 * TokenColorUtil that pass the token and get their color representation in svg
 */
public class TokenColorUtil {
    private static String TOKEN_PARSE_ERROR = "Token must be one of \"red\", \"green\", \"blue\", \"white\", \"black\"";
    // get the token's color in svg
    // if there is no such color, throw exception
    public static String toColor(Token token) {
        switch (token) {
            case RED:
                return "#D22";
            case GREEN:
                return "#2D2";
            case BLUE:
                return "#22D";
            case BLACK:
                return "#222";
            case WHITE:
                return "#DDD";
        }
        throw new RuntimeException(TOKEN_PARSE_ERROR);
    }
}
