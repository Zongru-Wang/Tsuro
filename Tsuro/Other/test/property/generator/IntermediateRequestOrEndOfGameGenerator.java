package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.Board;
import game.remote.EndOfGame;
import game.remote.IntermediateRequest;
import game.remote.IntermediateRequestOrEndOfGame;

import java.util.ArrayList;

public class IntermediateRequestOrEndOfGameGenerator extends Generator<IntermediateRequestOrEndOfGame> {
    public IntermediateRequestOrEndOfGameGenerator() {
        super(IntermediateRequestOrEndOfGame.class);
    }

    @Override
    public IntermediateRequestOrEndOfGame generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        if (sourceOfRandomness.nextBoolean()) {
            IntermediateRequest request = new IntermediateRequestGenerator().generate(sourceOfRandomness, generationStatus);
            return new IntermediateRequestOrEndOfGame(request, null);
        } else {
            Board board = new BoardGenerator().generate(sourceOfRandomness, generationStatus);
            EndOfGame endOfGame = new EndOfGame(new ArrayList<>(), new ArrayList<>(), board); //TODO make better winner losers array generation
            return new IntermediateRequestOrEndOfGame(null, endOfGame);
        }
    }
}
