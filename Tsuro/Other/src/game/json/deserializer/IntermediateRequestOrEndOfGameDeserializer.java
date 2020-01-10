package game.json.deserializer;

import com.google.gson.*;
import game.remote.EndOfGame;
import game.remote.IntermediateRequest;
import game.remote.IntermediateRequestOrEndOfGame;

import java.lang.reflect.Type;

/**
 * IntermediateRequestOrEndOfGameDeserializer that deserialize IntermediateRequestOrEndOfGame json in the
 * either the format IntermediateRequestSerializer serializes or EndOfGameSerializer serializes
 */
public class IntermediateRequestOrEndOfGameDeserializer implements JsonDeserializer<IntermediateRequestOrEndOfGame> {
    private final Gson gson;

    public IntermediateRequestOrEndOfGameDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestDeserializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameDeserializer());
        this.gson = builder.create();
    }

    /**
     * serializes IntermediateRequestOrEndOfGame object to json element.
     *
     * @param jsonElement                json to deserialize as IntermediateRequestOrEndOfGame
     * @param type                       IntermediateRequestOrEndOfGame
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return IntermediateRequestOrEndOfGame that the json encodes
     * @throws JsonParseException when json does not conform to the json format
     */
    @Override
    public IntermediateRequestOrEndOfGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            EndOfGame endOfGame = this.gson.fromJson(jsonElement, EndOfGame.class);
            return new IntermediateRequestOrEndOfGame(null, endOfGame);
        } catch (JsonParseException ignored) {
        }
        IntermediateRequest request = this.gson.fromJson(jsonElement, IntermediateRequest.class);
        return new IntermediateRequestOrEndOfGame(request, null);
    }
}
