package game.observer.image;

import game.board.Board;
import game.board.Token;
import game.common.PortPosition;
import game.common.Position;
import game.observer.TokenColorUtil;
import game.tile.Port;
import game.tile.Tile;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.w3c.dom.svg.SVGDocument;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * BoardVectorImageImpl that is VectorImage represent the current board state
 * originally is a vectorImage, we want to get the tile and place those on the board with using the embed,
 * if there is tile, we place the tile with its look like, if no, we place the empty tile frame
 */
class BoardVectorImageImpl implements VectorImage {
    private final VectorImage source;

    // create the board with given length without any highlight on it
    // it is using BoardVectorImageImpl(Board board, int length, Position highlight)
    BoardVectorImageImpl(Board board, int length) {
        this(board, length, Position.of(-1, -1));
    }

    // create the board with given length with any highlight on the given highlight position
    // base on the empty VectorImage
    // tiles on it we are using CombineVector image to embed the tiles on it
    // if there is empty, we place an empty tile
    BoardVectorImageImpl(Board board, int length, Position highlight) {
        VectorImage source = VectorImage.empty(length, length);
        // since we want to enable the path and port has color of the token
        Map<Position, Map<Port, String>> portToColorMap = this.createPortToColorMap(board);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Position boardPosition = Position.of(x, y);
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

    // preparation for enabling the path and port has color of the token
    // from is the start port
    // to is the end port
    private Map<Position, Map<Port, String>> createPortToColorMap(Board board) {
        Map<Position, Map<Port, String>> portToColorMap = new HashMap<>();
        for (Token token : Token.values()) {
            SimpleGraph<PortPosition, DefaultEdge> graph = board.asGraph();
            ShortestPathAlgorithm<PortPosition, DefaultEdge> algorithm = new DijkstraShortestPath<>(graph);
            if (board.getPortPosition(token).isPresent() && board.getInitialPortPosition(token).isPresent() && !board.getPortPosition(token).get().didExit()) {
                PortPosition from = board.getInitialPortPosition(token).get();
                PortPosition to = board.getPortPosition(token).get().tokenPosition();
                for (PortPosition v : algorithm.getPath(from, to).getVertexList()) {
                    if (!portToColorMap.containsKey(v.getPosition())) {
                        portToColorMap.put(v.getPosition(), new HashMap<>());
                    }
                    portToColorMap.get(v.getPosition()).put(v.getKey(), TokenColorUtil.toColor(token));
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
