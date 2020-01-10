package game.observer.image;

import org.w3c.dom.Element;

/**
 * CircleVectorImageImpl that using AbstractVectorImageImpl to create an representation of tokens with given radius and color
 */
class CircleVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {
    // create the token representation with given radius and stylize it with given color
    // we needs to add it to the current root element
    CircleVectorImageImpl(int radius, String fillColor) {
        super(radius * 2, radius * 2);
        Element root = this.asSVGDocument().getRootElement();
        Element circle = this.circle(radius, radius, radius);
        circle = stylize(circle, fillColor, null, 0);
        root.appendChild(circle);
    }
}
