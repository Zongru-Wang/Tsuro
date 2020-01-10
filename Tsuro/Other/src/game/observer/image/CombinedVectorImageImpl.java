package game.observer.image;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * CombinedVectorImageImpl that combines the the given VectorImage to the current base VectorImage
 * with given location on the current VectorImage
 */
class CombinedVectorImageImpl extends AbstractVectorImageImpl implements VectorImage{
    // combines the the given VectorImage to the current base VectorImage
    // with given location on the current VectorImage
    CombinedVectorImageImpl(VectorImage base, int xOffset, int yOffset, VectorImage embedded) {
        super(base.getWidth(), base.getHeight());
        Element baseRoot = base.asSVGDocument().getRootElement();
        Element embeddedRoot = embedded.asSVGDocument().getRootElement();
        // make a copy of the current base VectorImage
        for (int i = 0; i < baseRoot.getChildNodes().getLength(); i++) {
            Node copy = this.asSVGDocument().importNode(baseRoot.getChildNodes().item(i), true);
            this.asSVGDocument().getRootElement().appendChild(copy);
        }
        // for each child element of the embeddedRoot element
        // we add to the root element svg document
        for (int i = 0; i < embeddedRoot.getChildNodes().getLength(); i++) {
            Node currentNode = embeddedRoot.getChildNodes().item(i);
            NamedNodeMap attributes = currentNode.getAttributes();
            String style = attributes.getNamedItem("style").getNodeValue();
            switch(currentNode.getNodeName()) {
                case "rect":
                    int x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue()) + xOffset;
                    int y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue()) + yOffset;
                    int width = Integer.parseInt(attributes.getNamedItem("width").getNodeValue());
                    int height = Integer.parseInt(attributes.getNamedItem("height").getNodeValue());
                    Element rect = rect(x, y, width, height);
                    rect = stylize(rect, style);
                    this.asSVGDocument().getRootElement().appendChild(rect);
                    break;
                case "circle":
                    int cx = Integer.parseInt(attributes.getNamedItem("cx").getNodeValue()) + xOffset;
                    int cy = Integer.parseInt(attributes.getNamedItem("cy").getNodeValue()) + yOffset;
                    int r = Integer.parseInt(attributes.getNamedItem("r").getNodeValue());
                    Element circle = circle(cx, cy, r);
                    circle = stylize(circle, style);
                    this.asSVGDocument().getRootElement().appendChild(circle);
                    break;
                case "path":
                    String d = attributes.getNamedItem("d").getNodeValue();
                    StringBuilder builder = new StringBuilder();
                    String[] nums = d.split(" ");
                    for (int j = 0; j < nums.length; j++) {
                        try {
                            int newX = Integer.parseInt(nums[j]) + xOffset;
                            int newY = Integer.parseInt(nums[j+1]) + yOffset;
                            builder.append(newX);
                            builder.append(" ");
                            builder.append(newY);
                            builder.append(" ");
                            j++;
                        } catch (NumberFormatException e) {
                            builder.append(nums[j]);
                            builder.append(" ");
                        }
                    }
                    Element path = path(builder.toString());
                    path = stylize(path, style);
                    this.asSVGDocument().getRootElement().appendChild(path);
            }
        }
    }
}
