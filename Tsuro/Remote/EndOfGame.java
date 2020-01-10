package game.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.board.RefereeBoard;
import game.json.serializer.EndOfGameSerializer;

import java.util.List;

/**
 * Represent the end of the game state
 */
public class EndOfGame {
    private final List<List<String>> winners;
    private final List<String> losers;
    private final RefereeBoard board;
    public EndOfGame(List<List<String>> winners, List<String> losers, RefereeBoard board) {
        this.winners = winners;
        this.losers = losers;
        this.board = board;
    }

    /**
     * Get the winners
     * @return the winners in List<List<String>>: winners with the order
     */
    public List<List<String>> getWinners() {
        return winners;
    }

    /**
     * Get the losers
     * @return the losers in <List<String>
     */
    public List<String> getLosers() {
        return losers;
    }

    /**
     * the current board
     * @return the current board
     */
    public RefereeBoard getBoard() {
        return board;
    }

    /**
     * get the json string of the current state
     * @return json string of the current state
     */
    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        Gson gson = builder.create();
        return gson.toJsonTree(this).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EndOfGame) {
            EndOfGame other = (EndOfGame)obj;
            return this.winners.equals(other.winners) &&
                    this.losers.equals(other.losers) &&
                    this.board.equals(other.board);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.board.hashCode() * this.winners.hashCode() * this.losers.hashCode();
    }
}
