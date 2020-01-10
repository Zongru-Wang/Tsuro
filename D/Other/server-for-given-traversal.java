import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class client {


    // creates a labrynth based on the information found in an iterator to a
    // jsonarray
    // objects in Json array is formatted {"from" : name:string, "to" : name:string}
    public static void createLabrynthFromJSON(labyrinth labrynth, Iterator<JSONObject> itr) {
        while (itr.hasNext()) {
            JSONObject obj = (JSONObject) itr.next();
            String nodeName1 = obj.get("from").toString();
            String nodeName2 = obj.get("to").toString();
            addEdgeToLabrynth(labrynth, nodeName1, nodeName2);
        }
    }


    //adds token to a node based on the information found in an iterator to a JSON array
    // format of JSON array is ["add" , token:color-string, name:string]
    public static void addTokenToLabrynthFromJSON(labyrinth labrynth, Iterator itr) {
        String tokenColor = itr.next().toString();
        String nodeName = itr.next().toString();
        addTokenToLabrynth(labrynth, tokenColor, nodeName);
    }

    //moves token to a node in the labrynth based on the information found in an iterator to a JSON array
    //format of the JSON array is  ["move", token:color-string, name:string]
    public static void moveTokenInLabrynthFromJSON(labyrinth labrynth, Iterator itr) {
        String tokenColor = itr.next().toString();
        String nodeName = itr.next().toString();
        moveTokenInLabrynth(labrynth, tokenColor, nodeName);
    }

    public static void addEdgeToLabrynth(labyrinth labrynth, String nodeName1, String nodeName2) {
        // insert code here based on implementation of Labrynth
        labrynth.addNodeAnyway(nodeName1);
        labrynth.addNodeAnyway(nodeName2);
        labrynth.connectNode(nodeName1, nodeName2);
    }

    public static void addTokenToLabrynth(labyrinth labrynth, String tokenColor, String nodeName) {
        //insert code here based on implementation of Labrynth
        labrynth.addColoredToken(nodeName, tokenColor);
    }

    public static void moveTokenInLabrynth(labyrinth labrynth, String tokenColor, String nodeName) {
        //insert code here based on implementation of Labrynth
        labrynth.moveToken(tokenColor, nodeName);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        labyrinth labrynth = new labyrinth();
        boolean first = true;
        while (true) {
            String line = scanner.nextLine();
            try {
                Object obj = new JSONParser().parse(line);
                JSONArray ja = (JSONArray) obj;
                Iterator itr = ja.iterator();
                String command = itr.next().toString();
                //chooses action based on first element of JSON array
                switch (command) {
                    case "lab":
                        if (!first) {
                            throw new IllegalArgumentException("Invalid JSON input, labrynth command must be first command");
                        }
                        first = false;
                        createLabrynthFromJSON(labrynth, itr);
                        break;
                    case "add":
                        addTokenToLabrynthFromJSON(labrynth, itr);
                        break;
                    case "move":
                        moveTokenInLabrynthFromJSON(labrynth, itr);
                        break;
                    default:
                        //incorrect Json command, exit program
                        scanner.close();
                        throw new IllegalArgumentException("Invalid JSON input");
                }
            }
            catch (Exception ex) {
                //incorrectly formed JSON expression
                ex.printStackTrace();
                scanner.close();
                throw new IllegalArgumentException("Invalid JSON input");
            }
        }
    }
}

/**
 * A Labyrinth is a simple graph. Each node in the graph has at most one outgoing node.
 */
public interface ILabyrinth {

    /**
     * Find the name of all nodes in the labyrinth.
     * @return List containing the names of all nodes in this labyrinth.
     */
    ArrayList<String> getAllNodeNames();

    /**
     * Find the neighbors of the named node.
     * @param name Name of the node to look for neighbors.
     * @return List containing the names of all nodes connected to the node name input.
     * @throws IllegalArgumentException If name does not exist within the labyrinth.
     */
    ArrayList<String> getConnectedNodeNames(String name) throws IllegalArgumentException;

