package game.board;

import game.common.PortPosition;
import game.common.Position;
import game.tile.Port;
import game.tile.Tile;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static game.board.BoardUtil.*;
import static game.common.PositionUtil.neighborAt;

/**
 * The class represent the board. The board will have a 2D array represents the tiles on it.
 * And a Map record the initial token color and location on the tile.
 * And a BoardState shows the state of the Board.
 * The Size of the board is 10 which means it is a 10 by 10 board.
 */
public class Board {
    private final int SIZE = 10;
    private final Tile[][] tiles;
    private final BidiMap<Token, PortPosition> tokenInitialPositions;
    private final BoardState state;

    private static final String NOT_WITHIN_BOARD_PERIPHERY = "Position is not within Board Periphery";
    private static final String NOT_WITHIN_BOARD = "Position is not within Board";
    private static final String NOT_AT_PERIPHERY = "Port is not at Periphery";
    private static final String TOKEN_ALREADY_EXIST = "Token is already exist";
    private static final String POSITION_ALREADY_TOKEN = "Position is already token";
    private static final String INITIAL_ONLY = "This is only for initial";
    private static final String BOARD_SHOULD_NOT_EMPTY = "Board should not be empty";
    private static final String INVALID_PLACEMENT = "Invalid placement";
    private static final String INVALID_INITIAL_PLACEMENT = "Invalid initial placement";
    private static final String INVALID_INTERMEDIATE_PLACEMENT = "Invalid intermediate placement";


    /**
     * Create an empty Board.
     */
    private Board() {
        this.tiles = new Tile[SIZE][SIZE];
        this.tokenInitialPositions = new DualHashBidiMap<>();
        this.state = BoardState.EMPTY;
    }

    /**
     * Create a playing board by giving a 2D array of tiles, initial token positions, and the state of the board.
     * @param tiles                     The tiles placed on the board.
     * @param tokenInitialPositions     The locations of initial Token positions.
     * @param state                     The state of the board
     */
    private Board(Tile[][] tiles, BidiMap<Token, PortPosition> tokenInitialPositions, BoardState state) {
        Tile[][] result = new Tile[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            result[i] = tiles[i].clone();
        }
        this.tiles = result;
        this.tokenInitialPositions = new DualHashBidiMap<>(tokenInitialPositions);
        this.state = state;
    }


    /**
     * Make initial placements in the board.
     * @param token       The token for the initial placement
     * @param tile        The tile for the initial placement
     * @param port        The port that the token starts
     * @param position    The position of the placement for the tile on the board.
     * @return            A new board with the given initial placement.
     * @throws IllegalStateException      If tyring to place initial placements during the intermediate state.
     * @throws IllegalArgumentException   if trying to place invalid initial placement on the board.
     */
    public Board placeInitial(Token token, Tile tile, Port port, Position position)
            throws IllegalStateException, IllegalArgumentException {
        if (!withinBoardPeriphery(position))
            throw new IllegalArgumentException(NOT_WITHIN_BOARD_PERIPHERY);
        if (!atPeriphery(position, port))
            throw new IllegalArgumentException(NOT_AT_PERIPHERY);
        if (this.tokenInitialPositions.containsKey(token))
            throw new IllegalArgumentException(TOKEN_ALREADY_EXIST);
        if (this.getTile(position).isPresent())
            throw new IllegalArgumentException(POSITION_ALREADY_TOKEN);
        Tile[][] newTiles = this.withNewTile(tile, position);
        BidiMap<Token, PortPosition> newTokenPositions
                = new DualHashBidiMap<>(this.tokenInitialPositions);
        newTokenPositions.put(token, PortPosition.of(port, position));
        return this.placeInitialHelper(newTiles, newTokenPositions);
    }

    /**
     * Give a tile and position return a new Tile[][] with the given tile placed on the desired location
     * on that 2D array.
     * @throws IllegalArgumentException if the given location is already occupied.
     */
    private Tile[][] withNewTile(Tile tile, Position position) {
        if (this.getTile(position).isPresent())
            throw new IllegalArgumentException(POSITION_ALREADY_TOKEN);
        Tile[][] newTiles = this.as2DArray();
        newTiles[position.getX()][position.getY()] = tile;
        return newTiles;
    }

    /**
     * Give an InitialPlacement and place that on the board
     */
    public Board placeInitial(InitialPlacement ip) {
        return this.placeInitial(ip.getToken(), ip.getTile(), ip.getPort(), ip.getPosition());
    }

    /**
     * The helper function that check if we care calling the initial placements during the right state of the game.
     * Throw exceptions if the state doesn't allow initial placement.
     */
    private Board placeInitialHelper(Tile[][] newTiles,
                                     BidiMap<Token, PortPosition> newTokenPositions) {
        switch (this.state) {
            case EMPTY:
            case INITIALIZING:
                return new Board(newTiles, newTokenPositions, BoardState.INITIALIZING);
            case HAD_INITIALIZED:
                throw new IllegalStateException(INITIAL_ONLY);
            default:
                throw new RuntimeException(INVALID_PLACEMENT);
        }
    }

