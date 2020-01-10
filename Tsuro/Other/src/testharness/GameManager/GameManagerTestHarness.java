package testharness.GameManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import game.board.Board;
import game.board.Token;
import game.board.TokenStatus;
import game.common.Position;
import game.json.deserializer.BoardDeserializer;
import game.json.serializer.BoardSerializer;
import game.observer.ObserverImpl;
import game.observer.image.VectorImage;
import game.player.Player;
import game.player.PlayerImpl;
import game.player.Strategy;
import game.referee.GameManager;
import game.tile.Tile;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class GameManagerTestHarness {
    // TODO -- put into proper files
    private static final List<String> TEST_INPUT = Arrays.asList(
            "[\"koissi\"]",
            "[\"koissi\", \"jerry\"]",
            "[\"koissi\", \"jerry\", \"lucas\"]",
            "[\"lucas\", \"jerry\", \"zongru\", \"koissi\"]",
            "[\"zongru\", \"lucas\", \"koissi\", \"jerry\", \"awesome-o\"]"
    );
    private static final String globalStrategyIdentifier = "dumb";

    public static void main(String[] args) {
        runHarness(TEST_INPUT.get(4), "dumb", "testResources/jsonBoardHistoryDumb.json");
        runHarness(TEST_INPUT.get(4), "second", "testResources/jsonBoardHistorySecond.json");
    }

    private static void runHarness(String playersJson, String strategyIdentifier, String historyPath) {
        GameManager gm = new GameManager();
        BoardLogObserver observer = new BoardLogObserver();
//        JsonLogObserver observer = new JsonLogObserver();
        gm.addObserver(observer);
        runGameManagerTestWithInput(gm, playersJson, strategyIdentifier);
        observer.compareLogs(historyPath);
    }

    private static void runGameManagerTestWithInput(GameManager gm, String jsonInput, String strategyIdentifier) {
        Gson gson = new Gson();
        List<Player> players = unparsedJsonStringArrayToPlayerList(gson, jsonInput, strategyIdentifier);
        addPlayersToGame(gm, players);
        gm.startGame();
        // assuming game is over -- will have to change once multi-threading is a thing
        String result = new GameManagerTestResult(gm).toJsonString(gson);
        System.out.println(result);
    }

    private static void addPlayersToGame(GameManager gm, List<Player> players) {
        for (Player player : players) {
            gm.addPlayer(player);
        }
    }

    private static List<Player> unparsedJsonStringArrayToPlayerList(Gson gson, String unparsedStringArray, String strategyIdentifier) {
        JsonArray inputAsJsonArray = gson.fromJson(unparsedStringArray, JsonArray.class);
        List<Player> players = new ArrayList<>();

        long age = 5;
        for (JsonElement element : inputAsJsonArray) {
            String playerName = element.getAsString();
            players.add(new PlayerImpl(playerName, age, Strategy.fromString(strategyIdentifier)));
            age -= 1;
        }

        return players;
    }

    // TODO -- use lucas' test harness stuff
    static class GameManagerTestResult {
        List<List<String>> winners;
        List<String> losers;

        GameManagerTestResult(List<List<String>> winners, List<String> losers) {
            this.winners = winners;
            this.losers = losers;
        }

        GameManagerTestResult(GameManager gameManager) {
            this(gameManager.getWinnerIdentifiers(), gameManager.getEjectedIdentifiers());
        }

        String toJsonString(Gson gson) {
            return gson.toJson(this);
        }
    }

    static class ImageObserver extends ObserverImpl {
        private int round = 0;

        @Override
        public void update(Board board) {
            super.update(board);
            VectorImage image = this.render();
            try {
                OutputStream out = new FileOutputStream("gmTests/" + this.round + "-gm-test.jpg");
                ImageIO.write(image.asBufferedImage(), "JPG", out);
                this.round++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class JsonLogObserver extends ObserverImpl {
        private List<String> log;
        private Gson gson;

        public JsonLogObserver() {
            this.log = new ArrayList<>();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Board.class, new BoardSerializer());
            builder.registerTypeAdapter(Board.class, new BoardDeserializer());
            this.gson = builder.create();
        }

        @Override
        public void update(Board board) {
            super.update(board);
            this.log.add(this.gson.toJson(board));
            System.out.println(this.log.get(this.log.size() - 1));
        }
    }

    static class BoardLogObserver extends ObserverImpl {
        private List<Board> boardLog;
        private Gson gson;

        BoardLogObserver() {
            this.boardLog = new ArrayList<>();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Board.class, new BoardSerializer());
            builder.registerTypeAdapter(Board.class, new BoardDeserializer());
            this.gson = builder.create();
        }

        @Override
        public void update(Board board) {
            super.update(board);
            this.boardLog.add(board);
        }

        void compareLogs(String otherLogPath) {
            try {
                List<Board> otherLog = Files.lines(Paths.get(otherLogPath))
                        .map(json -> this.gson.fromJson(json, Board.class))
                        .collect(Collectors.toList());
                /*
                if (this.boardLog.size() != otherLog.size()) {
                    this.findBoardDifferences(this.boardLog, otherLog);
                    return;
                }
                 */
                boolean failed = !this.findBoardDifferences(this.boardLog, otherLog);

                /*
                boolean failed = false;
                for (int boardIndex = 0; boardIndex < this.boardLog.size(); boardIndex++) {
                    Board thisBoard = this.boardLog.get(boardIndex);
                    Board thatBoard = otherLog.get(boardIndex);
                    if (!thisBoard.equals(thatBoard)) {
                        System.out.println("Difference in logs at line index " + boardIndex);
                        System.out.println("this: " + this.gson.toJson(thisBoard));
                        System.out.println("that: " + this.gson.toJson(thatBoard));
                        failed = true;
                    }
                }
                 */

                if (!failed) {
                    System.out.println("Tests passed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean findBoardDifferences(List<Board> thisLog, List<Board> thatLog) {
            int minSize = Math.min(thisLog.size(), thatLog.size());
            for (int boardIndex = 0; boardIndex < minSize; boardIndex++) {
                Board thisBoard = thisLog.get(boardIndex);
                Board thatBoard = thatLog.get(boardIndex);
                if (!thisBoard.equals(thatBoard)) {
                    return this.findTileDifferences(thisBoard, thatBoard) || this.findTokenDifferences(thisBoard, thatBoard);
                }
            }
            return true;
        }

        private boolean findTileDifferences(Board thisBoard, Board thatBoard) {
            final int boardSize = 10;
            boolean foundDifference = false;
            for (int yIndex = 0; yIndex < boardSize; yIndex++) {
                for (int xIndex = 0; xIndex < boardSize; xIndex++) {
                    Pair position = Position.of(xIndex, yIndex);
                    Optional<Tile> thisTileOpt = thisBoard.getTile(Position.of(xIndex, yIndex));
                    Optional<Tile> thatTileOpt = thatBoard.getTile(Position.of(xIndex, yIndex));
                    if (!thisTileOpt.equals(thatTileOpt)) {
                        System.out.println("Difference at tile position " + position);
                        System.out.println("this: " + thisTileOpt.orElse(null));
                        System.out.println("that: " + thatTileOpt.orElse(null));
                        foundDifference = true;
                    }
                }
            }
            return foundDifference;
        }

        private boolean findTokenDifferences(Board thisBoard, Board thatBoard) {
            boolean foundDifference = false;
            for (Token token : Token.values()) {
                Optional<TokenStatus> thisTokenStatusOpt = thisBoard.getPortPosition(token);
                Optional<TokenStatus> thatTokenStatusOpt = thatBoard.getPortPosition(token);
                if (!thisTokenStatusOpt.equals(thatTokenStatusOpt)) {
                    System.out.println("Different status for token " + token);
                    System.out.println("this: " + thisTokenStatusOpt.orElse(null));
                    System.out.println("that: " + thatTokenStatusOpt.orElse(null));
                    foundDifference = true;
                }
            }
            return foundDifference;
        }
    }
}
