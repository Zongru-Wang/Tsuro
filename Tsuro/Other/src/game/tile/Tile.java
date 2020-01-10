package game.tile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import game.json.deserializer.TileDeserializer;
import javafx.util.Pair;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The class represent the Tiles in the game. Each tile will have 8 ports.
 * Every tile is represented by a SimpleGraph from the jgrapht library.
 */
public class Tile extends Rotatable<Tile> {
    private static final String INVALID_PAIRS_SIZE = "Invalid pairs size";
    private static final String PAIR_ALREADY_EXIST = "Pair already exist";
    private static final String INVALID_GRAPH = "Invalid graph";
    private static final String JSON_FILENAME_NULL = "Json filename is null";
    private static final String TILE_COLLECTION_SHOULD_BE_JSON_ARRAY = "Tile collection must be json array";
    private static final String INVALID_FILENAME = "Invalid Json filename";
    private static final String INVALID_JSON_FILE_PATH = "Invalid Json file path";

    private final SimpleGraph<Port, DefaultEdge> graph;

    /**
     * Construct a tile by 4 pairs of port, each pair represent the connection between two ports in a tile.
     * @param pairs         A collection of 4 pairs of ports.
     */
    Tile(Collection<Pair<Port, Port>> pairs) {
        if (pairs.size() != 4) {
            throw new IllegalArgumentException(INVALID_PAIRS_SIZE);
        }
        graph = new SimpleGraph<>(DefaultEdge.class);
        Set<Port> observed = new HashSet<>();
        for (Pair<Port, Port> pair: pairs) {
            if (!observed.contains(pair.getKey()) && !observed.contains(pair.getValue())) {
                observed.add(pair.getKey());
                observed.add(pair.getValue());
                graph.addVertex(pair.getKey());
                graph.addVertex(pair.getValue());
                graph.addEdge(pair.getKey(), pair.getValue());
            } else {
                throw new IllegalArgumentException(PAIR_ALREADY_EXIST);
            }
        }
    }

    /**
     * Create a tile by a simpleGraph.
     * Through IllegalArgumentException if the vertexSet in the graph is not same as the set of all our ports.
     */
    Tile(SimpleGraph<Port, DefaultEdge> graph) {
        if (!graph.vertexSet().equals(new HashSet<>(Port.all())))
            throw new IllegalArgumentException(INVALID_GRAPH);
        SimpleGraph<Port, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addGraph(newGraph, graph);
        this.graph = newGraph;
    }


    Tile(Tile t) {
        this(t.asGraph());
    }

    /**
     * Give a port and return a port that connects to the given port in the tile.
     * @param fromPort   The port we given.
     * @return           The port connects to the given port.
     */
    public Port connectedPort(Port fromPort) {
        Port result = null;
        // Get all path of a tile form the graph
        BidirectionalDijkstraShortestPath<Port, DefaultEdge> shortestPath = new BidirectionalDijkstraShortestPath<>(this.graph);
        // Loop through all ports and check all path to get the connected port.
        for (Port port: Port.all()) {
            if (!fromPort.equals(port) && shortestPath.getPath(fromPort, port) != null) {
                result = port;
            }
        }
        return result;
    }

    /**
     * Represent a tile as an SimpleGraph
     */
    public SimpleGraph<Port, DefaultEdge> asGraph() {
        SimpleGraph<Port, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addGraph(newGraph, this.graph);
        return newGraph;
    }

    /**
     * Get a collection of pairs of ports which represent the connection of ports in the tile.
     */
    public Collection<Pair<Port, Port>> asPairCollection() {
        Set<Port> observed = new HashSet<>();
        Collection<Pair<Port, Port>> result = new ArrayList<>();
        for (Port port: Port.all()) {
            if (!observed.contains(port)) {
                observed.add(port);
                observed.add(this.connectedPort(port));
                result.add(new Pair<>(port, this.connectedPort(port)));
            }
        }
        return result;
    }

    @Override
    protected Tile rotateXTimes(int x) {
        SimpleGraph<Port, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);
        for (DefaultEdge edge: this.graph.edgeSet()) {
            Port newSource = this.graph.getEdgeSource(edge).rotate(x * 90);
            Port newTarget = this.graph.getEdgeTarget(edge).rotate(x * 90);
            newGraph.addVertex(newSource);
            newGraph.addVertex(newTarget);
            newGraph.addEdge(newSource, newTarget);
        }
        return new Tile(newGraph);
    }

    @Override
    public String toString() {
        return this.asGraph().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile other = (Tile) obj;
            for (Port port :Port.all()) {
                if (!this.connectedPort(port).equals(other.connectedPort(port)))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Port port :Port.all()) {
            result += port.hashCode();
            result *= 10;
        }
        return result;
    }

    public static Tile of(Collection<Pair<Port, Port>> pairs) {
        return new Tile(pairs);
    }

    public static Tile of(SimpleGraph<Port, DefaultEdge> graph) {
        return new Tile(graph);
    }

    public static Tile of(Tile tile) {
        return new Tile(tile);
    }

    public static List<Tile> loadAll(String jsonFileName) throws FileNotFoundException {
        if (jsonFileName == null) {
            throw new IllegalArgumentException(JSON_FILENAME_NULL);
        }
        try {
            InputStream jsonInput = Tile.class.getResourceAsStream(jsonFileName);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Tile.class, new TileDeserializer());
            Gson gson = gsonBuilder.create();
            JsonElement jsonElement = gson.fromJson(new InputStreamReader(jsonInput), JsonElement.class);
            if (!jsonElement.isJsonArray())
                throw new JsonParseException(TILE_COLLECTION_SHOULD_BE_JSON_ARRAY);
            List<Tile> result = new ArrayList<>();
            for (JsonElement tileJson : jsonElement.getAsJsonArray()) {
                Tile tile = gson.fromJson(tileJson, Tile.class);
                result.add(tile);
            }
            return result;
        } catch (NullPointerException e) {
            throw new FileNotFoundException(INVALID_FILENAME);
        }
    }

    private static String DEFAULT_JSON_FILE_PATH = "/tile.json";
    private static List<Tile> defaultFileCache = new ArrayList<>();

    public static List<Tile> loadDefaultAll() {
        if (defaultFileCache.isEmpty()) {
            try {
                defaultFileCache.addAll(loadAll(DEFAULT_JSON_FILE_PATH));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(INVALID_JSON_FILE_PATH);
            }
        }
        return new ArrayList<>(defaultFileCache);
    }
}
