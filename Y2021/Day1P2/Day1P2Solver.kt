package Y2021.Day1P2

import shared.advent.emgvivas.interfaces.ISolver
import kotlin.collections.mutableListOf
import shared.advent.emgvivas.util.*

class Day1P2Solver(val args: Array<String>) : ISolver{
    override fun run() {

        val sonarInput = Util.getPuzzleInput(args)
        var inputIndex = 0
        var threeMeasurementSum = mutableListOf<Int>()

        while(inputIndex < sonarInput.size) {

            if(inputIndex + 2 >= sonarInput.size)
                break

            var threeSum = 0
            for (m in inputIndex..(inputIndex+2))
                threeSum += sonarInput[m].toInt()

            threeMeasurementSum.add(threeSum)
            ++inputIndex
        }

        var increments: Int = 0
        threeMeasurementSum.forEachIndexed{ index, element ->

            if(index > 0 && element > threeMeasurementSum[index-1])
                ++increments;
        }

        println("Answer: ${increments}")
    }
}