    /**
     * Add a new named node to the labyrinth.
     * @param name name of the node to be added.
     * @throws IllegalArgumentException if the name already exists within the labyrinth.
     */
    void addNode(String name) throws IllegalArgumentException;

    /**
     * Connect node named node1 to node named node2.
     * @param nodeName1 name of the first node.
     * @param nodeName2 name of the second node.
     * @throws IllegalArgumentException If the nodeName does not exist within the labyrinth or
     * If there is already a connection between two nodes.
     */
    void connectNode(String nodeName1, String nodeName2) throws IllegalArgumentException;

    /**
     * Mutates this labyrinth by adding a colored token specified by color to the node named name.
     * Do nothing if the node already has a token of given color.
     * @param name name of the node.
     * @param color Color of the token.
     * @throws IllegalArgumentException If there is no node with the name.
     */
    void addColoredToken(String name, String color) throws IllegalArgumentException;

    /**
     * Check if a node with a inputted colored token can reach another node.
     * @param color color of the token.
     * @param name name of the node to reach out to.
     * @return Returns true if there is a path from a node of the given color to the node named name.
     *         Otherwise, returns false.
     * @throws IllegalArgumentException If there is no node with the name.
     */
    boolean canColoredTokenReachNamedNode(String color, String name) throws IllegalArgumentException;

}

public class labyrinth implements ILabyrinth {

    private ArrayList<node> nodes;
    private ArrayList<token> tokens;

    public labyrinth() {
        this.nodes = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }


    @Override
    public ArrayList<String> getAllNodeNames() {
        ArrayList<String> nodeNames = new ArrayList<>();
        for (node n : nodes) {
            nodeNames.add(n.getName());
        }
        return nodeNames;
    }

    @Override
    public ArrayList<String> getConnectedNodeNames(String name) throws IllegalArgumentException {

        ArrayList<String> answer = new ArrayList<>();

        if(!this.nodeNameExists(name)) {
            throw new IllegalArgumentException("Node name \"" + name + "\" does not exist within this labyrinth");
        }

        node node1 = this.getNodebyName(name);

        for (node n : node1.getNeighbors()) {
            answer.add(n.getName());
        }

        return answer;

    }

