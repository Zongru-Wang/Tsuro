package game.json.serializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.InitialRequest;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.List;

/**
 * InitialRequestSerializer that serialize InitialRequest to json string in the format [BOARD, [TILE, TILE, TILE]].
 * Board is serialized according to BoardSerializer
 * Tile is serialized according to TileSerializer
 */
public class InitialRequestSerializer implements JsonSerializer<InitialRequest> {
    private final Gson gson;
    public InitialRequestSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        builder.registerTypeAdapter(Board.class, new BoardSerializer());
        this.gson = builder.create();
    }

    /**
     * @param initialRequest the initialRequest that we want to transfer to json string
     * @param type InitialRequest.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in the format [BOARD, [TILE, TILE, TILE]]
     */
    @Override
    public JsonElement serialize(InitialRequest initialRequest, Type type, JsonSerializationContext jsonSerializationContext) {
        List<Tile> choices = initialRequest.getChoices();
        Board Board = initialRequest.getBoard();
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
