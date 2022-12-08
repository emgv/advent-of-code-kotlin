package Y2022.Day8P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day8P1Solver(val args: Array<String>) : ISolver {

    override fun run() {
        val gridInput = Util.getPuzzleInput(args)
        val grid = parseInput(gridInput)
        val w = grid[0].size
        val h = grid.size
        var visibleCount = 0

        for(r in 1 until h-1) {
            for(c in 1 until w-1) {

                if(isVisible(grid, w, h, r, c, 1, 0)) {
                    ++visibleCount
                    continue
                }
                if(isVisible(grid, w, h, r, c, 0, 1)) {
                    ++visibleCount
                    continue
                }
                if(isVisible(grid, w, h, r, c, -1, 0)) {
                    ++visibleCount
                    continue
                }
                if(isVisible(grid, w, h, r, c, 0, -1)) {
                    ++visibleCount
                    continue
                }
            }
        }

        println(visibleCount + w * 2 + (h-2) * 2)
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

    private fun isVisible(grid: List<List<Int>>, w: Int, h: Int, row: Int, col: Int, rowIncr: Int, colIncr: Int) : Boolean {

        val height = grid[row][col]
        var r = row + rowIncr
        var c = col + colIncr

        while(r in 0 until h
            && c in 0 until w) {

            val treeH = grid[r][c]
            if(treeH >= height)
                return false

            c += colIncr
            r += rowIncr
        }

        return true
    }
}