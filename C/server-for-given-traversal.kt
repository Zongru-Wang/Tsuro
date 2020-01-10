import java.util.*
import kotlin.test.assertEquals


// The node class represent the nodes
class Node {

    // each node has a set of colors
    var colors: MutableSet<Color>

    // The unique string that represents the id/name of each node
    var nodeId: String

    // The node id/name that a node connect to
    var connectedTo: String

    constructor(colors: MutableSet<Color>, nodeId: String, connectedTo: String) {
        this.colors = colors
        this.nodeId = nodeId
        this.connectedTo = connectedTo
    }

}

// The Color class represent the RGB requested by rgb(HexCode)
// Now the enum class only has 3 colors, but users can add more
// or make the class no enum, but won't affect the implementation
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}


interface ILabyrinth {

    // A createLabrynth function that allows the user to create a simple graph with any amount of nodes.
    fun createLabrynth(inNodes: MutableList<Node>): Labyrinth

    // An addColoredToken function that takes in an rgb value and a node-id and puts that token on that node.
    fun addColoredToken(RGB: Color, nodeId: String)

    // A queryPath function that takes in a colored token and a destination node and returns a boolean that determines
    // whether any colored token of that color can reach that node in the graph.
    fun queryPath(RGB: Color, destNode: String): Boolean

}




class Labyrinth: ILabyrinth {

    // A map that track the set of color of each Node
    var colorMap: MutableMap<String, MutableSet<Color>> = mutableMapOf()

    // A map that stores the edges for each node
    // Key is the node name and the value stored is the set of Node name that
    // are connected to
    var edgesList: MutableMap<String, MutableSet<String>> = mutableMapOf()

    // A map that key is the name of node, and store the Node value
    private var nodes: MutableMap<String, Node> = mutableMapOf()

    // Construct the Labyrinth
    // input a list of Nodes
    // construct edgeList, nodes, and colorMap

    constructor(inNodes: MutableList<Node>) {
        if (inNodes.isNullOrEmpty()) {
            throw IllegalArgumentException()
        }

        for (node: Node in inNodes) {
            nodes[node.nodeId] = node
            colorMap[node.nodeId] = node.colors
            this.addStringToConnectedSet(node.nodeId, node.connectedTo)
            this.addStringToConnectedSet(node.connectedTo, node.nodeId)
        }
    }

    // add a node name to the Set of Node name of EdgeList Map
    private fun addStringToConnectedSet(nodeId: String, connectedTo: String) {
        if (!edgesList.containsKey(nodeId)) {
            edgesList[nodeId] = mutableSetOf()
        }
        this.edgesList[nodeId]?.add(connectedTo)
    }

    // add a color token to color map
    override fun addColoredToken(RGB: Color, nodeId: String) {
        if (nodeId in this.colorMap.keys) {
            this.colorMap[nodeId]?.add(RGB)
        }
    }

    // A queryPath function that takes in a colored token and a destination node and returns a boolean that determines
    // whether any colored token of that color can reach that node in the graph.
    override fun queryPath(RGB: Color, destNode: String): Boolean {
        if (destNode !in nodes) {
            throw IllegalArgumentException()
        }

        val closed: MutableSet<String> = mutableSetOf()
        val fringe: Stack<String> = Stack()
        fringe.push(destNode)

        while (!fringe.isEmpty()) {
            val current: String = fringe.pop()
            if (RGB in colorMap[current]!!) {
                return true
            }

            if (current !in closed) {
                closed.add(current)
                fringe.addAll(edgesList[current]!!)
            }
        }

        return false
    }

    // Use constructor for this method
    // A createLabrynth function that allows the user to create a simple graph with any amount of nodes.
    override fun createLabrynth(inNodes: MutableList<Node>): Labyrinth {
        return Labyrinth(inNodes)
    }

}


