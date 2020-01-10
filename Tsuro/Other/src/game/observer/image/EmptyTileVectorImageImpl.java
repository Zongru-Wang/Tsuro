package game.observer.image;

import org.w3c.dom.Element;

/**
 * EmptyTileVectorImageImpl that using AbstractVectorImageImpl to create an empty tile(tile boarder) with given length (color)
 */
class EmptyTileVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {

    // default color is grey
    final String DEFAULT_COLOR = "#888";

    // create an empty tile(tile boarder) with given length
    EmptyTileVectorImageImpl(int length) {
        super(length, length);
        this.drawFrameRect(DEFAULT_COLOR, length);
    }

    // create an empty tile(tile boarder) with given length and color
    EmptyTileVectorImageImpl(int length, String color) {
        super(length, length);
        this.drawFrameRect(color, length);
    }

    // draw the frame(empty tile with boarder) with given color and length
    // we needs to add it to the current root element
    private void drawFrameRect(String color, int length) {
        Element root = this.asSVGDocument().getRootElement();
        Element frame = this.rect(0, 0, (int)(length * 0.95), (int)(length * 0.95));
        frame = stylize(frame, null, color, length / 20);
        root.appendChild(frame);
    }

}
