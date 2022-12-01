package Y2022.Day1P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day1P1Solver(val args: Array<String>) : ISolver {

    data class ElfInventoryItem(val Calories : Double)
    data class ElfInventory(val Inventory : List<ElfInventoryItem>)

    override fun run() {
        val elvesFoodInventoryInput = Util.getPuzzleInput(args)
        val inventory = getInventory(elvesFoodInventoryInput)
        val invTotals = inventory.map { elf : ElfInventory -> elf.Inventory.sumOf { it.Calories } }

        println("Answer: ${invTotals.max()}")
    }

    private fun getInventory(input: List<String>) : List<ElfInventory> {
        var result = mutableListOf<ElfInventory>()
        var elfIndex : Int = 0
        lateinit var items : MutableList<ElfInventoryItem>

        for(i in 0 until input.size) {
            val inputLine = input[i]

            if(inputLine.isNullOrEmpty() || elfIndex == 0) {
                items = mutableListOf<ElfInventoryItem>()
                result.add(ElfInventory(items))
                ++elfIndex
                continue
            }

            items.add(ElfInventoryItem(inputLine.toDouble()))
        }

        return result
    }
}