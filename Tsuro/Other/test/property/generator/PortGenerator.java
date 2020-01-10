package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.tile.Port;

public class PortGenerator extends Generator<Port> {
    public PortGenerator() {
        super(Port.class);
    }

    @Override
    public Port generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return Port.of(sourceOfRandomness.nextInt(0 , 7));
    }
}