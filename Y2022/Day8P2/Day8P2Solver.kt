package Y2022.Day8P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day8P2Solver(val args: Array<String>) : ISolver {

    override fun run() {
        val gridInput = Util.getPuzzleInput(args)
        val grid = parseInput(gridInput)
        val w = grid[0].size
        val h = grid.size
        var highestScore = 0

        for(r in 1 until h-1) {
            for(c in 1 until w-1) {

                val down = dist(grid, w, h, r, c, 1, 0)
                val right = dist(grid, w, h, r, c, 0, 1)
                val up = dist(grid, w, h, r, c, -1, 0)
                val left = dist(grid, w, h, r, c, 0, -1)
                val score = down * right * up * left

                if(highestScore < score)
                    highestScore = score
            }
        }

        println(highestScore)
    }

    private fun parseInput(input: List<String>) : List<List<Int>> {
        var result = mutableListOf<List<Int>>()

        input.forEach {
            var row = mutableListOf<Int>()
            it.forEach { c ->
                row.add(c.digitToInt())
            }
            result.add(row)
        }
        return result
    }

    private fun dist(grid: List<List<Int>>, w: Int, h: Int, row: Int, col: Int, rowIncr: Int, colIncr: Int): Int {
        val height = grid[row][col]
        var r = row + rowIncr
        var c = col + colIncr
        var visibleCount = 0

        while(r in 0 until h
            && c in 0 until w) {
            ++visibleCount
            val treeH = grid[r][c]
            if(treeH >= height)
                break

            c += colIncr
            r += rowIncr
        }

        return visibleCount
    }
}