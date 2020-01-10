package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.Board;
import game.remote.InitialRequest;
import game.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class InitialRequestGenerator extends Generator<InitialRequest> {
    public InitialRequestGenerator() {
        super(InitialRequest.class);
    }

    @Override
    public InitialRequest generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Board board = new BoardGenerator().generate(sourceOfRandomness, generationStatus);
        List<Tile> choices = new ArrayList<>();
        choices.add(new TileGenerator().generate(sourceOfRandomness, generationStatus));
        choices.add(new TileGenerator().generate(sourceOfRandomness, generationStatus));
        choices.add(new TileGenerator().generate(sourceOfRandomness, generationStatus));
        return new InitialRequest(board, choices);
    }
}
