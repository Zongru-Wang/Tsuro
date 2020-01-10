package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.observer.Observer;
import game.observer.ObserverImpl;
import game.observer.image.VectorImage;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.BoardGenerator;
import property.generator.InitialPlacementGenerator;
import property.generator.IntermediatePlacementGenerator;
import property.generator.TileGenerator;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class TestObserverProperty {
    @Property
    public void testRenderWithUpdate(@From(BoardGenerator.class) Board board,
                                     @From(InitialPlacementGenerator.class) InitialPlacement initialPlacement,
                                     @From(IntermediatePlacementGenerator.class) IntermediatePlacement intermediatePlacement,
                                     Token token,
                                     @From(TileGenerator.class) Tile tile1,
                                     @From(TileGenerator.class) Tile tile2,
                                     @From(TileGenerator.class) Tile tile3) {
        Observer<VectorImage> observer = new ObserverImpl();
        observer.update(board);
        VectorImage image = observer.render();
        assertEquals(image.getWidth(), (int) (1000 * 1.5));
        assertEquals(image.getHeight(), (int) (1000 * 1.2));
        observer.update(board, initialPlacement, Arrays.asList(tile1, tile2, tile3), token);
        image = observer.render();
        assertEquals(image.getWidth(), (int) (1000 * 1.5));
        assertEquals(image.getHeight(), (int) (1000 * 1.2));
        observer.update(board, intermediatePlacement, Arrays.asList(tile1, tile2), token);
        image = observer.render();
        assertEquals(image.getWidth(), (int) (1000 * 1.5));
        assertEquals(image.getHeight(), (int) (1000 * 1.2));
    }
}
