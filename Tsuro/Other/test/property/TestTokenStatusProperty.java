package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.BoardUtil;
import game.board.Token;
import game.board.TokenStatus;
import game.common.PortPosition;
import game.common.Position;
import game.tile.Port;
import org.junit.runner.RunWith;
import property.generator.PortGenerator;
import property.generator.TokenStatusGenerator;
import property.generator.ValidPositionGenerator;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestTokenStatusProperty {
    @Property public void collidedThenExited(@From(TokenStatusGenerator.class) TokenStatus t) {
        if (t.didCollide()) {
            assertTrue(t.didExit());
        }
    }

    @Property public void didNotCollideThenDidNotExit(@From(TokenStatusGenerator.class) TokenStatus t) {
        if (!t.didExit()) {
            assertFalse(t.didCollide());
        }
    }

    @Property public void getToken(@From(TokenStatusGenerator.class) TokenStatus t) {
        assertNotNull(t.getToken());
    }

    @Property public void collidedWith(Token t, Token u,
                                       @From(ValidPositionGenerator.class) Position position,
                                       @From(PortGenerator.class) Port port) {
        if (t != u && BoardUtil.atPeriphery(position, port)) {
            assertTrue(TokenStatus.collided(t, PortPosition.of(port, position), u).collidedWith().isPresent());
            assertEquals(TokenStatus.collided(t, PortPosition.of(port, position), u).collidedWith().get(), u);
        }
    }

    @Property public void exited(Token t,
                                 @From(ValidPositionGenerator.class) Position position,
                                 @From(PortGenerator.class) Port port) {
        if (BoardUtil.atPeriphery(position, port)) {
            assertTrue(TokenStatus.exited(t, PortPosition.of(port, position)).didExit());
        }
    }

    @Property public void tokenPosition(Token token,
                                        @From(ValidPositionGenerator.class) Position position,
                                        @From(PortGenerator.class) Port port) {
        if (BoardUtil.atPeriphery(position, port)) {
            assertEquals(TokenStatus.exited(token, PortPosition.of(port, position)).tokenPosition(), PortPosition.of(port, position));
        }
    }

    @Property
    public void nullTokenExited(@From(ValidPositionGenerator.class) Position position,
                                @From(PortGenerator.class) Port port) {
        try {
            TokenStatus.exited(null, PortPosition.of(port, position));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPositionExited(Token token) {
        try {
            TokenStatus.exited(token, null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPairItemExited(Token token) {
        try {
            TokenStatus.exited(token, PortPosition.of(null, null));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullTokenInBoard(@From(ValidPositionGenerator.class) Position position,
                                 @From(PortGenerator.class) Port port) {
        try {
            TokenStatus.inBoard(null, PortPosition.of(port, position));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPositionInBoard(Token token) {
        try {
            TokenStatus.inBoard(token, null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPairItemInBoard(Token token) {
        try {
            TokenStatus.inBoard(token, PortPosition.of(null, null));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullTokenCollided(@From(ValidPositionGenerator.class) Position position,
                                  @From(PortGenerator.class) Port port) {
        try {
            TokenStatus.collided(null, PortPosition.of(port, position), null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPositionCollided(Token token, Token collided) {
        try {
            TokenStatus.collided(token, null, collided);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void nullPairItemCollided(Token token, Token collided) {
        try {
            TokenStatus.collided(token, PortPosition.of(null, null), collided);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
