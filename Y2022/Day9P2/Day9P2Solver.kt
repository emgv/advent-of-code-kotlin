package Y2022.Day9P2

import shared.advent.emgvivas.interfaces.ISolver
import shared.advent.emgvivas.util.Util
import java.lang.Exception

class Day9P2Solver(val args: Array<String>) : ISolver {

    enum class MovementTypeEnum(val xIncr: Int, val yIncr: Int) {
        Up(0, -1),
        Down(0, 1),
        Right(1, 0),
        Left(-1, 0),
        NorthWest(-1,-1),
        NorthEast(1,-1),
        SouthWest(-1,1),
        SouthEast(1,1)
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

        fun getMovementType(point: Vec2, destination: Vec2): MovementTypeEnum {
            val directionalVector = destination.sub(point)

            return when {
                directionalVector.x < 0 && directionalVector.y < 0 -> MovementTypeEnum.NorthWest
                directionalVector.x < 0 && directionalVector.y > 0 -> MovementTypeEnum.SouthWest
                directionalVector.x > 0 && directionalVector.y < 0 -> MovementTypeEnum.NorthEast
                directionalVector.x > 0 && directionalVector.y > 0 -> MovementTypeEnum.SouthEast
                directionalVector.x > 0 && directionalVector.y == 0 -> MovementTypeEnum.Right
                directionalVector.x < 0 && directionalVector.y == 0 -> MovementTypeEnum.Left
                directionalVector.x == 0 && directionalVector.y > 0 -> MovementTypeEnum.Down
                directionalVector.x == 0 && directionalVector.y < 0 -> MovementTypeEnum.Up
                else -> throw Exception("Could not convert the directional vector into a movement type")
            }
        }

        fun distanceSquared(point1: Vec2, point2: Vec2): Int {
            return point2.sub(point1).lengthSquared()
        }
    }

    class Head(var position: Vec2 = Vec2()) {
        override fun toString(): String {
            return position.toString()
        }
    }

    class Tail(var position: Vec2 = Vec2(), val isLast: Boolean = false) {
        override fun toString(): String {
            return position.toString()
        }

        companion object {
            fun createList(size: Int, position: Vec2): List<Tail> {
                return MutableList(size)
                    { index ->
                        Tail(
                            position = Vec2(position.x, position.y),
                            isLast = index == size - 1
                        )
                    }
            }
        }
    }

    val start = Vec2(0,5)
    var headCurrentPos = Vec2(0,5)

    override fun run() {
        val movementsInput = Util.getPuzzleInput(args)
        val moves = parseInput(movementsInput)
        val head = Head(start)
        val tails = Tail.createList(9, start)
        val visited = mutableListOf<Vec2>()

        visited.add(Vec2(0,0))

        moves.forEach { move ->
            moveHead(head, tails, move, visited)
            // println("H:${head.position}")
        }

        println(visited.count())
        // println("H:${head.position}")
        // println("^***")
    }

    private fun moveHead(head: Head, tails: List<Tail>, move: Movement, visited: MutableList<Vec2>) {
        val tail = tails.first()
        var printCount = 0

        // println("==${move.type} ${move.steps}==")
        headCurrentPos = Simulator.moveTo(head.position, move.type, move.steps)

        for(i in 1 .. move.steps) {
            val headVisit = Simulator.moveTo(head.position, move.type, i)
            val isTouching = Simulator.distanceSquared(tail.position, headVisit) <= 2

            if(!isTouching) {
                val previousHeadVisit = Simulator.moveTo(head.position, move.type, i-1)
                tail.position = Simulator.moveTo(tail.position, previousHeadVisit)

                // printGrid(Head(headVisit), tails, 6, 6, 0, 0, "»${0}")
                ++printCount

                moveTails(Tail(tail.position), tails, 1, visited)
            }
        }

        head.position = Simulator.moveTo(head.position, move.type, move.steps)
        // printGrid(Head(head.position), tails, 6, 6, 0, 0, "^^^^^^^»${0}")
    }

    private fun moveTails(tailToFollow: Tail, tails: List<Tail>, tailStartIndex: Int, visited: MutableList<Vec2>) {

        var currentTailToFollor = tailToFollow

        for(i in tailStartIndex until tails.size) {
            val tail = tails[i]
            val isTouching = Simulator.distanceSquared(tail.position, currentTailToFollor.position) <= 2

            if(isTouching)
                break

            val move = Simulator.getMovementType(tail.position, currentTailToFollor.position)
            tail.position = Simulator.moveTo(tail.position, move, 1)
            currentTailToFollor = tail

            // printGrid(Head(start), tails, 6, 6, 0, 0, "»${1}")

            if(tail.isLast)
                insertIfNotExists(visited, tail.position)
        }
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

    private fun printGrid(head: Head, tails: List<Tail>, width: Int, height: Int, offsetW: Int, offsetH: Int, p: String = "") {
        for(h in offsetH until height) {
            for(w in offsetW until width) {

                val indexTail = tails.indexOfFirst { t -> t.position.eq(w, h) }
                if(indexTail >= 0) {
                    print(indexTail+1)
                }
                else {
                    print(
                        when {
                            start.eq(w, h) -> "s"
                            head.position.eq(w, h) -> "H"
                            headCurrentPos.eq(w, h) -> "F"
                            else -> "."
                        }
                    )
                }
            }
            println()
        }
        println(p)
    }
}