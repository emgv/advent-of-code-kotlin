package Y2022.Day12P2

import Y2022.Day12P1.Day12P1Solver
import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.awt.GradientPaint
import java.lang.Exception

class Day12P2Solver(val args: Array<String>) : ISolver {

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

        fun reset() {
            visited = false
            addedToTheQueue = false
            stepsFromSource = -1
            prevNodeShortestPath = null
        }
    }

    override fun run() {
        val mapInput = Util.getPuzzleInput(args)
        val (graphNodes, initialAndDest) = parseInput(mapInput)
        val destination = initialAndDest.last()
        var shortestPath = -1

        initialAndDest
            .filterIndexed { i, _ -> i != initialAndDest.lastIndex }
            .forEach { initialNode ->
                val path = dijkstraShortestPath(graphNodes, initialNode, destination)
                if(path.size > 0 && (path.size-1 < shortestPath || shortestPath < 0))
                    shortestPath = path.size-1
            }

        println(shortestPath)
    }

    private fun parseInput(input: List<String>): Pair<Array<Array<GraphNode>>, List<GraphNode>> {
        val result = mutableListOf<GraphNode>()
        val heightMap = input.map { it.toCharArray() }
        val height = heightMap.size
        val width = heightMap.first().size
        val initialNodesAndDest = mutableListOf<GraphNode>()
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

                if(node.name == 'S' || node.name == 'a')
                    initialNodesAndDest.add(node)
                else if(node.name == 'E')
                    destination = node
            }
        }

        initialNodesAndDest.add(destination!!)
        return Pair(nodes, initialNodesAndDest)
    }

    private fun dijkstraShortestPath(graphNodes: Array<Array<GraphNode>>, initialNode: GraphNode, destination: GraphNode): List<GraphNode> {
        val queue = mutableListOf<Triple<Int, Int, Int>>()
        resetNodesState(graphNodes)

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
            return emptyList()
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

    private fun resetNodesState(graphNodes: Array<Array<GraphNode>>) {
        val height = graphNodes.size
        val width = graphNodes.first().size

        for (r in 0 until height) {
            for (c in 0 until width) {
                graphNodes[r][c].reset()
            }
        }
    }
}