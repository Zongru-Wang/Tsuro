package game.json.deserializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.InitialRequest;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * InitialRequestDeserializer that deserialize InitialRequest json in the format [BOARD, [TILE, TILE, TILE]].
 * Board is deserialized according to BoardDeserializer
 * Tile is deserialized according to TileDeserializer
 */
public class InitialRequestDeserializer implements JsonDeserializer<InitialRequest> {
    private final Gson gson;

    public InitialRequestDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new BoardDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        this.gson = builder.create();
    }
    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for initial request";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type InitialRequest.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return InitialRequest object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [BOARD, [TILE, TILE, TILE]]
     */
    @Override
    public InitialRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 2) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        Board board = this.gson.fromJson(jsonElement.getAsJsonArray().get(0), Board.class);
        JsonElement choicesJson = jsonElement.getAsJsonArray().get(1);
        if (!choicesJson.isJsonArray() || choicesJson.getAsJsonArray().size() != 3) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        List<Tile> choices = new ArrayList<>();
        for (JsonElement choice: choicesJson.getAsJsonArray()) {
            choices.add(this.gson.fromJson(choice, Tile.class));
        }
        return new InitialRequest(board, choices);
    }
}
