package game.json.serializer;

import com.google.gson.*;
import game.remote.EndOfGame;
import game.remote.IntermediateRequest;
import game.remote.IntermediateRequestOrEndOfGame;

import java.lang.reflect.Type;

/**
 * IntermediateRequestOrEndOfGameSerializer that serialize IntermediateRequestOrEndOfGame in the
 * either the format IntermediateRequestSerializer serializes or EndOfGameSerializer serializes
 */
public class IntermediateRequestOrEndOfGameSerializer implements JsonSerializer<IntermediateRequestOrEndOfGame> {
    private final Gson gson;

    public IntermediateRequestOrEndOfGameSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestSerializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        this.gson = builder.create();
    }

    /**
     * serialize an object as json format either IntermediateRequestSerializer or EndOfGameSerializer serialize
     *
     * @param intermediateRequestOrEndOfGame IntermediateRequestOrEndOfGame to serialize
     * @param type                           IntermediateRequestOrEndOfGame.class
     * @param jsonSerializationContext       default jsonSerializationContext
     * @return JsonElement that represents IntermediateRequestOrEndOfGame
     */
    @Override
    public JsonElement serialize(IntermediateRequestOrEndOfGame intermediateRequestOrEndOfGame, Type type, JsonSerializationContext jsonSerializationContext) {
        if (intermediateRequestOrEndOfGame.getEndOfGame().isPresent()) {
            return this.gson.toJsonTree(intermediateRequestOrEndOfGame.getEndOfGame().get(), EndOfGame.class);
        } else {
            return this.gson.toJsonTree(intermediateRequestOrEndOfGame.getIntermediateRequest().get(), IntermediateRequest.class);
        }
    }
}
