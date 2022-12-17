package Y2022.Day10P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day10P1Solver(val args: Array<String>) : ISolver {

    enum class CommandType(val value: Int) {
        NOOP(0),
        ADDX(1)
    }

    data class Command(val type: CommandType, val value: Int, val cycles: Int)

    override fun run() {
        val commandsInput = Util.getPuzzleInput(args)
        val commands = parseInput(commandsInput)
        val intervals = listOf(20, 60, 100, 140, 180, 220)
        var totalCycles = 1
        var totalX = 1
        var currentIntervalIndex = 0
        val strengths = mutableListOf<Int>()

        for(i in 0 until commands.size) {
            val command = commands[i]
            val interval = intervals[currentIntervalIndex]

            totalCycles += command.cycles
            totalX += command.value

            if(totalCycles >= interval) {

                if(totalCycles > interval)
                    totalX -= command.value

                strengths.add(totalX * interval)

                if(totalCycles > interval)
                    totalX += command.value

                ++currentIntervalIndex
                if(currentIntervalIndex >= intervals.size)
                    break
            }
        }

        println("${strengths.sum()}")
    }

    private fun parseInput(input: List<String>): List<Command> {
        val result = mutableListOf<Command>()

        input.forEach {
            val info = it.split(' ')

            result.add(
                when(info[0].lowercase()) {
                    "noop" -> Command(CommandType.NOOP,0, 1)
                    "addx" -> Command(CommandType.ADDX, info[1].toInt(), 2)
                    else -> throw Exception("incorrect command type")
                }
            )
        }

        return result
    }
}