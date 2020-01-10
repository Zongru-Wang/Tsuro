package game.remote.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import game.json.serializer.EndOfGameSerializer;
import game.remote.EndOfGame;

/**
 * Represent json message of game end
 */
public class EndedMessageImpl extends AbstractJsonMessageImpl implements Message {
    private final EndOfGame endOfGame;

    public EndedMessageImpl(EndOfGame endOfGame) {
        this.endOfGame = endOfGame;
    }

    @Override
    JsonElement toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this.endOfGame, EndOfGame.class);
    }
}
