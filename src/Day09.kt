import java.lang.IllegalArgumentException
import kotlin.math.abs

typealias Point = Pair<Int, Int>

private fun Point.isTouching(point: Point) = abs(first - point.first) < 2 && abs(second - point.second) < 2
fun main() {
    fun parseInput(name: String) = readInput(name).map { it.splitWords() }
        .map { (direction, steps) -> direction to steps.toInt() }

    fun getAllKnotsPositions(moves: List<Pair<String, Int>>, numOfKnots: Int): List<List<Point>> {

        fun getNextHeadPosition(position: Point, direction: String) = when (direction) {
            "L" -> Pair(position.first - 1, position.second)
            "R" -> Pair(position.first + 1, position.second)
            "U" -> Pair(position.first, position.second + 1)
            "D" -> Pair(position.first, position.second - 1)
            else -> throw IllegalArgumentException("Wrong direction $direction")
        }

        fun followingPosition(currentPosition: Point, leaderPosition: Point) = Point(
            currentPosition.first + leaderPosition.first.compareTo(currentPosition.first),
            currentPosition.second + leaderPosition.second.compareTo(currentPosition.second)
        )

        val startingPoint = 0 to 0
        val knotsPositions = List(numOfKnots) { mutableListOf(startingPoint) }
        moves.forEach { (direction, steps) ->
            repeat(steps) {
                val headPositions = knotsPositions.head()
                headPositions.add(getNextHeadPosition(headPositions.last(), direction))
                knotsPositions.forEachIndexed { index, currentKnotPositions ->
                    if (index == 0) return@forEachIndexed
                    val previousKnotPosition = knotsPositions[index - 1].last()
                    val currentKnotPosition = currentKnotPositions.last()

                    if (!previousKnotPosition.isTouching(currentKnotPosition)) {
                        currentKnotPositions.add(followingPosition(currentKnotPosition, previousKnotPosition))
                    } else {
                        currentKnotPositions.add(currentKnotPosition)
                    }
                }
            }
        }
        return knotsPositions
    }

    fun part1(input: List<Pair<String, Int>>): Int {
        return getAllKnotsPositions(input, 2).last().distinct().size
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        return getAllKnotsPositions(input, 10).last().distinct().size
    }

    val testInput = parseInput("Day09_test")
    val testInputPart2 = parseInput("Day09_test2")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    check(part2(testInputPart2) == 36)

    val input = parseInput("Day09")
    println(part1(input))
    println(part2(input))
}
