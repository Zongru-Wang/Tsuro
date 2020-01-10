package game.json.serializer;

import com.google.gson.*;
import game.board.Board;
import game.board.Token;
import game.common.PortPosition;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * BoardSerializer that serialize Board to json string in the format [[[TILE_OPT,...],...]. [[TOKEN, PORT, int, int],...]].
 * Token is serialized according to TokenSerializer.
 * Port is serialized according to PortSerializer.
 */
public class BoardSerializer implements JsonSerializer<Board> {
    private final Gson gson;

    public BoardSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        builder.registerTypeAdapter(Port.class, new PortSerializer());
        builder.registerTypeAdapter(Token.class, new TokenSerializer());
        this.gson = builder.create();
    }

    /**
     * @param Board the Board that we want to be in json string
     * @param type Board.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return the form of [[[TILE_OPT,...],...]. [[TOKEN, PORT, int, int],...]]
     */
    @Override
    public JsonElement serialize(Board Board, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray tilesJson = new JsonArray();
        for (int i = 0; i < 10; i++) {
            tilesJson.add(new JsonArray());
            for (int j =0; j < 10; j++) {
                Optional<Tile> tile = Board.getTile(Position.of(i, j));
                if (tile.isPresent()) {
                    tilesJson.get(i).getAsJsonArray().add(this.gson.toJsonTree(tile.get(), Tile.class));
                } else {
                    tilesJson.get(i).getAsJsonArray().add(JsonNull.INSTANCE);
                }
            }
        }
        JsonArray mapJson = new JsonArray();
        for (Token token: Token.values()) {
            Optional<PortPosition> initialPosition = Board.getInitialPortPosition(token);
            if (initialPosition.isPresent()) {
                JsonArray initialPositionJson = new JsonArray();
                initialPositionJson.add(this.gson.toJsonTree(token, Token.class));
                initialPositionJson.add(this.gson.toJsonTree(initialPosition.get().getKey(), Port.class));
                initialPositionJson.add(initialPosition.get().getValue().getKey());
                initialPositionJson.add(initialPosition.get().getValue().getValue());
                mapJson.add(initialPositionJson);
            }
        }
        JsonArray result = new JsonArray();
        result.add(tilesJson);
        result.add(mapJson);
        return result;
    }
}
