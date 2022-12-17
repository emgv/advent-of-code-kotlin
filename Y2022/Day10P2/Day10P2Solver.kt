package Y2022.Day10P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day10P2Solver(val args: Array<String>) : ISolver {

    enum class CommandType(val value: Int) {
        NOOP(0),
        ADDX(1)
    }

    data class Command(val type: CommandType, val value: Int, val cycles: Int)

    override fun run() {
        val commandsInput = Util.getPuzzleInput(args)
        val commands = parseInput(commandsInput)
        var sprite = "###....................................."
        val screenRows = mutableListOf("", "", "", "", "", "")
        var currentRowIndex = 0
        val screenWidth = 40
        var totalX = 1
        var currentPos = 0

        for(cmdIndex in 0 until commands.size) {

            screenRows[currentRowIndex] = screenRows[currentRowIndex] + sprite[currentPos++]

            if(screenRows[currentRowIndex].length == screenWidth) {
                currentPos = 0
                ++currentRowIndex

                if(currentRowIndex > screenRows.lastIndex)
                    break
            }

            val command = commands[cmdIndex]
            if(command.type == CommandType.ADDX) {
                screenRows[currentRowIndex] = screenRows[currentRowIndex] + sprite[currentPos++]
                totalX += command.value
                sprite = updateSpritePosition(totalX)
            }

            if(screenRows[currentRowIndex].length == screenWidth) {
                currentPos = 0
                ++currentRowIndex

                if(currentRowIndex > screenRows.lastIndex)
                    break
            }
        }

        screenRows.forEach {
            println(it)
        }
    }

    private fun updateSpritePosition(currentX: Int): String {
        var sprite = "###"

        if(currentX > 0)
            sprite = sprite.padStart(currentX + sprite.length-1, '.')

        return sprite.padEnd(40,'.')
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