    /**
     * Check if the inputted name node already exists in this labyrinth.
     * @param name name of the node.
     * @return True if node exists in the labyrinth. False if not.
     */
    private boolean nodeNameExists(String name) {
        for (String s : this.getAllNodeNames()) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the node by its node name.
     * @param name name of the node.
     * @return the node with inputted name.
     */
    private node getNodebyName(String name) {
        for (node n : nodes) {
            if (n.getName().equals(name)) {
                return n;
            }
        }
        throw new IllegalArgumentException("no node with the name: \"" + name + "\"");
    }

    @Override
    public void addNode(String name) throws IllegalArgumentException {
        if (this.nodeNameExists(name)) {
            throw new IllegalArgumentException("Node named \"" + name + "\" already exists within this labyrinth");
        }

        node newNode = new node(name);

        this.nodes.add(newNode);

    }

    @Override
    public void connectNode(String nodeName1, String nodeName2) throws IllegalArgumentException {
        if (!(this.nodeNameExists(nodeName1) && this.nodeNameExists(nodeName2))) {
            throw new IllegalArgumentException("Node name does not exist within this labyrinth");
        }

        node node1 = this.getNodebyName(nodeName1);
        node node2 = this.getNodebyName(nodeName2);

        if (this.isConnected(node1, node2)) {
            throw new IllegalArgumentException("Nodes are already connected");
        }

        node1.addNeighbors(node2);
        node2.addNeighbors(node1);

        System.out.println("Connected Node \"" + nodeName1 + "\" and Node \"" + nodeName2 + "\".");

    }

    /**
     * Check if the two nodes are already connected.
     * @param node1
     * @param node2
     * @return true if the nodes are already connected.
     */
    private boolean isConnected(node node1, node node2) {
        for (node n : node1.getNeighbors()) {
            if (n.getName().equals(node2.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean tokenExists(String color) {
        for (token t : tokens) {
            if (t.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addColoredToken(String name, String color) throws IllegalArgumentException {
        if (!this.nodeNameExists(name)) {
            throw new IllegalArgumentException("Node name \"" + name + "\" does not exist within this labyrinth");
        }
        node n = this.getNodebyName(name);
        if (!this.tokenExists(color) && !n.getHasToken()) {
            token t = new token(color);
            this.tokens.add(t);



            t.addNode(n);
            n.setHasToken();

            System.out.println("Added Token Color \"" + color + "\" to Node \"" + name + "\".");
        } else {
            throw new IllegalArgumentException("Two different colors can't be added to a node");
        }
    }

    @Override
    public boolean canColoredTokenReachNamedNode(String color, String name) throws IllegalArgumentException {
        if (!this.nodeNameExists(name)) {
            throw new IllegalArgumentException("Node name \"" + name + "\" does not exist within this labyrinth");
        }

        token t = this.getTokenbyColor(color);
        node from = t.getNode();
        node to = this.getNodebyName(name);

        return from.reachable(to);
    }

    /**
     * Get the token by its color.
     * @param color color of the token.
     * @return token with the inputted color.
     */
    private token getTokenbyColor(String color) {
        for (token t : tokens) {
            if (t.getColor().equals(color)) {
                return t;
            }
        }
        throw new IllegalArgumentException("no token with the color: \"" + color + "\"");
    }

    /**
     * Move the token from a node to a node when they are connected.
     * @param color color of the token.
     * @param name name of the to node.
     * @throws IllegalArgumentException when token can be reached to the node.
     */
    public void moveToken(String color, String name) {
        if (this.canColoredTokenReachNamedNode(color, name)) {
            token t = this.getTokenbyColor(color);
            node to = this.getNodebyName(name);

            t.addNode(to);

            System.out.println("Moved Token \"" + color + "\" to Node \"" + name + "\".");

        } else {
            throw new IllegalArgumentException("Token cannot reach to a node.");
        }
    }

    /**
     * Create a node if the name does not exist within the labyrinth. Don't do anything if the name already exists.
     * @param name name of the node.
     */
    public void addNodeAnyway(String name) {
        if (!this.nodeNameExists(name)) {


            node newNode = new node(name);

            this.nodes.add(newNode);
        }
    }


}

/**
 * Node is an object that has a name that is unique within the graph.
 */
public class node {

    private String name;
    private ArrayList<node> neighbors;
    private boolean hasToken;

    public node(String name) {
        this.name = name;
        this.neighbors = new ArrayList<>();
        this.hasToken = false;
    }

    public boolean getHasToken() {
        return this.hasToken;
    }

    public void setHasToken() {
        this.hasToken = true;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<node> getNeighbors(){
        return this.neighbors;
    }

    public void addNeighbors(node node) {
        this.neighbors.add(node);
    }

    public boolean reachable(node destination) {
        ArrayList<node> frontier = new ArrayList<>(neighbors);
        ArrayList<node> visited = new ArrayList<>(neighbors);

        while (frontier.size() > 0) {
            //gets the first node in the frontier, removes it from the frontier
            //and adds it to visited
            node neighbor = frontier.get(0);
            frontier.remove(0);
            //check if the node is the node we are trying to reach
            if (neighbor == destination) {
                return true;
            }
            //if not, add it's neighbors to the list(as long as they have not been visited
            //and continue search
            for (node n : neighbor.getNeighbors()) {
                if (!visited.contains(n)) {
                    frontier.add(n);
                    visited.add(n);
                }
            }
        }
        return false;
    }

}

/**
 * Token is an Object with a unique named color.
 */
public class token {

    private String color;
    private node node;

    public token(String color) {

        if (color.equals("white") ||
                color.equals("black") ||
                color.equals("red") ||
                color.equals("green") ||
                color.equals("blue")) {

            this.color = color;
            this.node = null;
        } else {
            throw new IllegalArgumentException("Wrong color");
        }
    }

    public String getColor() {
        return this.color;
    }

    public node getNode() {
        return this.node;
    }

    public void addNode(node n) {
        this.node = n;
    }

}



