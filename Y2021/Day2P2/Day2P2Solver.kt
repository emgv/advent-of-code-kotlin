package Y2021.Day2P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day2P2Solver(val args: Array<String>) : ISolver {

    data class NavigationCommand
    (
        val Command: String,
        val HorizontalIncrement: Int,
        val DepthIncrement: Int
    )
    data class Position
    (
        var X: Int, var Y: Int
    )

    override fun run() {

        val submarineNavigationInput = Util.getPuzzleInput(args)
        val submarineNavigation = parseNavigation(submarineNavigationInput)

        var initialPos = Position(0, 0)

        submarineNavigation.forEach({
            initialPos.X += it.HorizontalIncrement
            initialPos.Y += it.DepthIncrement
        })

        println("Answer: ${initialPos.X * initialPos.Y} (${initialPos.X} / ${initialPos.Y})")
    }

    fun parseNavigation(input: List<String>): List<NavigationCommand>
    {
        var result = mutableListOf<NavigationCommand>()
        var aim : Int = 0

        input.forEach {
            val commandInfo = it.lowercase().split(" ")
            val commandName = commandInfo[0]
            val commandValue = commandInfo[1].toInt()

            when (commandName) {
                "forward" -> {
                    result.add(NavigationCommand(commandName, HorizontalIncrement= commandValue, DepthIncrement = commandValue * aim))
                }
                "down" -> {
                    result.add(NavigationCommand(commandName, HorizontalIncrement= 0, DepthIncrement = 0))
                    aim += commandValue
                }
                "up" -> {
                    result.add(NavigationCommand(commandName, HorizontalIncrement= 0, DepthIncrement = 0))
                    aim -= commandValue
                }
            }
        }

        return result
    }
}