    /**
     * Give a tile and the position and use that to do an intermediate placement.
     * @param tile         the tile we want to place.
     * @param position     the position of the placement
     * @return             the Board add with the given placement if the placement is valid.
     */
    public Board place(Tile tile, Position position) {
        if (!withinBoardBoundary(position))
            throw new IllegalArgumentException(NOT_WITHIN_BOARD);
        Tile[][] newTiles = this.withNewTile(tile, position);
        BidiMap<Token, PortPosition> newTokenPositions
                = new DualHashBidiMap<>(this.tokenInitialPositions);
        return placeHelper(newTiles, newTokenPositions);
    }

    /**
     * Make an intermediatePlacement by the given intermediate placement.
     */
    public Board place(IntermediatePlacement intermediatePlacement) {
        return this.place(
                intermediatePlacement.getTile(),
                intermediatePlacement.getPosition());
    }

    /**
     * The helper function for intermediate placement that check if the placement happens in right state.
     * if the placement is valid, then create new board by the given value.
     */
    private Board placeHelper(Tile[][] newTiles,
                              BidiMap<Token, PortPosition> newTokenPositions) {
        switch (this.state) {
            case EMPTY:
                throw new IllegalStateException(BOARD_SHOULD_NOT_EMPTY);
            case INITIALIZING:
            case HAD_INITIALIZED:
                return new Board(newTiles, newTokenPositions, BoardState.HAD_INITIALIZED);
            default:
                throw new RuntimeException(INVALID_PLACEMENT);
        }
    }

    /**
     * Three methods belows is used to check the current game state
     */
    public boolean isEmpty() {
        return this.state == BoardState.EMPTY;
    }


    public boolean isInitializing() {
        return this.state == BoardState.INITIALIZING;
    }


    public boolean hadInitialized() {
        return this.state == BoardState.HAD_INITIALIZED;
    }

    /**
     * Get the tile by given location.
     * @param position  The position on the board that we want to the the tile from
     * @return          An optional, return empty if the given location on the board doesn't have a tile
     * @throws IllegalArgumentException     if the given location is not within the board.
     */
    public Optional<Tile> getTile(Position position) throws IllegalArgumentException {
        if (!withinBoardBoundary(position))
            throw new IllegalArgumentException(NOT_WITHIN_BOARD);
        if (this.tiles[position.getX()][position.getY()] == null) {
            return Optional.empty();
        } else {
            return Optional.of(this.tiles[position.getX()][position.getY()]);
        }
    }


    /**
     * Get the position of a port represented by PortPosition. Position is the
     * coordinate of the tile contains the port which the token locates on.
     * @param token  The token we want to track
     * @return  An optional.empty if the given token is not placed ever,
     *          if the token exists PortPosition represent the position of a token.
     */
    public Optional<TokenStatus> getPortPosition(Token token) {
        if (this.tokenInitialPositions.containsKey(token)) {
            SimpleGraph<PortPosition, DefaultEdge> graph = this.asGraph();
            ShortestPathAlgorithm<PortPosition, DefaultEdge> algorithm
                    = new DijkstraShortestPath<>(graph);
            ConnectivityInspector<PortPosition, DefaultEdge> connectivity
                    = new ConnectivityInspector<>(graph);

            PortPosition initial = this.tokenInitialPositions.get(token);
            PortPosition destination = connectivity.connectedSetOf(initial).stream()
                    .max(Comparator.comparingInt(o -> algorithm.getPath(initial, o).getLength()))
                    .orElseThrow(RuntimeException::new);
            if (this.tokenInitialPositions.containsValue(destination)) {
                return Optional.of(TokenStatus.collided(
                        token,
                        destination,
                        this.tokenInitialPositions.getKey(destination)));
            } else if (atPeriphery(destination.getPosition(), destination.getPort())) {
                return Optional.of(TokenStatus.exited(token, destination));
            } else {
                return Optional.of(TokenStatus.inBoard(token, destination));
            }
        } else {
            return Optional.empty();
        }
    }


    /**
     * Get the token position at the initial state of the game.
     * Also used to check the equality between two boards.
     */
    public Optional<PortPosition> getInitialPortPosition(Token token) {
        if (this.tokenInitialPositions.containsKey(token)) {
            return Optional.of(this.tokenInitialPositions.get(token));
        } else {
            return Optional.empty();
        }
    }


