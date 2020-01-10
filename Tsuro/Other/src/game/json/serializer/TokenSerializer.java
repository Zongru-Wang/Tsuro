package game.json.serializer;

import com.google.gson.*;
import game.board.Token;

import java.lang.reflect.Type;

/**
 * TokenSerializer that serialize port to json string as one of "red", "green", "blue", "white", "black"
 */
public class TokenSerializer implements JsonSerializer<Token> {
    private static String TOKEN_PARSE_ERROR = "Token must be one of \"red\", \"green\", \"blue\", \"white\", \"black\"";
    /**
     * @param token the token that we want to transfer to json string
     * @param type Token.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return one of "red", "green", "blue", "white", "black"
     */
    @Override
    public JsonElement serialize(Token token, Type type, JsonSerializationContext jsonSerializationContext) {
        switch (token) {
            case RED:
                return new JsonPrimitive("red");
            case GREEN:
                return new JsonPrimitive("green");
            case BLUE:
                return new JsonPrimitive("blue");
            case WHITE:
                return new JsonPrimitive("white");
            case BLACK:
                return new JsonPrimitive("black");
        }
        throw new RuntimeException(TOKEN_PARSE_ERROR);
    }
}
