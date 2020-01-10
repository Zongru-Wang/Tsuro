package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.BoardUtil;
import game.board.IntermediatePlacement;
import game.common.Position;
import game.tile.Tile;

public class IntermediatePlacementGenerator extends Generator<IntermediatePlacement> {

    public IntermediatePlacementGenerator() {
        super(IntermediatePlacement.class);
    }

    @Override
    public IntermediatePlacement generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        while (true) {
            Tile tile = sourceOfRandomness.choose(Tile.loadDefaultAll());
            Position position = sourceOfRandomness.choose(BoardUtil.allBoardPosition());
            try {
                return IntermediatePlacement.of(tile, position);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
