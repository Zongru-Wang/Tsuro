package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.BoardUtil;
import game.board.Token;
import game.board.TokenStatus;
import game.common.PortPosition;
import game.common.Position;
import game.tile.Port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TokenStatusGenerator extends Generator<TokenStatus> {
    public TokenStatusGenerator() {
        super(TokenStatus.class);
    }

    @Override
    public TokenStatus generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Collection<TokenStatus> tokenStatuses = new ArrayList<>();
        Token token = sourceOfRandomness.choose(Token.values());
        Port port = sourceOfRandomness.choose(Port.all());
        Position peripheryPosition = Position.of(sourceOfRandomness.nextInt(0, 9), sourceOfRandomness.nextInt(0, 9));
        Position position = Position.of(sourceOfRandomness.nextInt(0, 9), sourceOfRandomness.nextInt(0, 9));
        while (!BoardUtil.atPeriphery(peripheryPosition, port) ||
                BoardUtil.atPeriphery(position, port)) {
            peripheryPosition = Position.of(sourceOfRandomness.nextInt(0, 9), sourceOfRandomness.nextInt(0, 9));
            position = Position.of(sourceOfRandomness.nextInt(0, 9), sourceOfRandomness.nextInt(0, 9));
        }
        List<Token> possibleCollidedWith = new ArrayList<>(Arrays.asList(Token.values()));
        possibleCollidedWith.remove(token);
        Token collidedWith = sourceOfRandomness.choose(possibleCollidedWith);
        tokenStatuses.add(TokenStatus.exited(token, PortPosition.of(port, peripheryPosition)));
        tokenStatuses.add(TokenStatus.collided(token, PortPosition.of(port, peripheryPosition), collidedWith));
        tokenStatuses.add(TokenStatus.inBoard(token, PortPosition.of(port, position)));
        return sourceOfRandomness.choose(tokenStatuses);
    }
}
