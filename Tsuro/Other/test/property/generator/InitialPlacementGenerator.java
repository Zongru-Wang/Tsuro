package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.BoardUtil;
import game.board.InitialPlacement;
import game.board.Token;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

public class InitialPlacementGenerator extends Generator<InitialPlacement> {

    public InitialPlacementGenerator() {
        super(InitialPlacement.class);
    }

    @Override
    public InitialPlacement generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        while (true) {
            Token token = sourceOfRandomness.choose(Token.values());
            Tile tile = sourceOfRandomness.choose(Tile.loadDefaultAll());
            Port port = sourceOfRandomness.choose(Port.all());
            Position position = sourceOfRandomness.choose(BoardUtil.allPeripheryPosition());
            try {
                return InitialPlacement.of(token, tile, port, position);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
