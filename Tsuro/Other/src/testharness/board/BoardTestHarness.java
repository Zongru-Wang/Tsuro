package testharness.board;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import game.board.*;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.*;

public class BoardTestHarness {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        JsonArray array = null;
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        try {
            array = gson.fromJson(stringBuilder.toString(), JsonArray.class);
            stringBuilder.setLength(0);
            TestInput input = TestInput.from(array);
            TestOutput output = testBoard(input);
            System.out.println(output.asJson().toString());
        } catch (JsonSyntaxException ignored) {
            System.err.println("Not JSON");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Input");
        }
    }

    private static TestOutput testBoard(TestInput input) throws IllegalArgumentException {
        Board board = Board.empty();
        TestOutput result = new TestOutput();
        try {
            for (InitialPlacement initialPlacement: input.getInitialPlacements()) {
                board = board.placeInitial(initialPlacement);
            }
            for (IntermediatePlacement intermediatePlacement: input.getIntermediatePlacements()) {
                board = board.place(intermediatePlacement);
            }
            for (Token token: Token.values()) {
                if (board.getPortPosition(token).isPresent()) {
                    TokenStatus status = board.getPortPosition(token).get();
                    if (status.didCollide()) {
                        assert status.collidedWith().isPresent();
                        result.add(new CollidedTestOutputResponse(token.toString()));
                    } else if (status.didExit()) {
                        result.add(new CollidedTestOutputResponse(token.toString()));
                    } else {
                        Port port = status.tokenPosition().getPort();
                        Position position = status.tokenPosition().getPosition();
                        assert board.getTile(position).isPresent();
                        Tile tile = board.getTile(position).get();
                        result.add(ResultPositionTestOutputResponse.from(token, tile, port, position.getX(), position.getY()));
                    }
                } else {
                    result.add(new NeverPlayedTestOutputResponse(token.toString()));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            throw new IllegalArgumentException();
        }
        return result;
    }

    private static class TileUtil {
        private static HashMap<Integer, Tile> Tiles = null;

        static Port computePortImpl(String port) {
            return Port.of(port);
        }

        static int getRotationFrom(Tile tile) {
            Tile target = Tiles.get(getTileIndexFrom(tile));
            for (int rotation: Arrays.asList(0, 90, 180, 270)) {
                if (target.equals(tile)) {
                    return rotation;
                }
                target = target.rotateClockwise();
            }
            throw new RuntimeException();
        }

        static int getTileIndexFrom(Tile tile) {
            for (Map.Entry<Integer, Tile> entry: Tiles.entrySet()) {
                if (entry.getValue().equals(tile)) {
                    return entry.getKey();
                }
            }
            throw new RuntimeException();
        }

        static String getPortFrom(Port port) {
            return String.valueOf(port.asAlphabeticIndex());
        }
    }

    private static class TestInput {
        private Collection<InitialPlacementInput> initialPlacements;
        private Collection<IntermediatePlacementInput> intermediatePlacements;
        private TestInput(Collection<InitialPlacementInput> initialPlacements,
                          Collection<IntermediatePlacementInput> intermediatePlacements) {
            this.initialPlacements = initialPlacements;
            this.intermediatePlacements = intermediatePlacements;
        }

        public List<InitialPlacement> getInitialPlacements() {
            List<InitialPlacement> result = new ArrayList<>();
            for (InitialPlacementInput input: this.initialPlacements) {
                result.add(input.toInitialPlacement());
            }
            return result;
        }

        public List<IntermediatePlacement>getIntermediatePlacements() {
            List<IntermediatePlacement> result = new ArrayList<>();
            for (IntermediatePlacementInput input: this.intermediatePlacements) {
                result.add(input.toIntermediatePlacement());
            }
            return result;
        }

        static TestInput from(JsonArray array) throws IllegalArgumentException {
            if (array == null) {
                throw new IllegalArgumentException();
            }
            List<InitialPlacementInput> initialPlacements = new ArrayList<>();
            List<IntermediatePlacementInput> intermediatePlacements = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                if (!array.get(i).isJsonArray())
                    throw new IllegalArgumentException();
                JsonArray placementJson = array.get(i).getAsJsonArray();
                if (placementJson.size() == 6) {
                    InitialPlacementInput initialPlacement = InitialPlacementInput.from(placementJson);
                    initialPlacements.add(initialPlacement);
                } else if (placementJson.size() == 5) {
                    IntermediatePlacementInput intermediatePlacement = IntermediatePlacementInput.from(placementJson);
                    intermediatePlacements.add(intermediatePlacement);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return new TestInput(initialPlacements, intermediatePlacements);
        }
    }

    private static abstract class AInput {
        int tileIndex;
        int rotation;
        String color;
        int x;
        int y;
        private AInput(int tileIndex ,int rotation, String color, int x, int y) {
            this.tileIndex = tileIndex;
            this.rotation = rotation;
            this.color = color;
            this.x = x;
            this.y = y;
        }

        public Token getToken() {
            switch (this.color) {
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

        Tile getTile() {
            try {
                Tile tile = new ArrayList<>(Tile.loadAll("tile.json")).get(this.tileIndex);
                for (int i = 0; i < rotation / 90; i ++) {
                    tile = (Tile)tile.rotateClockwise();
                }
                return tile;
            } catch (FileNotFoundException e) {
                throw new RuntimeException();
            }
        }
    }

    private static class InitialPlacementInput extends AInput {
        private String port;

        private InitialPlacementInput(int tileIndex, int rotation, String color, String port, int x, int y) {
            super(tileIndex, rotation, color, x, y);
            this.port = port;
        }

        public Port getPortImpl() {
            return TileUtil.computePortImpl(this.port);
        }

        public InitialPlacement toInitialPlacement() {
            return InitialPlacement.of(this.getToken(), this.getTile(), this.getPortImpl(), Position.of(this.x, this.y));
        }

        static InitialPlacementInput from(JsonArray array) throws IllegalArgumentException {
            if (array == null) {
                throw new IllegalArgumentException();
            }
            if (array.size() != 6)
                throw new IllegalArgumentException();
            for (int i = 0; i < array.size(); i++) {
                if (!array.get(i).isJsonPrimitive())
                    throw new IllegalArgumentException();
            }
            int tileIndex;
            int rotation;
            String color;
            String port;
            int x;
            int y;
            try {
                tileIndex = array.get(0).getAsInt();
                rotation = array.get(1).getAsInt();
                color = array.get(2).getAsString();
                port = array.get(3).getAsString();
                x = array.get(4).getAsInt();
                y = array.get(5).getAsInt();
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
            if (0 > tileIndex || 35 <= tileIndex)
                throw new IllegalArgumentException();
            if (!Arrays.asList(0, 90, 180, 270).contains(rotation))
                throw new IllegalArgumentException();
            if (!Arrays.asList("white", "black", "red", "green", "blue").contains(color))
                throw new IllegalArgumentException();
            if (!Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H").contains(port))
                throw new IllegalArgumentException();
            if (0 > x || 10 <= x)
                throw new IllegalArgumentException();
            if (0 > y || 10 <= y)
                throw new IllegalArgumentException();
            return new InitialPlacementInput(tileIndex, rotation, color, port, x, y);
        }
    }

    private static class IntermediatePlacementInput extends AInput {
        private IntermediatePlacementInput(String color, int tileIndex, int rotation, int x, int y) {
            super(tileIndex, rotation, color, x, y);
        }

        public IntermediatePlacement toIntermediatePlacement() {
            return IntermediatePlacement.of(this.getTile(), Position.of(this.x, this.y));
        }

        static IntermediatePlacementInput from(JsonArray array) throws IllegalArgumentException {
            if (array == null) {
                throw new IllegalArgumentException();
            }
            if (array.size() != 5)
                throw new IllegalArgumentException();
            for (int i = 0; i < array.size(); i++) {
                if (!array.get(i).isJsonPrimitive())
                    throw new IllegalArgumentException();
            }
            String color;
            int tileIndex;
            int rotation;
            int x;
            int y;
            try {
                color = array.get(0).getAsString();
                tileIndex = array.get(1).getAsInt();
                rotation = array.get(2).getAsInt();
                x = array.get(3).getAsInt();
                y = array.get(4).getAsInt();
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
            if (!Arrays.asList("white", "black", "red", "green", "blue").contains(color))
                throw new IllegalArgumentException();
            if (0 > tileIndex || 35 <= tileIndex)
                throw new IllegalArgumentException();
            if (!Arrays.asList(0, 90, 180, 270).contains(rotation))
                throw new IllegalArgumentException();
            if (0 > x || 10 <= x)
                throw new IllegalArgumentException();
            if (0 > y || 10 <= y)
                throw new IllegalArgumentException();
            return new IntermediatePlacementInput(color, tileIndex, rotation, x, y);
        }
    }

    private static class TestOutput {
        private List<TestOutputResponse> responses;

        private TestOutput() {
            responses = new ArrayList<>();
        }

        public JsonArray asJson() {
            JsonArray result = new JsonArray();
            for (TestOutputResponse response: responses) {
                result.add(response.asJson());
            }
            return result;
        }

        public void add(TestOutputResponse response) {
            responses.add(response);
        }
    }

    private interface TestOutputResponse {
        JsonArray asJson();
    }

    private static abstract class ATestOutputResponse {
        protected String color;
        private ATestOutputResponse(String color) {
            this.color = color;
        }

        static String getColorFrom(Token token) {
            switch (token) {
                case WHITE:
                    return "white";
                case BLACK:
                    return "black";
                case RED:
                    return "red";
                case GREEN:
                    return "green";
                case BLUE:
                    return "blue";
                default:
                    throw new RuntimeException();
            }
        }
    }

    private static class ResultPositionTestOutputResponse extends ATestOutputResponse implements TestOutputResponse {
        private int tileIndex;
        private int rotation;
        private String port;
        int x;
        int y;
        private ResultPositionTestOutputResponse(String color, int tileIndex, int rotation, String port, int x, int y) {
            super(color);
            this.tileIndex = tileIndex;
            this.rotation = rotation;
            this.port = port;
            this.x = x;
            this.y = y;
        }

        @Override
        public JsonArray asJson() {
            JsonArray result = new JsonArray();
            result.add(this.color);
            result.add(this.tileIndex);
            result.add(this.rotation);
            result.add(this.port);
            result.add(this.x);
            result.add(this.y);
            return result;
        }

        static ResultPositionTestOutputResponse from(Token token, Tile tile, Port port, int x, int y) {
            String color = ATestOutputResponse.getColorFrom(token);
            int tileIndex = TileUtil.getTileIndexFrom(tile);
            int rotation = TileUtil.getRotationFrom(tile);
            String portStr = TileUtil.getPortFrom(port);
            return new ResultPositionTestOutputResponse(color, tileIndex, rotation, portStr, x, y);
        }
    }

    private static class NeverPlayedTestOutputResponse extends ATestOutputResponse implements TestOutputResponse {
        private NeverPlayedTestOutputResponse(String color) {
            super(color);
        }

        @Override
        public JsonArray asJson() {
            JsonArray result = new JsonArray();
            result.add(this.color);
            result.add(" never played");
            return result;
        }

        static NeverPlayedTestOutputResponse from(Token token) {
            String color = ATestOutputResponse.getColorFrom(token);
            return new NeverPlayedTestOutputResponse(color);
        }
    }

    private static class ExitedTestOutputResponse extends ATestOutputResponse implements TestOutputResponse {
        private ExitedTestOutputResponse(String color) {
            super(color);
        }

        @Override
        public JsonArray asJson() {
            JsonArray result = new JsonArray();
            result.add(this.color);
            result.add(" exited");
            return result;
        }

        static ExitedTestOutputResponse from(Token token) {
            String color = ATestOutputResponse.getColorFrom(token);
            return new ExitedTestOutputResponse(color);
        }
    }

    private static class CollidedTestOutputResponse extends ATestOutputResponse implements TestOutputResponse {
        private CollidedTestOutputResponse(String color) {
            super(color);
        }

        @Override
        public JsonArray asJson() {
            JsonArray result = new JsonArray();
            result.add(this.color);
            result.add(" collided");
            return result;
        }

        static CollidedTestOutputResponse from(Token token) {
            String color = ATestOutputResponse.getColorFrom(token);
            return new CollidedTestOutputResponse(color);
        }
    }
}
