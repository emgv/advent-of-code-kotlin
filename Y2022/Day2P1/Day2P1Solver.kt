package Y2022.Day2P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util

class Day2P1Solver(val args: Array<String>) : ISolver {

    enum class PlayerHand(val value: Int) {
        Rock(1),
        Paper(2),
        Scissors(3)
    }

    enum class RoundResultType(val value: Int) {
        Lost(0),
        Draw(3),
        Won(6)
    }

    data class RoundResult(val ResultType: RoundResultType, val TotalPoints : Int)

    class PlayerRound(roundHand : String) {

        val Hand : PlayerHand
        init {
            val roundHandUpper = roundHand.uppercase()
            Hand = when {
                roundHandUpper == "A" || roundHandUpper == "X" -> PlayerHand.Rock
                roundHandUpper == "B" || roundHandUpper == "Y" -> PlayerHand.Paper
                roundHandUpper == "C" || roundHandUpper == "Z" -> PlayerHand.Scissors
                else ->
                    throw IllegalArgumentException("Incorrect input")
            }
        }

        fun isDrawAgainst(otherPlayerHand : PlayerHand) : Boolean {
            return Hand == otherPlayerHand
        }

        fun isWinAgainst(otherPlayerHand : PlayerHand) : Boolean {
            return when {
                Hand == PlayerHand.Rock && otherPlayerHand == PlayerHand.Scissors -> true
                Hand == PlayerHand.Scissors && otherPlayerHand == PlayerHand.Paper -> true
                Hand == PlayerHand.Paper && otherPlayerHand == PlayerHand.Rock -> true
                else -> false
            }
        }
    }
    data class Round(val Opponent : PlayerRound, val Me : PlayerRound, val Result : RoundResult)

    override fun run() {
        val roundsInput = Util.getPuzzleInput(args)
        val rounds = parseInput(roundsInput)
        val totalPoints = rounds.sumOf { it.Result.TotalPoints }

        println("Answer: ${totalPoints}")
    }

    private fun parseInput(input: List<String>) : List<Round> {

        val result = mutableListOf<Round>()
        input.forEach {
            val roundData = it.split(" ")
            val opponent = PlayerRound(roundData[0])
            val me = PlayerRound(roundData[1])

            result.add(Round(
                    Opponent = PlayerRound(roundData[0]),
                    Me = PlayerRound(roundData[1]),
                    Result = calcResultForPlayer1(player1= me, player2= opponent)
            ))
        }

        return result
    }

    private fun calcResultForPlayer1(player1: PlayerRound, player2: PlayerRound) : RoundResult {
        var resultType = when {
            player1.isDrawAgainst(player2.Hand) ->  RoundResultType.Draw
            player1.isWinAgainst(player2.Hand) ->  RoundResultType.Won
            else -> RoundResultType.Lost
        }

        return RoundResult(
            ResultType =  resultType,
            TotalPoints = player1.Hand.value + resultType.value)
    }
}