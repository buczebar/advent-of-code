import java.lang.Integer.max
import java.lang.Integer.min

private typealias Path = List<Position>
private typealias MutableMatrix<T> = MutableList<MutableList<T>>

private val Pair<Int, Int>.depth: Int
    get() = second

private fun List<Path>.getMaxValue(selector: (Position) -> Int) =
    flatten().maxOf { selector(it) }

private fun List<Path>.toFieldMatrix(): MutableMatrix<FieldType> {
    val maxDepth = getMaxValue { it.depth }
    val maxWidth = getMaxValue { it.x }
    val matrix = MutableList(maxDepth + 1) { MutableList(maxWidth * 2) { FieldType.AIR } }
    forEach { path ->
        path.windowed(2).forEach { (start, end) ->
            val xRange = min(start.x, end.x)..max(start.x, end.x)
            val depthRange = min(start.depth, end.depth)..max(start.depth, end.depth)
            for (x in xRange) {
                for (y in depthRange) {
                    matrix[y][x] = FieldType.ROCK
                }
            }
        }
    }
    return matrix
}

private fun MutableList<MutableList<FieldType>>.addRow(type: FieldType) {
    add(MutableList(first().size) { type })
}

fun main() {
    fun parseInput(name: String) =
        readInput(name)
            .map { line ->
                line.split(" -> ")
                    .map { path ->
                        path.split(",")
                            .toListOfInts()
                            .pair()
                    }
            }

    fun simulate(fieldMatrix: MutableMatrix<FieldType>): Int {
        fun nextPosition(position: Position): Position? {
            val directionsToCheck = listOf(Position(0, 1), Position(-1, 1), Position(1, 1))
            for (direction in directionsToCheck) {
                val newPosition = position + direction
                if (fieldMatrix[newPosition.y][newPosition.x] == FieldType.AIR) {
                    return newPosition
                }
            }
            return null
        }
        val maxDepth = fieldMatrix.size - 1
        val sandStartPosition = Position(500, 0)
        var position = sandStartPosition

        while (position.depth < maxDepth && fieldMatrix[sandStartPosition.y][sandStartPosition.x] == FieldType.AIR) {
            val newPosition = nextPosition(position)
            if (newPosition == null) {
                fieldMatrix[position.y][position.x] = FieldType.SAND
                position = sandStartPosition
            } else {
                position = newPosition
            }
        }
        return fieldMatrix.flatten().count { it == FieldType.SAND }
    }

    fun part1(fieldMatrix: MutableMatrix<FieldType>) = simulate(fieldMatrix)

    fun part2(fieldMatrix: MutableMatrix<FieldType>): Int {
        fieldMatrix.apply {
            addRow(FieldType.AIR)
            addRow(FieldType.ROCK)
        }
        return simulate(fieldMatrix)
    }

    val testInput = parseInput("Day14_test").toFieldMatrix()
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = parseInput("Day14").toFieldMatrix()
    println(part1(input))
    println(part2(input))
}

private enum class FieldType {
    AIR,
    SAND,
    ROCK
}