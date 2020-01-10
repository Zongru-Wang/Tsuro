package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TileGenerator extends Generator<Tile> {

    public TileGenerator() {
        super(Tile.class);
    }

    @Override
    public Tile generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return sourceOfRandomness.choose(Tile.loadDefaultAll());
    }
}
