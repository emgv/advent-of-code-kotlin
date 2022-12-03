package Y2021.Day3P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day3P2Solver(val args: Array<String>) : ISolver {

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
        val bitLength = reportInput[0].BitsAsString.length

        var oxygenFilter = ""
        var cO2Filter = ""
        var oxygenList = reportInput.toMutableList()
        var cO2List = reportInput.toMutableList()

        for (i in 0 until bitLength) {

            if(oxygenList.size != 1) {
                val bitsInfo = getBitsInfo(oxygenList)
                oxygenFilter += getMostCommon(bitsInfo[i])
                oxygenList = oxygenList.filter { it -> it.BitsAsString.startsWith(oxygenFilter) }.toMutableList()
            }

            if(cO2List.size != 1) {
                val bitsInfo = getBitsInfo(cO2List)
                cO2Filter += getLeastCommon(bitsInfo[i])
                cO2List = cO2List.filter { it -> it.BitsAsString.startsWith(cO2Filter) }.toMutableList()
            }
        }

        println("Answer: ${oxygenList.first().BitsAsUInt * cO2List.first().BitsAsUInt} (${oxygenList.first().BitsAsUInt} ${cO2List.first().BitsAsUInt})")
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

    private fun getMostCommon(bitsInfo : BitCounter) : String {
        return if(bitsInfo.Bit0 > bitsInfo.Bit1) "0" else "1"
    }

    private fun getLeastCommon(bitsInfo : BitCounter) : String {
        val mostCommon = getMostCommon(bitsInfo)
        return if(mostCommon == "0") "1" else "0"
    }
}