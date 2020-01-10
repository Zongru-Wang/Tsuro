package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.common.PortPosition;
import game.common.Position;
import game.common.PositionUtil;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.runner.RunWith;
import property.generator.BoardGenerator;
import property.generator.InitialPlacementGenerator;
import property.generator.IntermediatePlacementGenerator;
import property.generator.ValidPositionGenerator;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestBoardProperty {
    @Property
    public void oneOfEmptyInitializingInitialized(@From(BoardGenerator.class) Board board) {
        if (!board.isEmpty() && !board.isInitializing()) {
            assertTrue(board.hadInitialized());
        } else if (!board.isEmpty() && !board.hadInitialized()) {
            assertTrue(board.isInitializing());
        } else if (!board.isInitializing() && !board.hadInitialized()) {
            assertTrue(board.isEmpty());
        } else {
            fail();
        }
    }

    @Property
    public void ifEmptyNoTile(
            @From(BoardGenerator.class) Board board,
            @From(ValidPositionGenerator.class) Position position) {
        if (board.isEmpty()) {
            assertFalse(board.getTile(position).isPresent());
        }
    }

    @Property
    public void as2DArrayGetTile(
            @From(BoardGenerator.class) Board board,
            @From(ValidPositionGenerator.class) Position position) {
        if (board.getTile(position).isPresent()) {
            assertEquals(board.as2DArray()[position.getX()][position.getY()], board.getTile(position).get());
        }
    }

    @Property
    public void tokenInitialPosition(
            @From(BoardGenerator.class) Board board,
            Token token) {
        assertEquals(board.getInitialPortPosition(token).isPresent(), board.getPortPosition(token).isPresent());
        if (board.getInitialPortPosition(token).isPresent()) {
            assertTrue(board.getTile(board.getInitialPortPosition(token).get().getPosition()).isPresent());
        }
    }

    @Property
    public void equalsHashCode(
            @From(BoardGenerator.class) Board board1,
            @From(BoardGenerator.class) Board board2) {
        if (board1.equals(board2)) {
            assertEquals(board1.hashCode(), board2.hashCode());
        }
    }

    @Property
    public void tokenPosition(@From(BoardGenerator.class) Board b) {
        SimpleGraph<PortPosition, DefaultEdge> graph = b.asGraph();
        ShortestPathAlgorithm<PortPosition, DefaultEdge> algorithm = new DijkstraShortestPath<>(graph);
        for (Token token : Token.values()) {
            if (b.getPortPosition(token).isPresent()) {
                assertTrue(b.getInitialPortPosition(token).isPresent());
                PortPosition initial = b.getInitialPortPosition(token).get();
                PortPosition destination = b.getPortPosition(token).get().tokenPosition();
                assertNotNull(algorithm.getPath(initial, destination));
                Position initialPosition = initial.getPosition();
                Position destinationPosition = destination.getPosition();
                assertTrue(algorithm.getPath(initial, destination).getLength()
                        >= PositionUtil.manhattanDistance(initialPosition, destinationPosition));
            }
        }
    }

    @Property
    public void placeOnEmpty(@From(BoardGenerator.class) Board b,
                             @From(InitialPlacementGenerator.class) InitialPlacement i) {
        if (b.isEmpty()) {
            Board placed = b.placeInitial(i);
            assertTrue(placed.getTile(i.getPosition()).isPresent());
            assertEquals(placed.getTile(i.getPosition()).get(), i.getTile());
            assertTrue(placed.isInitializing());
            assertTrue(placed.getInitialPortPosition(i.getToken()).isPresent());
            assertTrue(placed.getPortPosition(i.getToken()).isPresent());
        }
    }

    @Property
    public void placeOnInitializing(@From(BoardGenerator.class) Board b,
                                    @From(IntermediatePlacementGenerator.class) IntermediatePlacement i) {
        if (b.isInitializing()) {
            try {
                Board placed = b.place(i);
                assertTrue(placed.getTile(i.getPosition()).isPresent());
                assertEquals(placed.getTile(i.getPosition()).get(), i.getTile());
                assertTrue(placed.hadInitialized());
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }

    @Property
    public void empty() {
        assertTrue(Board.empty().isEmpty());
    }

    @Property
    public void initializing(List<@From(InitialPlacementGenerator.class) InitialPlacement> placements) {
        try {
            assertTrue(Board.initializing(placements).isInitializing());
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void initializing(List<@From(InitialPlacementGenerator.class) InitialPlacement> initialPlacements,
                             List<@From(IntermediatePlacementGenerator.class) IntermediatePlacement> intermediatePlacements) {
        try {
            assertTrue(Board.initialized(initialPlacements, intermediatePlacements).hadInitialized());
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Property
    public void emptyPlace(@From(BoardGenerator.class) Board board, @From(IntermediatePlacementGenerator.class) IntermediatePlacement placement) {
        if (board.isEmpty()) {
            try {
                board.place(placement);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalStateException);
            }
        }
    }

    @Property
    public void hadInitializedPlaceInitial(@From(BoardGenerator.class) Board board, @From(InitialPlacementGenerator.class) InitialPlacement placement) {
        if (board.hadInitialized()) {
            try {
                board.placeInitial(placement);
                fail();
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException || e instanceof IllegalStateException);
            }
        }
    }

    @Property
    public void toString(@From(BoardGenerator.class) Board board) {
        assertEquals(board.toString(), board.asGraph().toString());
    }
}
