package Y2022.Day3P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day3P2Solver(val args: Array<String>) : ISolver {

    class SupplyGroup(val First: String, val Second: String, val Third: String) {
        val CommonType: Char

        init {
            CommonType= getCommonItemType()
        }

        private fun getCommonItemType() : Char{
            for(i in 0 until First.length) {
                val currentChar = First[i]
                if(Second.contains(currentChar) && Third.contains(currentChar))
                    return currentChar
            }

            throw Exception("Incorrect group, there is no common item type")
        }
    }

    override fun run() {
        val suppliesInput = Util.getPuzzleInput(args)
        val suppliesGroup = parseInput(suppliesInput)
        val total = suppliesGroup.sumOf {
            it -> if(it.CommonType.isUpperCase()) (it.CommonType - 'A') + 27 else (it.CommonType - 'a') + 1
        }

        println("Answer: ${total}")
    }

    private fun parseInput(input: List<String>) : List<SupplyGroup> {

        val result = mutableListOf<SupplyGroup>()
        var i = 0

        while(i < input.size) {

            result.add(
                SupplyGroup(
                    First = input[i],
                    Second = input[i+1],
                    Third = input[i+2]
                )
            )

            i += 3
        }

        return result
    }

    private fun getOcurrencies(first: String, second: String) : List<Char> {

        var result = mutableListOf<Char>()

        for (i in 0 until first.length) {
            val currentChar = first[i]
            if (second.contains(currentChar))
                result.add(currentChar)
        }

        return result.distinct()
    }
}