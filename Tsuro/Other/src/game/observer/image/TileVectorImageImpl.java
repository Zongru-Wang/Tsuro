package game.observer.image;

import game.common.Position;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * TileVectorImageImpl that is based on the EmptyTileVectorImageImpl
 * draw the paths, ports
 */
class TileVectorImageImpl extends EmptyTileVectorImageImpl implements VectorImage {
    // draw the basic tile without the ports and paths specified with given information
    TileVectorImageImpl(Tile tile, int length) {
        super(length);
        for (Port port: Port.all()) {
            this.drawPortCircle(DEFAULT_COLOR, port, length);
        }
        for (Pair<Port, Port> pair: tile.asPairCollection()) {
            this.drawPortPairPath(DEFAULT_COLOR, pair, length);
        }
    }

    // draw the tile with with given information, and set the frame, port color
    // first get all port and draw them with colors
    // then draw the path between ports
    TileVectorImageImpl(Tile tile, int length, String frameColor, Map<Port, String> portToColorMap) {
        super(length, frameColor);
        for (Port port: Port.all()) {
            this.drawPortCircle(portToColorMap.getOrDefault(port, DEFAULT_COLOR), port, length);
        }
        for (Pair<Port, Port> pair : tile.asPairCollection()) {
            if (portToColorMap.containsKey(pair.getKey()) && portToColorMap.containsKey(pair.getValue()) &&
            portToColorMap.get(pair.getKey()).equals(portToColorMap.get(pair.getValue()))) {
                this.drawPortPairPath(portToColorMap.get(pair.getKey()), pair, length);
            } else {
                this.drawPortPairPath(DEFAULT_COLOR, pair, length);
            }
        }
    }

    // draw the port for with given information
    // we don't use CombinedVectorImage here is because CombinedVectorImage create a new document which can slow down
    private void drawPortCircle(String color, Port port, int length) {
        Element root = this.asSVGDocument().getRootElement();
        Position pair = this.getPortLocation(port, length);
        Element portCircle = circle(pair.getKey(), pair.getValue(), length / 10);
        portCircle = stylize(portCircle, color, null, 0);
        root.appendChild(portCircle);
    }

    // draw the path between two ports
    // here is a technique we used called bazier point
    private void drawPortPairPath(String color, Pair<Port, Port> pair, int length) {
        Element root = this.asSVGDocument().getRootElement();
        // location for two ports and their bazier points
        Position port1Location = getPortLocation(pair.getKey(), length);
        Position port2Location = getPortLocation(pair.getValue(), length);
        Position port1AssistingPoint = bazierAssistingPoint(pair.getKey(), length);
        Position port2AssistingPoint = bazierAssistingPoint(pair.getValue(), length);
        Element path = path(String.format("M %d %d C %d %d %d %d %d %d",
                port1Location.getKey(),          // x
                port1Location.getValue(),        // y
                port1AssistingPoint.getKey(),
                port1AssistingPoint.getValue(),
                port2AssistingPoint.getKey(),
                port2AssistingPoint.getValue(),
                port2Location.getKey(),
                port2Location.getValue()));
        path = stylize(path, null, color, length / 20);
        root.appendChild(path);
    }

    // get the bazierAssistingPoint for the port
    // Assisting point perpendicular to the line, quarter of the tile length
    private Position bazierAssistingPoint(Port port, int length) {
        int x = 0;
        int y = 0;
        switch (port.getDirection()) {
            case NORTH:
                x = (length / 3) * (port.getSideIndex() + 1);
                y = length / 4;
                break;
            case SOUTH:
                x = (length / 3) * (2 - port.getSideIndex());
                y = length * 3 / 4;
                break;
            case EAST:
                x = length * 3/ 4;
                y = (length / 3) * (port.getSideIndex() + 1);
                break;
            case WEST:
                x = length / 4;
                y = (length / 3) * (2 - port.getSideIndex());
                break;
        }
        return Position.of(x, y);
    }

    // get the location of the port
    // for different directions, we get the index of that port and do calculation
    private Position getPortLocation(Port port, int length) {
        int x = 0;
        int y = 0;
        switch (port.getDirection()) {
            case NORTH:
                x = (length / 3) * (port.getSideIndex() + 1);
                break;
            case SOUTH:
                x = (length / 3) * (2 - port.getSideIndex());
                y = length;
                break;
            case EAST:
                x = length;
                y = (length / 3) * (port.getSideIndex() + 1);
                break;
            case WEST:
                y = (length / 3) * (2 - port.getSideIndex());
                break;
        }
        return Position.of(x, y);
    }
}
