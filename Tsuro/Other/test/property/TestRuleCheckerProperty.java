package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.*;
import game.common.PositionUtil;
import game.rulechecker.RuleChecker;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.BoardGenerator;
import property.generator.InitialPlacementGenerator;
import property.generator.IntermediatePlacementGenerator;
import property.generator.TileGenerator;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestRuleCheckerProperty {
    @Property
    public void testNullBoard(@From(InitialPlacementGenerator.class) InitialPlacement ip,
                              @From(TileGenerator.class) Tile tile1,
                              @From(TileGenerator.class) Tile tile2,
                              @From(TileGenerator.class) Tile tile3) {
        RuleChecker checker = new RuleChecker();
        try {
            checker.checkInitialPlacement(null, ip, Arrays.asList(tile1, tile2, tile3));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property(trials = 1000)
    public void checkInitialPlacement(@From(InitialPlacementGenerator.class) InitialPlacement ip) {
        RuleChecker checker = new RuleChecker();
        if (BoardUtil.atPeriphery(ip.getPosition(), ip.getPort()) &&
                !Board.empty().placeInitial(ip).getPortPosition(ip.getToken()).get().didExit()) {
            assertTrue(checker.checkInitialPlacement(Board.empty(), ip, Arrays.asList(ip.getTile(), ip.getTile(), ip.getTile())));
        } else if (BoardUtil.atPeriphery(ip.getPosition(), ip.getPort()) &&
                Board.empty().placeInitial(InitialPlacement.of(ip.getToken(), ip.getTile().rotate(0), ip.getPort(), ip.getPosition())).getPortPosition(ip.getToken()).get().didExit() &&
                Board.empty().placeInitial(InitialPlacement.of(ip.getToken(), ip.getTile().rotate(90), ip.getPort(), ip.getPosition())).getPortPosition(ip.getToken()).get().didExit() &&
                Board.empty().placeInitial(InitialPlacement.of(ip.getToken(), ip.getTile().rotate(180), ip.getPort(), ip.getPosition())).getPortPosition(ip.getToken()).get().didExit() &&
                Board.empty().placeInitial(InitialPlacement.of(ip.getToken(), ip.getTile().rotate(270), ip.getPort(), ip.getPosition())).getPortPosition(ip.getToken()).get().didExit()) {
            assertTrue(checker.checkInitialPlacement(Board.empty(), ip, Arrays.asList(ip.getTile(), ip.getTile(), ip.getTile())));
        } else {
            assertFalse(checker.checkInitialPlacement(Board.empty(), ip, Arrays.asList(ip.getTile(), ip.getTile(), ip.getTile())));
        }
    }

    @Property(trials = 1000)
    public void checkIntermediatePlacement(@From(BoardGenerator.class) Board board,
                                           @From(IntermediatePlacementGenerator.class) IntermediatePlacement ip,
                                           Token token) {
        RuleChecker checker = new RuleChecker();
        if (!board.isEmpty() &&
                !board.getTile(ip.getPosition()).isPresent() &&
                board.getPortPosition(token).isPresent() &&
                !board.place(ip).getPortPosition(token).get().didExit() &&
                ip.getPosition().equals(PositionUtil.neighborAt(
                        board.getPortPosition(token).get().tokenPosition().getPosition(),
                        board.getPortPosition(token).get().tokenPosition().getPort().getDirection()))) {
            assertTrue(checker.checkIntermediatePlacement(board, token, ip, Arrays.asList(ip.getTile(), ip.getTile())));
        } else if (!board.isEmpty() &&
                !board.getTile(ip.getPosition()).isPresent() &&
                board.getPortPosition(token).isPresent() &&
                board.place(IntermediatePlacement.of(ip.getTile().rotate(0), ip.getPosition())).getPortPosition(token).get().didExit() &&
                board.place(IntermediatePlacement.of(ip.getTile().rotate(90), ip.getPosition())).getPortPosition(token).get().didExit() &&
                board.place(IntermediatePlacement.of(ip.getTile().rotate(180), ip.getPosition())).getPortPosition(token).get().didExit() &&
                board.place(IntermediatePlacement.of(ip.getTile().rotate(270), ip.getPosition())).getPortPosition(token).get().didExit() &&
                ip.getPosition().equals(PositionUtil.neighborAt(
                        board.getPortPosition(token).get().tokenPosition().getPosition(),
                        board.getPortPosition(token).get().tokenPosition().getPort().getDirection()))) {
            assertTrue(checker.checkIntermediatePlacement(board, token, ip, Arrays.asList(ip.getTile(), ip.getTile())));
        } else {
            assertFalse(checker.checkIntermediatePlacement(board, token, ip, Arrays.asList(ip.getTile(), ip.getTile())));
        }
    }

    @Property
    public void wrongAvailableTilesCheckInitialPlacement(@From(InitialPlacementGenerator.class) InitialPlacement ip,
                                                         @From(TileGenerator.class) Tile tile1,
                                                         @From(TileGenerator.class) Tile tile2,
                                                         @From(TileGenerator.class) Tile tile3) {
        RuleChecker checker = new RuleChecker();
        if (BoardUtil.atPeriphery(ip.getPosition(), ip.getPort()) &&
                !Board.empty().placeInitial(ip).getPortPosition(ip.getToken()).get().didExit()) {
            try {
                checker.checkInitialPlacement(Board.empty(), ip, Arrays.asList(tile1, tile2, tile3, ip.getTile()));
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }

    @Property
    public void wrongAvailableTilesCheckIntermediatePlacement(@From(BoardGenerator.class) Board board,
                                                              @From(IntermediatePlacementGenerator.class) IntermediatePlacement ip,
                                                              @From(TileGenerator.class) Tile tile,
                                                              @From(TileGenerator.class) Tile tile2,
                                                              Token token) {
        RuleChecker checker = new RuleChecker();
        if (!board.isEmpty() &&
                !board.getTile(ip.getPosition()).isPresent() &&
                board.getPortPosition(token).isPresent() &&
                !board.place(ip).getPortPosition(token).get().didExit() &&
                ip.getPosition().equals(PositionUtil.neighborAt(
                        board.getPortPosition(token).get().tokenPosition().getPosition(),
                        board.getPortPosition(token).get().tokenPosition().getPort().getDirection()))) {
            try {
                checker.checkIntermediatePlacement(board, token, ip, Arrays.asList(tile, tile2, ip.getTile()));
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }
}
