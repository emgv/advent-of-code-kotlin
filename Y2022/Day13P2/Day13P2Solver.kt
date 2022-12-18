package Y2022.Day13P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day13P2Solver(val args: Array<String>) : ISolver {

    data class NumberToken(val value: Int, val endIndex: Int)
    class PacketList(val list: MutableList<Any>,
                     val parent: PacketList? = null,
                     val isRoot: Boolean = false,
                     val isDivider: Boolean = false,
                     val isEndPacket: Boolean = false) {

        fun compare(other: PacketList): Int {

            return compareLists(this, other)
        }

        private fun addToStack(
            stack: MutableList<Pair<PacketList, PacketList>>,
            list1: PacketList,
            list2: PacketList,
            offset: Int) {

            var subStack = mutableListOf<Pair<PacketList, PacketList>>()
            var k = offset
            while(k <= list1.list.lastIndex || k <= list2.list.lastIndex) {

                var leftItem: PacketList = when {
                    k <= list1.list.lastIndex -> when {
                        list1.list[k] is PacketList -> list1.list[k] as PacketList
                        else -> PacketList(mutableListOf(list1.list[k]), list1)
                    }
                    else -> PacketList(mutableListOf(), list1, isEndPacket = true)
                }

                var rightItem: PacketList = when {
                    k <= list2.list.lastIndex -> when {
                        list2.list[k] is PacketList -> list2.list[k] as PacketList
                        else -> PacketList(mutableListOf(list2.list[k]), list2)
                    }
                    else -> PacketList(mutableListOf(), list2, isEndPacket = true)
                }

                subStack.add(0, Pair(leftItem, rightItem))

                if(leftItem.isEndPacket || rightItem.isEndPacket)
                    break

                ++k
            }

            stack.addAll(subStack)
        }

        private fun compareLists(list1: PacketList, list2: PacketList): Int {

            val stack = mutableListOf<Pair<PacketList, PacketList>>()
            addToStack(stack, list1, list2, 0)

            while(stack.isNotEmpty()) {
                val lists = stack.removeLast()
                val currentList1 = lists.first
                val currentList2 = lists.second

                // print("Compare "); prettyPrint(currentList1); print(" vs "); prettyPrint(currentList2); println();

                var i = 0
                while(true) {

                    if(!currentList1.isEndPacket
                        && !currentList2.isEndPacket
                        && i > currentList1.list.lastIndex
                        && i > currentList2.list.lastIndex)
                        break

                    if(i > currentList1.list.lastIndex || currentList1.isEndPacket)
                        return -1

                    if(i > currentList2.list.lastIndex || currentList2.isEndPacket)
                        return 1

                    var leftItem: Any = currentList1.list[i]
                    var rightItem: Any = currentList2.list[i]

                    if(leftItem is Int && rightItem is Int) {
                        val leftValue = leftItem as Int
                        val rightValue = rightItem as Int

                        // println("  Compare ${leftValue} vs ${rightValue}"); println()
                        if(leftValue < rightValue)
                            return -1

                        if(leftValue > rightValue)
                            return 1
                    }
                    else {

                        // println("pushed to the stack ")
                        // improve make a slice with offset i for currentList1 and currentList2
                        addToStack(stack, currentList1, currentList2, i)
                        break
                    }

                    ++i
                }
            }

            return 0
        }

        fun prettyPrint(packetList: PacketList) {

            print("[")
            packetList.list.forEach {
                if(it is PacketList)
                    prettyPrint(it)

                if(it is Int)
                    print("${it},")
            }
            print("]")
        }
    }

    override fun run() {
        val packetsInput = Util.getPuzzleInput(args)
        val packets = parseInput(packetsInput)
            .flatMap { it.toList() }
            .toMutableList()

        packets.
            addAll(
                listOf(
                    PacketList(mutableListOf(PacketList(mutableListOf(2))), parent = null, isRoot = true, isDivider = true),
                    PacketList(mutableListOf(PacketList(mutableListOf(6))), parent = null, isRoot = true, isDivider = true)
                )
            )

        packets.sortWith { a, b -> a.compare(b) }

        val divider1 = packets.indexOfFirst { it -> it.isDivider }
        val divider2 = packets.indexOfLast { it -> it.isDivider }

        println((divider1+1)*(divider2+1))
    }

    private fun parseInput(input: List<String>): List<Pair<PacketList, PacketList>> {

        val result = mutableListOf<Pair<PacketList, PacketList>>()

        var i = 0
        while(i+1 < input.size) {
            val packetList1 = parseLine(input[i])
            val packetList2 = parseLine(input[i+1])

            result.add(Pair(packetList1, packetList2))
            i+=3
        }

        return result
    }

    private fun parseLine(line: String): PacketList {

        val result = PacketList(mutableListOf(), null, true)
        var currentList = result
        var i = 1

        while(i < line.length) {
            val currentChar = line[i]

            when {
                currentChar.isDigit() -> {
                    val numberInfo = getNumber(line, i)
                    currentList.list.add(numberInfo.value)
                    i = numberInfo.endIndex
                    continue
                }

                currentChar == '[' -> {
                    val subList = PacketList(mutableListOf(), currentList)
                    currentList.list.add(subList)
                    currentList = subList
                }

                currentChar == ']' -> {
                    if(currentList.parent != null)
                        currentList = currentList.parent!!
                }

            }
            ++i
        }

        return result
    }

    private fun getNumber(line: String, startIndex: Int): NumberToken {

        var number = ""
        var i = startIndex

        while(i < line.length && line[i].isDigit()) {
            ++i
        }

        return NumberToken(line.substring(startIndex, i).toInt(), i)
    }
}