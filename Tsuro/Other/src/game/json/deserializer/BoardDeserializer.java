package game.json.deserializer;

import com.google.gson.*;
import game.board.Board;
import game.board.Token;
import game.common.PortPosition;
import game.tile.Port;
import game.tile.Tile;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.lang.reflect.Type;

/**
 * BoardDeserializer that deserialize Board json in the format [[[TILE_OPT,...],...]. [[TOKEN, PORT, int, int],...]].
 * Token is deserialized according to TokenDeserializer.
 * Port is deserialized according to PortDeserializer.
 */
public class BoardDeserializer implements JsonDeserializer<Board> {
    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for board";
    private final Gson gson;

    public BoardDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new OptionalTileDeserializer());
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type Board.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return Board object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [[[TILE_OPT,...],...]. [[TOKEN, PORT, int, int],...]]
     */
    @Override
    public Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 2) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        JsonElement boardJson = jsonElement.getAsJsonArray().get(0);
        JsonElement initialTokenPositionJson = jsonElement.getAsJsonArray().get(1);
        if (!boardJson.isJsonArray() || boardJson.getAsJsonArray().size() != 10) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        Tile[][] tiles = new Tile[10][10];
        for (int i = 0; i < 10; i++) {
            JsonElement boardRow = boardJson.getAsJsonArray().get(i);
            if (!boardRow.isJsonArray() || boardRow.getAsJsonArray().size() != 10) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            for (int j = 0; j < 10; j++) {
                JsonElement tileOptJson = boardRow.getAsJsonArray().get(j);
                tiles[i][j] = this.gson.fromJson(tileOptJson, Tile.class);
            }
        }
        if (!initialTokenPositionJson.isJsonArray()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        BidiMap<Token, PortPosition> map = new DualHashBidiMap<>();
        for (JsonElement tokenInitial: initialTokenPositionJson.getAsJsonArray()) {
            if (!tokenInitial.isJsonArray() || tokenInitial.getAsJsonArray().size() != 4) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            Token token = this.gson.fromJson(tokenInitial.getAsJsonArray().get(0), Token.class);
            Port port = this.gson.fromJson(tokenInitial.getAsJsonArray().get(1), Port.class);
            int x = tokenInitial.getAsJsonArray().get(2).getAsInt();
            int y = tokenInitial.getAsJsonArray().get(3).getAsInt();
            map.put(token, PortPosition.of(port, x, y));
        }
        return Board.fromTilesAndTokenMap(tiles, map);
    }
}
