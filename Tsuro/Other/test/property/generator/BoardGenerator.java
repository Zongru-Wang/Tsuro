package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.Board;
import game.board.Token;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static game.common.Direction.*;

public class BoardGenerator extends Generator<Board> {

    public BoardGenerator() {
        super(Board.class);
    }

    @Override
    public Board generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Board board = Board.empty();
        List<Token> tokens = new ArrayList<>(Arrays.asList(Token.values()));
        Collections.shuffle(tokens);
        if (sourceOfRandomness.nextBoolean()) {
            for (int i = 0; i < sourceOfRandomness.nextInt(1, 5); i++) {
                try {
                    Token token = tokens.get(i);
                    Tile tile = getRandomTile(sourceOfRandomness);
                    Position position = getRandomPeripheryPosition(sourceOfRandomness);
                    Port port = getRandomPeripheryPort(sourceOfRandomness, position);
                    board = board.placeInitial(token, tile, port, position);
                } catch (IllegalArgumentException e) {
                    i--;
                }
            }
            if (sourceOfRandomness.nextBoolean()) {
                for (int i = 0; i < sourceOfRandomness.nextInt(1, 94); i++) {
                    try {
                        Tile tile = getRandomTile(sourceOfRandomness);
                        Position position = getRandomBoardWithinPosition(sourceOfRandomness);
                        board = board.place(tile, position);
                    } catch (IllegalArgumentException e) {
                        i--;
                    }
                }
            }
        }
        return board;
    }

    private Tile getRandomTile(SourceOfRandomness r) {
        try {
            return new ArrayList<>(Tile.loadAll("/tile.json")).get(r.nextInt(0, 34));
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private Port getRandomPeripheryPort(SourceOfRandomness r, Position position) {
        List<Port> availablePorts = new ArrayList<>();
        if (position.getX() == 0) {
            availablePorts.add(Port.of(WEST, 0));
            availablePorts.add(Port.of(WEST, 1));
        }
        if (position.getX() == 9) {
            availablePorts.add(Port.of(EAST, 0));
            availablePorts.add(Port.of(EAST, 1));
        }
        if (position.getY() == 0) {
            availablePorts.add(Port.of(NORTH, 0));
            availablePorts.add(Port.of(NORTH, 1));
        }
        if (position.getY() == 9) {
            availablePorts.add(Port.of(NORTH, 0));
            availablePorts.add(Port.of(NORTH, 1));
        }
        return r.choose(availablePorts);
    }

    private Position getRandomBoardWithinPosition(SourceOfRandomness r) {
        int x = r.nextInt(0, 9);
        int y = r.nextInt(0, 9);
        return Position.of(x, y);
    }

    private Position getRandomPeripheryPosition(SourceOfRandomness r) {
        int randomCoordinate = r.nextInt(0, 9);
        if (r.nextBoolean()) {
            if (r.nextBoolean()) {
                return Position.of(0, randomCoordinate);
            } else {
                return Position.of(9, randomCoordinate);
            }
        } else {
            if (r.nextBoolean()) {
                return Position.of(randomCoordinate, 0);
            } else {
                return Position.of(randomCoordinate, 0);
            }
        }
    }
}
