package game.json.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import game.board.Token;

import java.lang.reflect.Type;

/**
 * TokenDeserializer that deserialize port json as one of "red", "green", "blue", "white", "black"
 */
public class TokenDeserializer implements JsonDeserializer<Token> {
    private static String TOKEN_PARSE_ERROR = "Token must be one of \"red\", \"green\", \"blue\", \"white\", \"black\"";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type Token.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return Token object jsonElement represents
     * @throws JsonParseException when jsonElement is not one of "red", "green", "blue", "white", "black"
     */
    @Override
    public Token deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            String str = jsonElement.getAsString();
            if (str.equals("red"))
                return Token.RED;
            if (str.equals("green"))
                return Token.GREEN;
            if (str.equals("blue"))
                return Token.BLUE;
            if (str.equals("white"))
                return Token.WHITE;
            if (str.equals("black"))
                return Token.BLACK;
        }
        throw new JsonParseException(TOKEN_PARSE_ERROR);
    }
}
