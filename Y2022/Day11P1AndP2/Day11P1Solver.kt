package Y2022.Day11P1AndP2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day11P1AndP2Solver(val args: Array<String>) : ISolver {

    class Monkey(
        val items: MutableList<Long>,
        val operation: (item: Long) -> Long,
        val test: (value: Long) -> Int,
        val divisor: Long,
        var inspectCount: Long = 0L) {

    }

    override fun run() {
        val monkeysInput = Util.getPuzzleInput(args)
        val monkeys = parseInput(monkeysInput)
        // val maxRounds = 20 // => Problem-1
        val maxRounds = 10000 // => Problem-2
        val magicNumber = monkeys.map { it.divisor }.reduce { acc, it -> it * acc }

        repeat(maxRounds) {
            for (m in 0 until monkeys.size) {
                val monkey = monkeys[m]
                if(monkey.items.isEmpty())
                    continue

                while(!monkey.items.isEmpty()) {
                    val oldWorryLevel = monkey.items.removeFirst()
                    //var worryLevel = monkey.operation(oldWorryLevel) / 3 // => Problem-1
                    var worryLevel = monkey.operation(oldWorryLevel) % magicNumber // => Problem-2
                    val sendTo = monkey.test(worryLevel)

                    ++monkey.inspectCount
                    monkeys[sendTo].items.add(worryLevel)
                }
            }
        }

        for(i in 0 until monkeys.size) {
            println("${i}: ${monkeys[i].inspectCount}")
        }

        println(monkeys
            .map { it.inspectCount }
            .sortedByDescending { it }
            .take(2)
            .reduce { acc, it -> acc * it }
        )
    }

    private fun parseInput(input: List<String>): List<Monkey> {
        var result = mutableListOf<Monkey>()
        var i = 0

        while(i < input.size) {
            val line = input[i]

            if(line.startsWith("Monkey ", true)) {
                val items = parseItems(input[++i])
                val operation = parseOperation(input[++i])
                val test = parseTest(input[++i], input[++i], input[++i])
                result.add(
                    Monkey(
                        items,
                        operation,
                        test.first,
                        divisor = test.second
                    )
                )
            }

            ++i
        }

        return result
    }

    private fun parseItems(input: String): MutableList<Long> {
        val colon = input.indexOf(':')
        if(colon < 0)
            throw Exception("Incorrect \"Starting items\" section")

        return input
            .substring(colon+1)
            .split(',')
            .map { it.trim().toLong() }
            .toMutableList()
    }

    private fun parseOperation(input: String): (item: Long) -> Long {
        val formula = input.indexOf('=')
        if(formula < 0)
            throw Exception("Incorrect \"Operation\" section")

        val rightHand = input
            .substring(formula+1)
            .trim()
            .lowercase()
            .split(' ')

        return when {
            rightHand[1] == "+" -> when {
                rightHand[2] == "old" -> { item -> item + item }
                else -> { item -> item + rightHand[2].toLong() }
            }
            rightHand[1] == "*" -> when {
                rightHand[2] == "old" -> { item -> item * item }
                else -> { item -> item * rightHand[2].toLong() }
            }
            else -> throw Exception("Incorrect \"Operation\" section")
        }
    }

    private fun parseTest(inputLine1: String, inputLine2: String, inputLine3: String): Pair<(value: Long) -> Int, Long> {
        val inputsInfo = listOf(inputLine1, inputLine2, inputLine3)
            .map { it.trim().split(' ').last().toLong() }

        val divisibleBy = inputsInfo[0]
        if(divisibleBy == 0L)
            throw Exception("Incorrect \"Test\" section: division by 0")

        val ifTrue = inputsInfo[1].toInt()
        val ifFalse = inputsInfo[2].toInt()

        return Pair({ value -> if(value % divisibleBy == 0L) ifTrue else ifFalse }, divisibleBy)
    }
}