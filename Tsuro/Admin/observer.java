package game.observer;

import game.board.*;
import game.observer.image.VectorImage;
import game.tile.Tile;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Observer {
    void update(RefereeBoard board, IntermediatePlacement requested, List<Tile> choices, Token turn);
    void update(RefereeBoard board, InitialPlacement requested, List<Tile> choices, Token turn);
    void update(RefereeBoard board);
    VectorImage render(int boardLength);
}

package game.observer;

import game.board.*;
import game.observer.image.VectorImage;
import game.tile.Tile;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ObserverImpl implements Observer{
    private PlayerBoard board;
    Pair<Tile, Pair<Integer, Integer>> requested;
    private List<Tile> choices;
    private Token turn;

    @Override
    public void update(RefereeBoard board, IntermediatePlacement requested, List<Tile> choices, Token turn) {
        this.board = board.place(requested);
        this.requested = new Pair<>(requested.getTile(), requested.getPosition());
        this.choices = choices;
        this.turn = turn;
    }

    @Override
    public void update(RefereeBoard board, InitialPlacement requested, List<Tile> choices, Token turn) {
        this.board = board.placeInitial(requested);
        this.requested = new Pair<>(requested.getTile(), requested.getPosition());
        this.choices = choices;
        this.turn = turn;
    }

    @Override
    public void update(RefereeBoard board) {
        this.board = board;
        this.requested = null;
        this.choices = null;
        this.turn = null;
    }

    @Override
    public VectorImage render(int boardLength) {
        VectorImage result = VectorImage.empty((int)(boardLength * 1.5), (int)(boardLength * 1.2));
        if (board != null && requested != null) {
            result = result.embed(VectorImage.board(board, boardLength, requested.getValue()), boardLength * 4 / 10, boardLength / 10);
        } else if (board != null) {
            result = result.embed(VectorImage.board(board, boardLength), boardLength * 4 / 10, boardLength / 10);
        }
        int y = boardLength / 10;
        if (choices != null) {
            for (Tile choice: choices) {
                result = result.embed(VectorImage.tile(choice, boardLength / 10), boardLength / 10, y);
                y += boardLength / 5;
            }
        }
        if (turn != null) {
            result = result.embed(VectorImage.circle(boardLength/20, TokenColorUtil.toColor(this.turn)), boardLength / 10, y);
        }
        return result;
    }

}

package game.observer;

import game.board.Token;

public class TokenColorUtil {
    public static String toColor(Token token) {
        switch (token) {
            case RED:
                return "#D22";
            case GREEN:
                return "#2D2";
            case BLUE:
                return "#22D";
            case BLACK:
                return "#222";
            case WHITE:
                return "#DDD";
        }
        throw new RuntimeException();
    }
}

// ============================ image =================================
package game.observer.image;

import game.board.PlayerBoard;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface VectorImage {
    int getWidth();
    int getHeight();
    VectorImage embed(VectorImage image, int x, int y);
    BufferedImage asBufferedImage();
    SVGDocument asSVGDocument();
    public static VectorImage empty(int width, int height) {
        return new EmptyVectorImageImpl(width, height);
    }
    public static VectorImage empty(int width, int height, String bgColor) {
        return new EmptyVectorImageImpl(width, height, bgColor);
    }
    public static VectorImage circle(int radius, String fillColor) {
        return new CircleVectorImageImpl(radius, fillColor);
    }
    public static VectorImage tile(Tile tile, int length) {
        return new TileVectorImageImpl(tile, length);
    }
    public static VectorImage tile(Tile tile, int length, String frameColor, Map<Port, String> portToColorMap) {
        return new TileVectorImageImpl(tile, length, frameColor, portToColorMap);
    }
    public static VectorImage emptyTile(int length) {
        return new EmptyTileVectorImageImpl(length);
    }
    public static VectorImage emptyTile(int length, String color) {
        return new EmptyTileVectorImageImpl(length, color);
    }
    public static VectorImage board(PlayerBoard board, int length) {
        return new BoardVectorImageImpl(board, length);
    }
    public static VectorImage board(PlayerBoard board, int length, Pair<Integer, Integer> highlight) {
        return new BoardVectorImageImpl(board, length, highlight);
    }
}

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

