package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.tile.Port;
import game.tile.Rotatable;
import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RotatableGenerator extends Generator<Rotatable> {
    public RotatableGenerator() {
        super(Rotatable.class);
    }

    @Override
    public Rotatable generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        if (sourceOfRandomness.nextBoolean()) {
            return Port.of(sourceOfRandomness.nextInt(0 , 7));
        } else {
            return sourceOfRandomness.choose(Tile.loadDefaultAll());
        }
    }
}
