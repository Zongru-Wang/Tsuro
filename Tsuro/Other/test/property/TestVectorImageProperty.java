package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.Board;
import game.common.Position;
import game.observer.image.VectorImage;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.BoardGenerator;
import property.generator.TileGenerator;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class TestVectorImageProperty {
    @Property(trials = 1)
    public void testWidthHeightEmptyVectorImage(int width, int height) {
        VectorImage image = VectorImage.empty(width, height);
        assertEquals(image.getHeight(), height);
        assertEquals(image.getWidth(), width);
        VectorImage withHighlight = VectorImage.empty(width, height, "#FFFFFF");
        assertEquals(withHighlight.getHeight(), height);
        assertEquals(withHighlight.getWidth(), width);
    }

    @Property(trials = 1)
    public void testWidthHeightCircleVectorImage(int radius) {
        VectorImage image = VectorImage.circle(radius, "#FFFFFF");
        assertEquals(image.getHeight(), radius * 2);
        assertEquals(image.getWidth(), radius * 2);
    }

    @Property(trials = 1)
    public void testWidthHeightBoardVectorImage(@From(BoardGenerator.class) Board board,
                                                int length) {
        VectorImage image = VectorImage.board(board, length);
        assertEquals(image.getWidth(), length);
        assertEquals(image.getHeight(), length);
        VectorImage withHighlight = VectorImage.board(board, length, Position.of(0, 0));
        assertEquals(withHighlight.getWidth(), length);
        assertEquals(withHighlight.getHeight(), length);
    }

    @Property(trials = 1)
    public void testWidthHeightCombinedVectorImage(@From(BoardGenerator.class) Board board,
                                                   int width,
                                                   int height,
                                                   int length,
                                                   int xOffSet,
                                                   int yOffSet) {
        VectorImage image = VectorImage.empty(width, height).embed(VectorImage.board(board, length), xOffSet, yOffSet);
        assertEquals(image.getHeight(), height);
        assertEquals(image.getWidth(), width);
    }

    @Property(trials = 1)
    public void testWidthHeightEmptyTileVectorImage(int length) {
        VectorImage image = VectorImage.emptyTile(length, "#FFFFFF");
        assertEquals(image.getWidth(), length);
        assertEquals(image.getWidth(), length);
    }

    @Property(trials = 1)
    public void testWidthHeightTileVectorImage(@From(TileGenerator.class) Tile tile,
                                               int length) {
        VectorImage image = VectorImage.tile(tile, length);
        assertEquals(image.getWidth(), length);
        assertEquals(image.getHeight(), length);
        VectorImage withHighlight = VectorImage.tile(tile, length, "#FFFFFF", new HashMap<>());
        assertEquals(withHighlight.getWidth(), length);
        assertEquals(withHighlight.getHeight(), length);
    }

    @Property(trials = 1)
    public void testEmbedBoardVectorImage(@From(BoardGenerator.class) Board board,
                                          int length) {
        VectorImage image = VectorImage.board(board, length).embed(VectorImage.empty(length, length), 0, 0);
        assertEquals(image.getWidth(), length);
        assertEquals(image.getHeight(), length);
    }

    @Property(trials = 1)
    public void testBufferedBoardVectorImage(@From(BoardGenerator.class) Board board,
                                             @InRange(minInt = 1, maxInt = 10000) int length) {
        BufferedImage image = VectorImage.board(board, length).asBufferedImage();
        assertEquals(image.getWidth(), length);
        assertEquals(image.getHeight(), length);
    }
}
