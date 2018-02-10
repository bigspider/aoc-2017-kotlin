import kotlin.math.max
import kotlin.math.min

private class Graph {
    class Edge constructor(node1: Int, node2: Int) {
        val source = min(node1, node2)
        val destination = max(node1, node2)

        val strength
            get() = source + destination

        var used = false

        override fun toString() = "($source, $destination)"

        fun otherEnd(node: Int) = when (node) {
            source -> destination
            destination -> source
            else -> throw IllegalArgumentException("$node is not a node of this edge")
        }
    }
    private val adjLists = HashMap<Int, MutableList<Edge>>()

    private fun initNode(node: Int) {
        if (!adjLists.containsKey(node))
            adjLists[node] = mutableListOf()
    }

    fun edges(node: Int): List<Edge> = adjLists.getOrDefault(node, emptyList())

    fun addEdge(source: Int, dest: Int) {
        initNode(source)
        initNode(dest)

        val e = Edge(source, dest)
        adjLists[source]!!.add(e)
        adjLists[dest]!!.add(e)
    }
}

fun main(args: Array<String>) {
    val g = Graph()

    for ( (i, j) in getInputLines().map { line -> line.split("/").map { it.toInt() } })
        g.addEdge(i, j)

    var maxStrength = Int.MIN_VALUE
    var longestPathLength = Int.MIN_VALUE
    var maxLongestPathStrength = Int.MIN_VALUE

    fun visit(node: Int, curStrength: Int = 0, curLength: Int = 0) {
        maxStrength = max(maxStrength, curStrength)

        if (curLength > longestPathLength) {
            longestPathLength = curLength
            maxLongestPathStrength = curStrength
        } else if (curLength == longestPathLength) {
            maxLongestPathStrength = max(maxLongestPathStrength, curStrength)
        }

        for (e in g.edges(node).filter { !it.used }) {
            e.used = true
            visit(e.otherEnd(node), curStrength + e.strength, curLength + 1)
            e.used = false
        }
    }

    visit(0)

    println("Maximum strength: $maxStrength")
    println("Maximum strength of a longest path: $maxLongestPathStrength")
}