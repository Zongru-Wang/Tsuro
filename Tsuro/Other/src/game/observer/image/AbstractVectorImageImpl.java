package game.observer.image;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * AbstractVectorImageImpl that implement the VectorImage, we have this for basic shape svg representation
 * like circle, rectangle, path
 */
abstract class AbstractVectorImageImpl implements VectorImage {
    private final SVGDocument svgDocument;
    private final String svgNS;
    private final int width;
    private final int height;

    // set the root element with the given width and height
    // set up the SVG_NAMESPACE_URI and SVGDocument
    AbstractVectorImageImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        this.svgDocument = (SVGDocument) impl.createDocument(this.svgNS, "svg", null);
        Element root = svgDocument.getRootElement();
        root.setAttributeNS(null, "width", String.valueOf(this.width));
        root.setAttributeNS(null, "height", String.valueOf(this.height));
    }

    // create the circle element and set up position and radius
    // set the Attribute namespace
    Element circle(int cx, int cy, int r) {
        Element circle = this.svgDocument.createElementNS(this.svgNS, "circle");
        circle.setAttributeNS(null, "cx", String.valueOf(cx));
        circle.setAttributeNS(null, "cy", String.valueOf(cy));
        circle.setAttributeNS(null, "r", String.valueOf(r));
        return circle;
    }

    // create the rect element: set up the position and width and height
    // set the Attribute namespace
    Element rect(int x, int y, int width, int height) {
        Element rectangle = this.svgDocument.createElementNS(svgNS, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(x));
        rectangle.setAttributeNS(null, "y", String.valueOf(y));
        rectangle.setAttributeNS(null, "width", String.valueOf(width));
        rectangle.setAttributeNS(null, "height", String.valueOf(height));
        return rectangle;
    }

    // create the path element with given name of port
    // set the Attribute namespace
    Element path(String d) {
        Element path = this.svgDocument.createElementNS(this.svgNS, "path");
        path.setAttributeNS(null, "d", d);
        return path;
    }

    // for filling the color for the given element with the given color, including the stroke color
    // with the given colors and strokeWidth
    Element stylize(Element element, String fillColor, String strokeColor, int strokeWidth) {
        StringBuilder styleBuilder = new StringBuilder();
        if (fillColor != null) {
            styleBuilder.append("fill:");
            styleBuilder.append(fillColor);
            styleBuilder.append(";");
        } else {
            styleBuilder.append("fill:none;");
        }
        if (strokeColor != null) {
            styleBuilder.append("stroke:");
            styleBuilder.append(strokeColor);
            styleBuilder.append(";");
        } else {
            styleBuilder.append("stroke:#000;");
        }
        styleBuilder.append("stroke-width:");
        styleBuilder.append(strokeWidth);
        styleBuilder.append(";");
        return stylize(element, styleBuilder.toString());
    }

    // fill the given element with given style
    Element stylize(Element element, String style) {
        element.setAttributeNS(null, "style", style);
        return element;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public VectorImage embed(VectorImage image, int x, int y) {
        return new CombinedVectorImageImpl(this, x, y, image);
    }

    @Override
    public SVGDocument asSVGDocument() {
        return this.svgDocument;
    }

    @Override
    public BufferedImage asBufferedImage() {
        JPEGTranscoder transcoder = new JPEGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, .8f);
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        try {
            transcoder.transcode(new TranscoderInput(this.asSVGDocument()), new TranscoderOutput(ostream));
        } catch (TranscoderException e) {
            e.printStackTrace();
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(ostream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
