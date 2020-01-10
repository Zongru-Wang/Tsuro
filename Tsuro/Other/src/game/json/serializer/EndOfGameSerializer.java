package game.json.serializer;

import com.google.gson.*;
import game.board.Board;
import game.remote.EndOfGame;

import java.lang.reflect.Type;
import java.util.List;

/**
 * EndOfGameSerializer that deserialize EndOfGame to json string in the format [[[PLAYER,...],...],[PLAYER,...],BOARD].
 * Player is serialized as String
 * Board is serialized according to BoardSerializer
 */
public class EndOfGameSerializer implements JsonSerializer<EndOfGame> {
    private final Gson gson;
    public EndOfGameSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new BoardSerializer());
        this.gson = builder.create();
    }

    /**
     * @param endOfGame the endOfGame that we want to transfer to json string
     * @param type EndOfGame.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return in the format [[[PLAYER,...],...],[PLAYER,...],BOARD]
     */
    @Override
    public JsonElement serialize(EndOfGame endOfGame, Type type, JsonSerializationContext jsonSerializationContext) {
        List<List<String>> winners = endOfGame.getWinners();
        List<String> losers = endOfGame.getLosers();
        Board board = endOfGame.getBoard();
        JsonArray result = new JsonArray();
        JsonArray winnersJson = new JsonArray();
        for (List<String> winner: winners) {
            JsonArray players = new JsonArray();
            for (String player: winner) {
                players.add(player);
            }
            winnersJson.add(players);
        }
        JsonArray losersJson = new JsonArray();
        for (String player: losers) {
            losersJson.add(player);
        }
        JsonElement boardJson = this.gson.toJsonTree(board, Board.class);
        result.add(winnersJson);
        result.add(losersJson);
        result.add(boardJson);
        return result;
    }
}
