package testharness.rulechecker;

import com.google.gson.*;
import game.board.Board;
import game.board.Token;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;
import testharness.AbstractJsonDeserializableTestInputExtractor;
import testharness.TestInputExtractor;
import testharness.schema.JsonSchema;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static testharness.schema.JsonSchema.*;

public class RuleCheckerTestInputExtractor
        extends AbstractJsonDeserializableTestInputExtractor<RuleCheckerTestInput>
        implements TestInputExtractor<RuleCheckerTestInput> {

    @Override
    public RuleCheckerTestInput fromJsonList(JsonArray jsonList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RuleCheckerTestInput.class, new RuleCheckerTestInputExtractor());
        Gson gson = gsonBuilder.create();
        try {
            return gson.fromJson(jsonList, RuleCheckerTestInput.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public RuleCheckerTestInput deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonSchema schema = bounded(
                unbounded(or(bounded(tileIndex, rotation, color, port, x, y), bounded(color, tileIndex, rotation, x, y))),
                bounded(bounded(color, tileIndex, rotation, x, y), tileIndex, tileIndex));
        if (!schema.validate(jsonElement)) {
            throw new JsonParseException("json format not correct");
        }
        Board board = Board.empty();
        JsonArray placements = jsonElement.getAsJsonArray().get(0).getAsJsonArray();
        for (JsonElement placement: placements) {
            JsonArray placementArray = placement.getAsJsonArray();
            if (placementArray.size() == 6) {
                int tileIndex = placementArray.get(0).getAsInt();
                int rotation = placementArray.get(1).getAsInt();
                Tile tile = this.toTile(tileIndex, rotation);
                String color = placementArray.get(2).getAsString();
                Token token = this.toToken(color);
                String portStr = placementArray.get(3).getAsString();
                Port port = Port.of(portStr);
                int x = placementArray.get(4).getAsInt();
                int y = placementArray.get(5).getAsInt();
                board = board.placeInitial(token, tile, port, Position.of(x, y));
            } else if (placementArray.size() == 5) {
                String color = placementArray.get(0).getAsString();
                Token token = this.toToken(color);
                int tileIndex = placementArray.get(1).getAsInt();
                int rotation = placementArray.get(2).getAsInt();
                Tile tile = this.toTile(tileIndex, rotation);
                int x = placementArray.get(3).getAsInt();
                int y = placementArray.get(4).getAsInt();
                board = board.place(tile, Position.of(x, y));
            } else {
                throw new JsonParseException("placement json malformed");
            }
        }
        JsonArray testArray = jsonElement.getAsJsonArray().get(1).getAsJsonArray();
        String color = testArray.get(0).getAsJsonArray().get(0).getAsString();
        Token token = this.toToken(color);
        int tileIndex = testArray.get(0).getAsJsonArray().get(1).getAsInt();
        int rotation = testArray.get(0).getAsJsonArray().get(2).getAsInt();
        Tile tile = this.toTile(tileIndex, rotation);
        int x = testArray.get(0).getAsJsonArray().get(3).getAsInt();
        int y = testArray.get(0).getAsJsonArray().get(4).getAsInt();
        Position position = Position.of(x, y);
        int tileIndexChoice1 = testArray.get(1).getAsInt();
        int tileIndexChoice2 = testArray.get(2).getAsInt();
        List<Tile> choices = Arrays.asList(this.toTile(tileIndexChoice1, 0), this.toTile(tileIndexChoice2, 0));
        RuleCheckerTestInput input = new RuleCheckerTestInput(board, token, tile, position, choices);
        return input;
    }

    private Token toToken(String color) {
        switch (color) {
            case "white":
                return Token.WHITE;
            case "black":
                return Token.BLACK;
            case "red":
                return Token.RED;
            case "green":
                return Token.GREEN;
            case "blue":
                return Token.BLUE;
            default:
                throw new RuntimeException();
        }
    }

    private Tile toTile(int tileIndex, int rotation) {
        Tile tile;
        try {
            Collection<Tile> tiles = Tile.loadAll("/tile.json");
            tile = new ArrayList<Tile>(tiles).get(tileIndex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        return tile.rotate(rotation);
    }
}
