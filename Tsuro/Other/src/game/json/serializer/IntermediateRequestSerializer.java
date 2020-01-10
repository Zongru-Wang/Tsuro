package game.json.serializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.IntermediateRequest;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.List;

/**
 * IntermediateRequestSerializer that serialize IntermediateRequest to json string in the format [BOARD, [TILE, TILE]].
 * Board is serialized according to BoardSerializer
 * Tile is serialized according to TileSerializer
 */
public class IntermediateRequestSerializer implements JsonSerializer<IntermediateRequest> {
    private final Gson gson;
    public IntermediateRequestSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        builder.registerTypeAdapter(Board.class, new BoardSerializer());
        this.gson = builder.create();
    }

    /**
     * @param intermediateRequest the intermediateRequest that we want to transfer to json string
     * @param type IntermediateRequest.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in the format [BOARD, [TILE, TILE]]
     */
    @Override
    public JsonElement serialize(IntermediateRequest intermediateRequest, Type type, JsonSerializationContext jsonSerializationContext) {
        List<Tile> choices = intermediateRequest.getChoices();
        Board Board = intermediateRequest.getBoard();
        JsonArray result = new JsonArray();
        JsonArray choicesJson = new JsonArray();
        for (Tile tile: choices) {
            choicesJson.add(this.gson.toJsonTree(tile, Tile.class));
        }
        result.add(this.gson.toJsonTree(Board, Board.class));
        result.add(choicesJson);
        return result;
    }
}
