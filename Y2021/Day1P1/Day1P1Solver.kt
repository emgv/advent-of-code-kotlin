package Y2021.Day1P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day1P1Solver(val args: Array<String>) : ISolver {
    override fun run() {

        val sonarInput = Util.getPuzzleInput(args)
        var increments: Int = 0

        sonarInput.forEachIndexed{ index, element ->

            if(index > 0 && element.toInt() > sonarInput[index-1].toInt())
                ++increments;
        }

        println("Answer: ${increments}")
    }
}