abstract class AbstractVectorImageImpl implements VectorImage {
    private final SVGDocument svgDocument;
    private final String svgNS;
    private final int width;
    private final int height;

    protected AbstractVectorImageImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        this.svgDocument = (SVGDocument) impl.createDocument(this.svgNS, "svg", null);
        Element root = svgDocument.getRootElement();
        root.setAttributeNS(null, "width", String.valueOf(this.width));
        root.setAttributeNS(null, "height", String.valueOf(this.height));
    }

    protected Element circle(int cx, int cy, int r) {
        Element circle = this.svgDocument.createElementNS(this.svgNS, "circle");
        circle.setAttributeNS(null, "cx", String.valueOf(cx));
        circle.setAttributeNS(null, "cy", String.valueOf(cy));
        circle.setAttributeNS(null, "r", String.valueOf(r));
        return circle;
    }

    protected Element rect(int x, int y, int width, int height) {
        Element rectangle = this.svgDocument.createElementNS(svgNS, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(x));
        rectangle.setAttributeNS(null, "y", String.valueOf(y));
        rectangle.setAttributeNS(null, "width", String.valueOf(width));
        rectangle.setAttributeNS(null, "height", String.valueOf(height));
        return rectangle;
    }

    protected Element path(String d) {
        Element path = this.svgDocument.createElementNS(this.svgNS, "path");
        path.setAttributeNS(null, "d", d);
        return path;
    }

    protected Element clip(int x, int y, int width, int height, String id) {
        Element clipPath = this.svgDocument.createElementNS(this.svgNS, "clipPath");
        clipPath.setAttributeNS(null, "id", id);
        Element rect = this.rect(x, y, width, height);
        clipPath.appendChild(rect);
        return clipPath;
    }

    protected Element stylize(Element element, String fillColor, String strokeColor, int strokeWidth) {
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

    protected Element stylize(Element element, String style) {
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
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
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

package game.observer.image;

import game.board.PlayerBoard;
import game.board.RefereeBoard;
import game.board.Token;
import game.observer.TokenColorUtil;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class BoardVectorImageImpl implements VectorImage {
    private final VectorImage source;
    BoardVectorImageImpl(PlayerBoard board, int length) {
        VectorImage source = VectorImage.empty(length, length);
        Map<Pair<Integer, Integer>, Map<Port, String>> portToColorMap = this.createPortToColorMap(board);
        for (int x = 0; x < 10; x ++) {
            for (int y = 0; y < 10; y ++) {
                Pair<Integer, Integer> boardPosition = new Pair<>(x, y);
                int xOffset = x * length / 10;
                int yOffset = y * length / 10;
                if (board.getTile(boardPosition).isPresent()) {
                    Tile tile = board.getTile(boardPosition).get();
                    VectorImage tileImage = VectorImage.tile(tile, length/10, "#888", portToColorMap.getOrDefault(boardPosition, new HashMap<>()));
                    source = source.embed(tileImage, xOffset, yOffset);
                } else {
                    VectorImage emptyTileImage = VectorImage.emptyTile(length/10);
                    source = source.embed(emptyTileImage, xOffset, yOffset);
                }
            }
        }
        this.source = source;
    }
    BoardVectorImageImpl(PlayerBoard board, int length, Pair<Integer, Integer> highlight) {
        VectorImage source = VectorImage.empty(length, length);
        Map<Pair<Integer, Integer>, Map<Port, String>> portToColorMap = this.createPortToColorMap(board);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Pair<Integer, Integer> boardPosition = new Pair<>(x, y);
                int xOffset = x * length / 10;
                int yOffset = y * length / 10;
                String color = "#888";
                if (boardPosition.equals(highlight)) {
                    color = "#AAA";
                }
                if (board.getTile(boardPosition).isPresent()) {
                    Tile tile = board.getTile(boardPosition).get();
                    VectorImage tileImage = VectorImage.tile(tile, length / 10, color, portToColorMap.getOrDefault(boardPosition, new HashMap<>()));
                    source = source.embed(tileImage, xOffset, yOffset);
                }else {
                    VectorImage emptyTileImage = VectorImage.emptyTile(length/10, color);
                    source = source.embed(emptyTileImage, xOffset, yOffset);
                }
            }
        }
        this.source = source;
    }

    private Map<Pair<Integer, Integer>, Map<Port, String>> createPortToColorMap(PlayerBoard board) {
        Map<Pair<Integer, Integer>, Map<Port, String>> portToColorMap = new HashMap<>();
        for (Token token : Token.values()) {
            SimpleGraph<Pair<Port, Pair<Integer, Integer>>, DefaultEdge> graph = board.asGraph();
            ShortestPathAlgorithm<Pair<Port, Pair<Integer, Integer>>, DefaultEdge> algorithm = new DijkstraShortestPath<>(graph);
            if (board.getInitialPortPosition(token).isPresent()) {
                Pair<Port, Pair<Integer, Integer>> from = board.getInitialPortPosition(token).get();
                assert board.getPortPosition(token).isPresent();
                Pair<Port, Pair<Integer, Integer>> to;
                if (!board.getPortPosition(token).get().didExit()) {
                    to = board.getPortPosition(token).get().tokenPosition();
                    for (Pair<Port, Pair<Integer, Integer>> v : algorithm.getPath(from, to).getVertexList()) {
                        if (!portToColorMap.containsKey(v.getValue())) {
                            portToColorMap.put(v.getValue(), new HashMap<>());
                        }
                        portToColorMap.get(v.getValue()).put(v.getKey(), TokenColorUtil.toColor(token));
                    }
                }
            }
        }
        return portToColorMap;
    }

    @Override
    public int getWidth() {
        return this.source.getWidth();
    }

    @Override
    public int getHeight() {
        return this.source.getHeight();
    }

    @Override
    public VectorImage embed(VectorImage image, int x, int y) {
        return this.source.embed(image, x, y);
    }

    @Override
    public BufferedImage asBufferedImage() {
        return this.source.asBufferedImage();
    }

    @Override
    public SVGDocument asSVGDocument() {
        return this.source.asSVGDocument();
    }
}

