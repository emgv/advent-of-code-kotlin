package Y2022.Day12P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.awt.GradientPaint
import java.lang.Exception

class Day12P1Solver(val args: Array<String>) : ISolver {

    class GraphNode(val row: Int,
                    val col: Int,
                    val name: Char,
                    val height: Char,
                    var visited: Boolean = false,
                    var addedToTheQueue: Boolean = false,
                    var stepsFromSource: Int = -1,
                    var prevNodeShortestPath: GraphNode? = null) {

        private val reachableNeighbors = mutableListOf<GraphNode>()

        fun addReachableNeighbor(neighbor: GraphNode): Boolean {
            if(height+1 >= neighbor.height) {
                reachableNeighbors.add(neighbor)
                return true
            }
            return false
        }

        fun getReachableNeighbor(): List<GraphNode> {
            return reachableNeighbors
        }

        fun eq(other: GraphNode): Boolean {
            return row == other.row
                && col == other.col
        }
    }

    override fun run() {
        val mapInput = Util.getPuzzleInput(args)
        val (graphNodes, initialAndDest) = parseInput(mapInput)
        val initialNode = initialAndDest.first
        val destination = initialAndDest.second
        val path = dijkstraShortestPath(graphNodes, initialNode, destination)

        println(path.size-1)
    }

    private fun parseInput(input: List<String>): Pair<Array<Array<GraphNode>>, Pair<GraphNode, GraphNode>> {
        val result = mutableListOf<GraphNode>()
        val heightMap = input.map { it.toCharArray() }
        val height = heightMap.size
        val width = heightMap.first().size
        var initialNode: GraphNode? = null
        var destination: GraphNode? = null
        val nodes = Array(height) {
            r ->
            Array(width) { c ->
                val name = heightMap[r][c]
                val height = when(name) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> name
                }
                GraphNode(row = r, col = c, name, height)
            }
        }

        for (r in 0 until height) {
            for (c in 0 until width) {
                val node = nodes[r][c]

                if(r-1 >= 0) node.addReachableNeighbor(nodes[r-1][c]) // up
                if(r+1 < height) node.addReachableNeighbor(nodes[r+1][c]) // down
                if(c-1 >= 0) node.addReachableNeighbor(nodes[r][c-1]) // left
                if(c+1 < width) node.addReachableNeighbor(nodes[r][c+1]) // right
                result.add(node)

                if(node.name == 'S')
                    initialNode = node

                if(node.name == 'E')
                    destination = node
            }
        }

        return Pair(nodes, Pair(initialNode!!, destination!!))
    }

    private fun dijkstraShortestPath(graphNodes: Array<Array<GraphNode>>, initialNode: GraphNode, destination: GraphNode): List<GraphNode> {
        val queue = mutableListOf<Triple<Int, Int, Int>>()

        initialNode.stepsFromSource=0
        queue.add(Triple(initialNode.row, initialNode.col, initialNode.stepsFromSource))

        var foundDestination = false

        while(!queue.isEmpty() && !foundDestination) {
            val visitedNodeIndices = queue.removeFirst()
            val visitedNode = graphNodes[visitedNodeIndices.first][visitedNodeIndices.second]
            visitedNode.visited = true

            val neighbors = visitedNode.getReachableNeighbor().filter { !it.visited && !it.addedToTheQueue }
            for(i in 0 until neighbors.size) {
                val neighbor = neighbors[i]
                neighbor.stepsFromSource= visitedNode.stepsFromSource+1
                neighbor.prevNodeShortestPath = visitedNode

                if(neighbor.eq(destination)) {
                    foundDestination = true
                    break
                }

                addToQueue(queue, graphNodes, neighbor)
            }
        }

        if(!foundDestination)
            throw Exception("destination not found!")
        return traceBackPath(destination, initialNode).reversed()
    }

    private fun traceBackPath(finalNode: GraphNode, initialNode: GraphNode): List<GraphNode> {
        val result = mutableListOf<GraphNode>()

        var node = finalNode
        while(true) {
            result.add(node)
            if(node.eq(initialNode))
                break
            node = node.prevNodeShortestPath!!
        }

        return result
    }

    private fun addToQueue(sortedQueue: MutableList<Triple<Int, Int, Int>>, graphNodes: Array<Array<GraphNode>>, node: GraphNode) {

        if(sortedQueue.isEmpty()) {
            node.addedToTheQueue = true
            sortedQueue.add(Triple(node.row, node.col, node.stepsFromSource))
            return
        }

        if(sortedQueue.size == 1) {
            var indices = sortedQueue[0]
            var nodeToCompare = graphNodes[indices.first][indices.second]

            if(node.stepsFromSource < nodeToCompare.stepsFromSource)
                sortedQueue.add(0, Triple(node.row, node.col, node.stepsFromSource))
            else
                sortedQueue.add(Triple(node.row, node.col, node.stepsFromSource))

            node.addedToTheQueue = true
            return
        }

        var lowerBound: Int = 0
        var upperBound: Int = sortedQueue.lastIndex

        while(upperBound - lowerBound > 1) {

            val middle = lowerBound + (upperBound - lowerBound) / 2
            val currentNodeIndices = sortedQueue[middle]
            val currentNode = graphNodes[currentNodeIndices.first][currentNodeIndices.second]

            if(node.stepsFromSource < currentNode.stepsFromSource) {
                upperBound = middle - 1
            } else {
                lowerBound = middle + 1
            }
        }

        var indices = sortedQueue[lowerBound]
        var nodeToCompare = graphNodes[indices.first][indices.second]
        if(node.stepsFromSource < nodeToCompare.stepsFromSource) {
            sortedQueue.add(Math.max(0, lowerBound-1), Triple(node.row, node.col, node.stepsFromSource))
            node.addedToTheQueue = true
            return
        }

        indices = sortedQueue[upperBound]
        nodeToCompare = graphNodes[indices.first][indices.second]
        if(node.stepsFromSource < nodeToCompare.stepsFromSource) {
            sortedQueue.add(lowerBound+1, Triple(node.row, node.col, node.stepsFromSource))
            node.addedToTheQueue = true
            return
        }

        node.addedToTheQueue = true
        sortedQueue.add(upperBound+1, Triple(node.row, node.col, node.stepsFromSource))
    }
}