package Y2022.Day6P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day6P2Solver(val args: Array<String>) : ISolver {

    override fun run() {
        val streamBuffer = Util.getPuzzleInput(args)
        val (_, numCharsBefore) = getStartOfPacket(streamBuffer.first(), 14)

        println(numCharsBefore+1)
    }

    private fun getStartOfPacket(input: String, distinctCount: Int): Pair<String, Int> {
        var currentMarker = ""
        var currentMarkerStartIndex = 0
        var i = 0

        while(i < input.length) {
            val currentChar = input[i]

            if(!currentMarker.contains(currentChar)) {
                currentMarker += currentChar
            }
            else {
                currentMarker = ""
                i = currentMarkerStartIndex + 1
                currentMarkerStartIndex = i
                continue
            }

            if(currentMarker.length == distinctCount) {
                return Pair(currentMarker, i)
            }
            ++i
        }

        return Pair("", -1)
    }
}