package game.observer.image;

import org.w3c.dom.Element;

public class CircleVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {
    protected CircleVectorImageImpl(int radius, String fillColor) {
        super(radius * 2, radius * 2);
        Element root = this.asSVGDocument().getRootElement();
        Element circle = this.circle(radius, radius, radius);
        circle = stylize(circle, fillColor, null, 0);
        root.appendChild(circle);
    }
}

package game.observer.image;

import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.*;
import org.w3c.dom.svg.SVGDocument;

class CombinedVectorImageImpl extends AbstractVectorImageImpl implements VectorImage{
    CombinedVectorImageImpl(VectorImage base, int xOffset, int yOffset, VectorImage embedded) {
        super(base.getWidth(), base.getHeight());
        Element baseRoot = base.asSVGDocument().getRootElement();
        Element embeddedRoot = embedded.asSVGDocument().getRootElement();
        for (int i = 0; i < baseRoot.getChildNodes().getLength(); i++) {
            Node copy = this.asSVGDocument().importNode(baseRoot.getChildNodes().item(i), true);
            this.asSVGDocument().getRootElement().appendChild(copy);
        }
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

package game.observer.image;

import game.tile.Tile;
import org.w3c.dom.Element;

class EmptyTileVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {

    protected final String DEFAULT_COLOR = "#888";

    EmptyTileVectorImageImpl(int length) {
        super(length, length);
        this.drawFrameRect(DEFAULT_COLOR, length);
    }

    EmptyTileVectorImageImpl(int length, String color) {
        super(length, length);
        this.drawFrameRect(color, length);
    }

    private void drawFrameRect(String color, int length) {
        Element root = this.asSVGDocument().getRootElement();
        Element frame = this.rect(0, 0, (int)(length * 0.95), (int)(length * 0.95));
        frame = stylize(frame, null, color, length / 20);
        root.appendChild(frame);
    }

}

package game.observer.image;


import org.w3c.dom.Element;

class EmptyVectorImageImpl extends AbstractVectorImageImpl implements VectorImage {

    EmptyVectorImageImpl(int width, int height) {
        super(width, height);
    }

    EmptyVectorImageImpl(int width, int height, String bgColor) {
        super(width, height);
        Element root = this.asSVGDocument().getRootElement();
        root.appendChild(stylize(rect(0, 0, width, height), bgColor, null, 0));
    }
}

package game.observer.image;

import game.board.RefereeBoard;
import game.board.Token;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;

class TileVectorImageImpl extends EmptyTileVectorImageImpl implements VectorImage {
    TileVectorImageImpl(Tile tile, int length) {
        super(length);
        for (Port port: Port.all()) {
            this.drawPortCircle(DEFAULT_COLOR, port, length);
        }
        for (Pair<Port, Port> pair: tile.asPairCollection()) {
            this.drawPortPairPath(DEFAULT_COLOR, pair, length);
        }
    }

    TileVectorImageImpl(Tile tile, int length, String frameColor, Map<Port, String> portToColorMap) {
        super(length, frameColor);
        for (Port port: Port.all()) {
            this.drawPortCircle(portToColorMap.getOrDefault(port, DEFAULT_COLOR), port, length);
        }
        for (Pair<Port, Port> pair: tile.asPairCollection()) {
            if (portToColorMap.containsKey(pair.getKey()) && portToColorMap.containsKey(pair.getValue()) &&
            portToColorMap.get(pair.getKey()).equals(portToColorMap.get(pair.getValue()))) {
                this.drawPortPairPath(portToColorMap.get(pair.getKey()), pair, length);
            } else {
                this.drawPortPairPath(DEFAULT_COLOR, pair, length);
            }
        }
    }

    private void drawPortCircle(String color, Port port, int length) {
        Element root = this.asSVGDocument().getRootElement();
        Pair<Integer, Integer> pair = this.getPortLocation(port, length);
        Element portCircle = circle(pair.getKey(), pair.getValue(), length / 10);
        portCircle = stylize(portCircle, color, null, 0);
        root.appendChild(portCircle);
    }

    private void drawPortPairPath(String color, Pair<Port, Port> pair, int length) {
        Element root = this.asSVGDocument().getRootElement();
        Pair<Integer, Integer> port1Location = getPortLocation(pair.getKey(), length);
        Pair<Integer, Integer> port2Location = getPortLocation(pair.getValue(), length);
        Pair<Integer, Integer> port1AssistingPoint = bazierAssistingPoint(pair.getKey(), length);
        Pair<Integer, Integer> port2AssistingPoint = bazierAssistingPoint(pair.getValue(), length);
        Element path = path(String.format("M %d %d C %d %d %d %d %d %d",
                port1Location.getKey(),
                port1Location.getValue(),
                port1AssistingPoint.getKey(),
                port1AssistingPoint.getValue(),
                port2AssistingPoint.getKey(),
                port2AssistingPoint.getValue(),
                port2Location.getKey(),
                port2Location.getValue()));
        path = stylize(path, null, color, length / 20);
        root.appendChild(path);
    }

    protected Pair<Integer, Integer> bazierAssistingPoint(Port port, int length) {
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
        return new Pair<>(x, y);
    }

    protected Pair<Integer, Integer> getPortLocation(Port port, int length) {
        int x = 0;
        int y = 0;
        switch (port.getDirection()) {
            case NORTH:
                x = (length / 3) * (port.getSideIndex() + 1);
                y = 0;
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
                x = 0;
                y = (length / 3) * (2 - port.getSideIndex());
                break;
        }
        return new Pair<>(x, y);
    }
}

