package Y2021.Day3P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day3P1Solver(val args: Array<String>) : ISolver {

    data class ReportNumber
    (
        val BitsAsString: String,
        val BitsAsUInt: UInt
    )

    data class BitCounter
    (
        var Bit0: ULong = 0UL,
        var Bit1: ULong = 0UL
    )

    override fun run() {
        val submarineDiagnosticReportInput = Util.getPuzzleInput(args)
        val reportInput = parseReport(submarineDiagnosticReportInput)
        val bitsInfo = getBitsInfo(reportInput)

        var gamma = ""
        var epsilon = ""
        bitsInfo.forEach {
            val mostCommon = if(it.Bit0 > it.Bit1) "0" else "1"
            gamma += mostCommon
            epsilon += if(mostCommon == "0") "1" else "0"
        }

        println("Answer: ${gamma.toUInt(2) * epsilon.toUInt(2)} (${gamma} ${epsilon})")
    }

    private fun parseReport(input: List<String>) : List<ReportNumber> {
        val result = mutableListOf<ReportNumber>()

        input.forEach {
            result.add(ReportNumber(it, it.toUInt(2)))
        }

        return result
    }

    private fun getBitsInfo(input: List<ReportNumber>) : List<BitCounter> {

        val bitsCounter = mutableListOf<BitCounter>()

        input.forEach {
            for (i in 0 until it.BitsAsString.length) {
                if(bitsCounter.size <= i)
                    bitsCounter.add(BitCounter())

                if(it.BitsAsString[i] == '0')
                    ++bitsCounter[i].Bit0

                if(it.BitsAsString[i] == '1')
                    ++bitsCounter[i].Bit1
            }
        }

        return bitsCounter
    }
}