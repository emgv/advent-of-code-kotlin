package Y2022.Day9P1

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day9P1Solver(val args: Array<String>) : ISolver {

    enum class MovementTypeEnum(val xIncr: Int, val yIncr: Int) {
        Up(0, -1),
        Down(0, 1),
        Right(1, 0),
        Left(-1, 0)
    }

    data class Movement(val type:MovementTypeEnum, val steps:Int)
    class Vec2(val x: Int=0, val y: Int=0) {

        fun add(other: Vec2) : Vec2 {
            return Vec2(x + other.x, y + other.y)
        }

        fun sub(other: Vec2) : Vec2 {
            return Vec2(x - other.x, y - other.y)
        }

        fun mul(scalar: Int): Vec2 {
            return Vec2(x*scalar,y*scalar)
        }

        fun lengthSquared(): Int {
            return x*x + y*y
        }

        override fun toString(): String {
            return "${x}, ${y}"
        }

        fun eq(xPos: Int, yPos: Int): Boolean {
            return x == xPos && y == yPos
        }
    }

    object Simulator {
        fun moveTo(point: Vec2, movementType: MovementTypeEnum, scalar: Int): Vec2 {
            val directionalVector = Vec2(movementType.xIncr, movementType.yIncr).mul(scalar)
            return point.add(directionalVector)
        }

        fun moveTo(point: Vec2, destination: Vec2): Vec2 {
            val directionalVector = destination.sub(point)
            return point.add(directionalVector)
        }
    }

    class Head(var position: Vec2 = Vec2()) {
        override fun toString(): String {
            return position.toString()
        }
    }

    class Tail(var position: Vec2 = Vec2()) {
        override fun toString(): String {
            return position.toString()
        }
    }

    override fun run() {
        val movementsInput = Util.getPuzzleInput(args)
        val moves = parseInput(movementsInput)
        val start = Vec2(0,0)
        val head = Head(start)
        val tail = Tail(start)
        val visited = mutableListOf<Vec2>()

        visited.add(Vec2(0,0))

        moves.forEach { move ->

            var tailVisit = tail.position

            for(i in 1 .. move.steps) {
                val headVisit = Simulator.moveTo(head.position, move.type, i)
                val delta = headVisit.sub(tailVisit)
                val isTouching = delta.lengthSquared() <= 2

                if(!isTouching) {
                    val previousHeadVisit = Simulator.moveTo(head.position, move.type, i-1)
                    tailVisit = Simulator.moveTo(tailVisit, previousHeadVisit)
                    insertIfNotExists(visited, tailVisit)
                }
            }

            tail.position = tailVisit
            head.position = Simulator.moveTo(head.position, move.type, move.steps)
        }

        println(visited.count())
    }

    private fun parseInput(input: List<String>) : List<Movement> {
        val result = mutableListOf<Movement>()

        input.forEach {
            val info = it.split(' ')
            val moveType = when(info[0].uppercase()) {
                "U" -> MovementTypeEnum.Up
                "D" -> MovementTypeEnum.Down
                "L" -> MovementTypeEnum.Left
                "R" -> MovementTypeEnum.Right
                else -> throw Exception("Incorrect movement type")
            }

            result.add(
                Movement(moveType, info[1].toInt())
            )
        }

        return result
    }

    private fun insertIfNotExists(list: MutableList<Vec2>, vector: Vec2) {
        if(!list.any { it.y == vector.y && it.x == vector.x })
            list.add(vector)
    }
}