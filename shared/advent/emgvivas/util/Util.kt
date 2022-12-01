package shared.advent.emgvivas.util

import java.io.File

object Util {
    fun getPuzzleInput(args: Array<String>): List<String>
    {
        if(args.isEmpty())
            throw IllegalArgumentException("Please provide the puzzle input file.")

        val input = File(args[0])

        if(!input.exists())
            throw IllegalArgumentException("Input file does not exists.")

        var result = mutableListOf<String>()

        input.forEachLine()
        {
            result.add(it)
        }

        return result
    }
}

