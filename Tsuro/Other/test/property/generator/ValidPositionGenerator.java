package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.common.Position;

public class ValidPositionGenerator extends Generator<Position> {
    public ValidPositionGenerator() {
        super(Position.class);
    }

    @Override
    public Position generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return Position.of(sourceOfRandomness.nextInt(0, 9), sourceOfRandomness.nextInt(0, 9));
    }
}
