package game.observer.image;

import game.board.Board;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * svg representation of the image, we use this for render function in Observer
 */
public interface VectorImage {
    /**
     * Get the width of the VectorImage
     * @return the width of the VectorImage
     */
    int getWidth();
    /**
     * Get the height of the VectorImage
     * @return the height of the VectorImage
     */
    int getHeight();

    /**
     * combine two VectorImages
     * @param image the image that we want to embed on the current VectorImage
     * @param x x axis of current VectorImage for place the given image
     * @param y y axis of current VectorImage for place the given image
     * @return CombinedVectorImageImpl
     */
    VectorImage embed(VectorImage image, int x, int y);

    /**
     * Use JPEGTranscoder to transfer the VectorImage to JPG BufferedImage
     * @return BufferedImage
     */
    BufferedImage asBufferedImage();

    /**
     * return the current svg document
     * @return the current svg document
     */
    SVGDocument asSVGDocument();

    /**
     * create an EmptyVectorImageImpl with given width and height
     * @param width the width to set
     * @param height the height to set
     * @return EmptyVectorImageImpl
     */
    static VectorImage empty(int width, int height) {
        return new EmptyVectorImageImpl(width, height);
    }

    /**
     * create an EmptyVectorImageImpl with given width and height, background color
     * @param width the width to set
     * @param height the height to set
     * @param bgColor the background color to set
     * @return EmptyVectorImageImpl
     */
    static VectorImage empty(int width, int height, String bgColor) {
        return new EmptyVectorImageImpl(width, height, bgColor);
    }

    /**
     * create an CircleVectorImageImpl with given radius and color we want to fill
     * represent the token on the board
     * @param radius the radius to set
     * @param fillColor the color to fill
     * @return CircleVectorImageImpl
     */
    static VectorImage circle(int radius, String fillColor) {
        return new CircleVectorImageImpl(radius, fillColor);
    }

    /**
     * create TileVectorImageImpl with given tile information and length
     * @param tile the tile we want to draw
     * @param length the length of the tile we want to draw
     * @return TileVectorImageImpl
     */
    static VectorImage tile(Tile tile, int length) {
        return new TileVectorImageImpl(tile, length);
    }

    /**
     * create TileVectorImageImpl with given tile information and length, for this, we want to highlight the frame and the port, path
     * @param tile the tile that we want to draw
     * @param length the length of the tile we want to draw
     * @param frameColor the color of the frame
     * @param portToColorMap the port to highlight
     * @return TileVectorImageImpl
     */
    static VectorImage tile(Tile tile, int length, String frameColor, Map<Port, String> portToColorMap) {
        return new TileVectorImageImpl(tile, length, frameColor, portToColorMap);
    }

    /**
     * create the frame for tile
     * @param length the length of the tile we want to draw
     * @param color the color of the frame
     * @return EmptyTileVectorImageImpl
     */
    static VectorImage emptyTile(int length, String color) {
        return new EmptyTileVectorImageImpl(length, color);
    }

    /**
     * create BoardVectorImageImpl with given board information and length
     * @param board the board that we need to get the information
     * @param length length of the board
     * @return BoardVectorImageImpl
     */
    static VectorImage board(Board board, int length) {
        return new BoardVectorImageImpl(board, length);
    }

    /**
     * create BoardVectorImageImpl with given board information and length, for this, we can highlight the given position
     * @param board the board that we need to get the information
     * @param length length of the board
     * @param highlight the location that we want to highlight
     * @return BoardVectorImageImpl
     */
    static VectorImage board(Board board, int length, Position highlight) {
        return new BoardVectorImageImpl(board, length, highlight);
    }
}
