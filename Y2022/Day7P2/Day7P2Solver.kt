package Y2022.Day7P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day7P2Solver(val args: Array<String>) : ISolver {

    enum class NodeTypeEnum(val value:Int) {
        Root(0),
        Directory(1),
        File(2)
    }

    data class Node(
        val Name:String,
        val Parent: Node?,
        val Childs: MutableList<Node>,
        val Type:NodeTypeEnum,
        var TotalSize:UInt=0u,
        var Visited:Boolean=false
    )

    override fun run() {
        val commandsInput = Util.getPuzzleInput(args)
        val rootNode = parseInput(commandsInput)
        calcDirectorySizes(rootNode)

        val totalOSSize = 70000000
        val requiredFreeSpace = 30000000
        val currentOccupiedSize = rootNode.TotalSize.toInt()
        val currentAvailableSize = totalOSSize - currentOccupiedSize
        val needToFreeAtLeast = requiredFreeSpace - currentAvailableSize

        println(
            getDirsAtLeast(rootNode, needToFreeAtLeast.toUInt())
                .sortedBy { it.TotalSize }
                .first().TotalSize
        )
    }

    private fun parseInput(input: List<String>) : Node {
        val rootNode = Node("/", null, mutableListOf<Node>(), NodeTypeEnum.Root)
        var currentNode: Node = rootNode
        var i = 0

        while(i < input.size) {
            val c= input[i]
            val info = c.split(' ')
            if(info[0] == "$") {
                when(info[1]) {
                    "cd" -> when(info[2]) {
                        "/" -> currentNode = rootNode
                        ".." -> currentNode = currentNode.Parent!!
                        else -> {
                            currentNode = currentNode.Childs.first { nd -> nd.Name == info[2] }
                        }
                    }
                    "ls" -> {
                        var k = i+1
                        while(k < input.size) {
                            val line = input[k].split(' ')
                            when(line[0]) {
                                "$" -> break
                                "dir" -> currentNode.Childs.add(Node(line[1], currentNode, mutableListOf<Node>(), NodeTypeEnum.Directory))
                                else -> currentNode.Childs.add(Node(line[1], currentNode, mutableListOf<Node>(), NodeTypeEnum.File, line[0].toUInt()))
                            }
                            ++k
                        }
                        i=k
                        continue
                    }
                }
            }
            ++i
        }

        return rootNode
    }

    private fun calcDirectorySizes(startNode:Node) {

        var traversalStack = ArrayDeque<Node>()

        traversalStack.addLast(startNode)

        while(!traversalStack.isEmpty()) {
            val currentNode = traversalStack.last()

            when(currentNode.Type) {
                NodeTypeEnum.File -> {
                    traversalStack.removeLast()
                    currentNode.Parent!!.TotalSize += currentNode.TotalSize
                }
                NodeTypeEnum.Root -> {
                    traversalStack.removeLast()
                    traversalStack.addAll(currentNode.Childs.sortedBy { child -> child.Type.value })
                }
                else -> {
                    if(currentNode.Visited) {
                        currentNode.Parent!!.TotalSize += currentNode.TotalSize
                        traversalStack.removeLast()
                    } else {
                        currentNode.Visited = true
                        traversalStack.addAll(currentNode.Childs.sortedBy { child -> child.Type.value })
                    }
                }
            }
        }
    }

    private fun getDirsAtLeast(startNode:Node, minSize:UInt): List<Node> {

        var result = mutableListOf<Node>()
        var traversalStack = ArrayDeque<Node>()
        traversalStack.addLast(startNode)

        while(!traversalStack.isEmpty()) {
            val currentNode = traversalStack.last()
            traversalStack.removeLast()

            if(currentNode.Type == NodeTypeEnum.Directory
                || currentNode.Type == NodeTypeEnum.Root) {

                traversalStack.addAll(currentNode.Childs)

                if(currentNode.TotalSize >= minSize)
                    result.add(currentNode)
            }
        }

        return result
    }
}