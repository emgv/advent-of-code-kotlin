package Y2022.Day5P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day5P2Solver(val args: Array<String>) : ISolver {

    enum class CommandTypeEnum(val value:Int) {
        Move(1)
    }
    data class Command(val CommandType: CommandTypeEnum, val CommandValue: Int, val From: Int, val To: Int)
    data class StacksPlan(val Stacks: List<ArrayDeque<String>>, val Commands: List<Command>)

    override fun run() {
        val stacksInput = Util.getPuzzleInput(args)
        val stacksPlan = parseInput(stacksInput)

        stacksPlan.Commands.forEach { command ->
            val stackFrom = stacksPlan.Stacks[command.From-1]
            val stackTo = stacksPlan.Stacks[command.To-1]
            val crates = stackFrom.takeLast(command.CommandValue)

            crates.forEach { crate ->
                stackTo.add(crate)
                stackFrom.removeLast()
            }
        }

        val result = stacksPlan.Stacks
            .map { stack -> stack.last() }
            .reduce { acc, it -> acc + it }
            .replace("[", "")
            .replace("]","")

        println(result)
    }

    private fun parseInput(input: List<String>) : StacksPlan {

        var stacks = mutableListOf<ArrayDeque<String>>()
        val pattern = "(    |\\[[A-Z]\\]|[0-9]{1,})".toRegex(RegexOption.IGNORE_CASE)
        var stacksDone = false
        var planStartIndex = -1

        for(k in 0 until input.size) {
            val inputLine = input[k]
            val matches = pattern.findAll(inputLine)

            for(i in 0 until matches.count()) {
                val m= matches.elementAt(i)
                if(stacks.size <= i)
                    stacks.add(ArrayDeque<String>())

                val item = m.groups[0]!!.value.trim()
                val itemIsNumeric = item.toIntOrNull() != null

                if(itemIsNumeric) {
                    stacksDone = true
                    break
                }

                if(!item.isEmpty())
                    stacks[i].addFirst(item)
            }

            if(stacksDone) {
                planStartIndex = k+2
                break
            }
        }

        val commands = mutableListOf<Command>()
        for(p in planStartIndex until input.size) {
            val info = input[p].split(' ')
            val commandType = if(info[0].lowercase() == "move") CommandTypeEnum.Move else throw Exception("Incorrect input: invalid command type")

            commands.add(
                Command(
                    commandType,
                    CommandValue = info[1].toInt(),
                    From = info[3].toInt(),
                    To = info[5].toInt(),
                )
            )
        }
/*
    val str = "                        [Z] [W] [Z]    "
    val d = "(    |\\[[A-Z]\\])".toRegex().findAll(str)

    println("=>")
    d.forEach {

        println(":${it.groups[0]}")
    }
    /*=========== Output ===========
    :MatchGroup(value=    , range=0..3)
    :MatchGroup(value=    , range=4..7)
    :MatchGroup(value=    , range=8..11)
    :MatchGroup(value=    , range=12..15)
    :MatchGroup(value=    , range=16..19)
    :MatchGroup(value=    , range=20..23)
    :MatchGroup(value=[Z], range=24..26)
    :MatchGroup(value=[W], range=28..30)
    :MatchGroup(value=[Z], range=32..34)
    :MatchGroup(value=    , range=35..38)
    */
 */
        return StacksPlan(stacks, commands)
    }
}