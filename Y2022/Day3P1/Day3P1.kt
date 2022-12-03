package Y2022.Day3P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day3P1Solver(val args: Array<String>) : ISolver {

    class Supply(val FirstHalf: String, val SecondHalf: String) {
        val Ocurr : List<Char>

        init {
            Ocurr = getOcurrencies()
        }

        private fun getOcurrencies() : List<Char> {

            var result = mutableListOf<Char>()

            for (i in 0 until FirstHalf.length) {
                val currentChar = FirstHalf[i]
                if(SecondHalf.contains(currentChar))
                    result.add(currentChar)
            }

            result = result.distinct().toMutableList()
            return result
        }
    }

    override fun run() {
        val suppliesInput = Util.getPuzzleInput(args)
        val supplies = parseInput(suppliesInput)
        val total = supplies.map {
                it -> it.Ocurr.sumOf { it -> if(it.isUpperCase()) (it - 'A') + 27 else (it - 'a') + 1}
        }.sum()

        println("Answer: ${total}")
    }

    private fun parseInput(input: List<String>) : List<Supply> {

        val result = mutableListOf<Supply>()
        input.forEach {
            val half = it.length / 2
            val first = it.substring(0, half)
            val second = it.substring(half)

            result.add(
                Supply(
                    FirstHalf = first,
                    SecondHalf = second
                )
            )
        }

        return result
    }
}