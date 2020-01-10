package game.json.deserializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.IntermediateRequest;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * IntermediateRequestDeserializer that deserialize IntermediateRequest json in the format [BOARD, [TILE, TILE]].
 * Board is deserialized according to BoardDeserializer
 * Tile is deserialized according to TileDeserializer
 */
public class IntermediateRequestDeserializer implements JsonDeserializer<IntermediateRequest> {
    private final Gson gson;

    public IntermediateRequestDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new BoardDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        this.gson = builder.create();
    }
    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for intermediate request";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type IntermediateRequest.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return IntermediateRequest object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [BOARD, [TILE, TILE]]
     */
    @Override
    public IntermediateRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 2) { //TODO CODE DUPLICATION with initial request deserializer
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        Board board = this.gson.fromJson(jsonElement.getAsJsonArray().get(0), Board.class);
        JsonElement choicesJson = jsonElement.getAsJsonArray().get(1);
        if (!choicesJson.isJsonArray() || choicesJson.getAsJsonArray().size() != 2) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        List<Tile> choices = new ArrayList<>();
        for (JsonElement choice: choicesJson.getAsJsonArray()) {
            choices.add(this.gson.fromJson(choice, Tile.class));
        }
        return new IntermediateRequest(board, choices);
    }
}
