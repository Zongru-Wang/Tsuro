package testharness.observer;

import com.google.gson.*;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
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
import java.util.Collection;
import java.util.List;

import static testharness.schema.JsonSchema.*;

public class ObserverTestInputExtractor
        extends AbstractJsonDeserializableTestInputExtractor<ObserverTestInput>
        implements TestInputExtractor<ObserverTestInput> {

    @Override
    public ObserverTestInput fromJsonList(JsonArray jsonList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ObserverTestInput.class, new ObserverTestInputExtractor());
        Gson gson = gsonBuilder.create();
        try {
            return gson.fromJson(jsonList, ObserverTestInput.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ObserverTestInput deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonSchema schema = or(bounded(unbounded(or(bounded(tileIndex, rotation, color, port, x, y), bounded(color, tileIndex, rotation, x, y))),
                                       or(bounded(bounded(color, tileIndex, rotation, x, y, port), tileIndex, tileIndex, tileIndex),
                                          bounded(bounded(color, tileIndex, rotation, x, y), tileIndex, tileIndex))),
                               bounded(unbounded(or(bounded(tileIndex, rotation, color, port, x, y), bounded(color, tileIndex, rotation, x, y)))));
        if (!schema.validate(jsonElement)) {
            throw new JsonParseException("json format not correct");
        }
        Board board = Board.empty();
        Token turn = null;
        List<Tile> choices = null;
        InitialPlacement requestedInitialPlacement = null;
        IntermediatePlacement requestedIntermediatePlacement = null;
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
        if (jsonElement.getAsJsonArray().size() == 2) {
            JsonArray request = jsonElement.getAsJsonArray().get(1).getAsJsonArray();
            turn = toToken(request.get(0).getAsJsonArray().get(0).getAsString());
            int tileIndex = request.get(0).getAsJsonArray().get(1).getAsInt();
            int rotation = request.get(0).getAsJsonArray().get(2).getAsInt();
            Tile tile = toTile(tileIndex, rotation);
            int x = request.get(0).getAsJsonArray().get(3).getAsInt();
            int y = request.get(0).getAsJsonArray().get(4).getAsInt();
            Position position = Position.of(x, y);
            choices = new ArrayList<>();
            int choiceIndex1 = request.get(1).getAsInt();
            Tile choice1 = toTile(choiceIndex1, 0);
            int choiceIndex2 = request.get(2).getAsInt();
            Tile choice2 = toTile(choiceIndex2, 0);
            choices.add(choice1);
            choices.add(choice2);
            if (request.size() == 4) {
                int choiceIndex3 = request.get(3).getAsInt();
                Tile choice3 = toTile(choiceIndex3, 0);
                choices.add(choice3);
                String portStr = request.get(0).getAsJsonArray().get(5).getAsString();
                Port port = Port.of(portStr);
                requestedInitialPlacement = InitialPlacement.of(turn, tile, port, position);
            } else {
                requestedIntermediatePlacement = IntermediatePlacement.of(tile, position);
            }
        }
        return new ObserverTestInput(board, requestedInitialPlacement, requestedIntermediatePlacement, turn, choices);
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
