package game.observer.image;


import org.w3c.dom.Element;

/**
 * EmptyVectorImageImpl that using AbstractVectorImageImpl to create an empty VectorImage with given width and height (color)
 */
class EmptyVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {

    // extend the abstract and can create the element with the given width and height
    EmptyVectorImageImpl(int width, int height) {
        super(width, height);
    }

    // create the element with the given width and height and set the background color
    // we needs to add it to the current root element
    EmptyVectorImageImpl(int width, int height, String bgColor) {
        super(width, height);
        Element root = this.asSVGDocument().getRootElement();
        root.appendChild(stylize(rect(0, 0, width, height), bgColor, null, 0));
    }
}