    /**
     * Represent the board as an SimpleGraph. With all tiles in the current board.
     */
    public SimpleGraph<PortPosition, DefaultEdge> asGraph() {
        SimpleGraph<PortPosition, DefaultEdge> result = new SimpleGraph<>(DefaultEdge.class);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Position position = Position.of(x, y);
                if (this.getTile(position).isPresent()) {
                    addTileGraph(result, this.getTile(position).get(), position);
                }
            }
        }
        return result;
    }

    /**
     * Given a SimpleGraph with default edges, a tile and the position that we want to place the tile.
     * Transfer the tile to a SimpleGraph and use this to calculate the destination of the token location
     * on the graph.
     * @param destination  The final location of the token when adding a new tile on the board.
     * @param tile         The tile added to the board.
     * @param position     The position that we place the tile
     */
    private void addTileGraph(SimpleGraph<PortPosition, DefaultEdge> destination,
                              Tile tile,
                              Position position) {
        SimpleGraph<Port, DefaultEdge> tileGraph = tile.asGraph();
        for (DefaultEdge e: tileGraph.edgeSet()) {
            Port source = tileGraph.getEdgeSource(e);
            Port target = tileGraph.getEdgeTarget(e);
            PortPosition sourceVertex = PortPosition.of(source, position);
            PortPosition targetVertex = PortPosition.of(target, position);
            Graphs.addEdgeWithVertices(destination, sourceVertex, targetVertex);

            PortPosition toConnectSource = PortPosition.of(
                    Port.of(source.getDirection().opposite(), (source.getSideIndex() + 1) % 2),
                    neighborAt(position, source.getDirection()));
            PortPosition toConnectTarget = PortPosition.of(
                    Port.of(target.getDirection().opposite(), (target.getSideIndex() + 1) % 2),
                    neighborAt(position, target.getDirection()));
            if (destination.containsVertex(toConnectSource)) {
                destination.addEdge(sourceVertex, toConnectSource);
            }
            if (destination.containsVertex(toConnectTarget)) {
                destination.addEdge(targetVertex, toConnectTarget);
            }
        }
    }


    /**
     * Represent the board as a 2D array.
     * Size is 10 by 10.
     */
    public Tile[][] as2DArray() {
        Tile[][] result = new Tile[SIZE][];
        for (int i = 0; i < SIZE; i++) {
            result[i] = this.tiles[i].clone();
        }
        return result;
    }

    @Override
    public String toString() {
        return this.asGraph().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Board) {
            Board other = (Board) obj;
            for (Token token : Token.values()) {
                if (!this.getInitialPortPosition(token).equals(other.getInitialPortPosition(token))) {
                    return false;
                }
            }
            return Arrays.deepEquals(this.tiles, other.as2DArray());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.tiles) * this.tokenInitialPositions.hashCode();
    }

    /**
     * Represent the state of the board.
     */
    private enum BoardState {
        EMPTY, INITIALIZING, HAD_INITIALIZED
    }

    /**
     * create an empty board.
     */
    public static Board empty() {
        return new Board();
    }

    /**
     * Give a list of initial placements and try to place them.
     * Throws exceptions if the given initial placements are empty
     * @param initialPlacements         A list of initial placements given
     * @return                          A board with given initial placements if valid.
     */
    public static Board initializing(List<InitialPlacement> initialPlacements) {
        if (initialPlacements.isEmpty())
            throw new IllegalArgumentException(INVALID_INITIAL_PLACEMENT);
        Board result = empty();
        for (InitialPlacement initialPlacement : initialPlacements) {
            result = result.placeInitial(initialPlacement);
        }
        return result;
    }

    /**
     * Given a list of initial placements and intermediate placements to give an initialized board.
     * @param initialPlacements          A list of initial placements to place
     * @param intermediatePlacements     A list of intermediate placements to place
     * @return                           A initialized board
     */
    public static Board initialized(List<InitialPlacement> initialPlacements,
                                    List<IntermediatePlacement> intermediatePlacements) {
        if (intermediatePlacements.isEmpty())
            throw new IllegalArgumentException(INVALID_INTERMEDIATE_PLACEMENT);
        Board result = initializing(initialPlacements);
        for (IntermediatePlacement intermediatePlacement : intermediatePlacements) {
            result = result.place(intermediatePlacement);
        }
        return result;
    }

    /**
     * Make a board has certain state from tiles and token map. If no tiles exists, return empty(), if the number
     * of tiles equal to the number of token, the board state is INITIALIZING, else, the board has been INITIALIZED
     * @param tiles   the tiles on the board
     * @param map     the  map of the token.
     * @return        the Board
     */
    public static Board fromTilesAndTokenMap(Tile[][] tiles, BidiMap<Token, PortPosition> map) {
        int tileCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (tiles[i][j] != null) {
                    tileCount++;
                }
            }
        }
        if (tileCount == 0) {
            return empty();
        } else if (tileCount == map.size()) {
            return new Board(tiles, map, BoardState.INITIALIZING);
        } else {
            return new Board(tiles, map, BoardState.HAD_INITIALIZED);
        }
    }
}
