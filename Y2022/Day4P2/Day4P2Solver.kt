package Y2022.Day4P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day4P2Solver(val args: Array<String>) : ISolver {

    class Section(val StartID: UInt, val EndID: UInt) {
        fun fullyContains(other: Section): Boolean {
            return StartID <= other.StartID
                    && EndID >= other.EndID
        }

        fun overlaps(other: Section): Boolean {
            return (StartID <= other.StartID && EndID >= other.StartID)
                    || (StartID <= other.EndID && EndID >= other.EndID)
        }
    }

    class SectionPair(val Section1: Section, val Section2: Section) {
        fun pairFullyContainsEachOther(): Boolean {
            return Section1.fullyContains(Section2)
                    || Section2.fullyContains(Section1)
        }

        fun pairOverlaps(): Boolean {
            return Section1.overlaps(Section2)
                    || Section2.overlaps(Section1)
        }
    }

    override fun run() {
        val sectionsInput = Util.getPuzzleInput(args)
        val sections = parseInput(sectionsInput)
        val countOverlaps = sections.count { it -> it.pairOverlaps() }

        println("Answer: ${countOverlaps}")
    }

    private fun parseInput(input: List<String>): List<SectionPair> {

        val result = mutableListOf<SectionPair>()
        input.forEach {
            val inputLineInfo = it
                .split(',')
                .map {
                    section ->
                    val pair = section.split('-')
                    Section(pair[0].toUInt(), pair[1].toUInt())
                }
            result.add(SectionPair(inputLineInfo[0], inputLineInfo[1]))
        }
        return result
    }
}