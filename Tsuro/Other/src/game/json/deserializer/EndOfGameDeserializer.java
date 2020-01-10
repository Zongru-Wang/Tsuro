package game.json.deserializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.EndOfGame;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * EndOfGameDeserializer that deserialize EndOfGame json in the format [[[PLAYER,...],...],[PLAYER,...],BOARD].
 * Player is deserialized as String
 * Board is deserialized according to BoardDeserializer
 */
public class EndOfGameDeserializer implements JsonDeserializer<EndOfGame> {

    private final Gson gson;

    public EndOfGameDeserializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new BoardDeserializer());
        this.gson = builder.create();
    }

    private static final String ILLEGAL_JSON_FORMAT = "Illegal json format for end of game";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type EndOfGame.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return EndOfGame object jsonElement represents
     * @throws JsonParseException when jsonElement does not conform to the form of [[[PLAYER,...],...],[PLAYER,...],BOARD]
     */
    @Override
    public EndOfGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonArray() || jsonElement.getAsJsonArray().size() != 3) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        List<List<String>> winners = new ArrayList<>();
        JsonElement winnersJson = jsonElement.getAsJsonArray().get(0);
        if (!winnersJson.isJsonArray()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        for (JsonElement winnerJson: winnersJson.getAsJsonArray()) {
            List<String> winner = new ArrayList<>();
            if (!winnerJson.isJsonArray()) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            for (JsonElement playerJson: winnerJson.getAsJsonArray()) {
                winner.add(playerJson.getAsString());
            }
            winners.add(winner);
        }
        List<String> losers = new ArrayList<>();
        JsonElement losersJson = jsonElement.getAsJsonArray().get(1);
        if (!losersJson.isJsonArray()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        for (JsonElement playerJson: losersJson.getAsJsonArray()) {
            losers.add(playerJson.getAsString());
        }
        JsonElement boardJson = jsonElement.getAsJsonArray().get(2);
        Board board = this.gson.fromJson(boardJson, Board.class);
        return new EndOfGame(winners, losers, board);
    }
}
