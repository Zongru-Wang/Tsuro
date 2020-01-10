package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.board.Token;
import game.json.serializer.TokenSerializer;

/**
 * Represent the json message of token colors
 */
public class TokenJsonMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final Token token;

    TokenJsonMessageImpl(Token token) {
        this.token = token;
    }

    @Override
    JsonElement toJson() {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.token, Token.class);
    }
}
