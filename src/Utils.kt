import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Reads input separated by empty line
 */
fun readGroupedInput(name: String) = File("src", "$name.txt").readText().split("\n\n")

fun List<String>.splitLinesToInts() = map { it.split("\n").toListOfInts() }

fun List<String>.toListOfInts() = map { it.toInt() }

fun <T> List<T>.allDistinct() = distinct().size == size

fun <T> List<T>.pair(): Pair<T, T> =
    if (size == 2) Pair(this[0], this[1]) else throw IllegalArgumentException("Input array has wrong size")

fun String.remove(regex: Regex) = replace(regex, "")
fun String.splitWords() = split(" ")

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val desiredSize = maxOf { it.size }
    val resultList = List<MutableList<T>>(desiredSize) { mutableListOf() }
    forEach { list ->
        list.forEachIndexed { index, item ->
            resultList[index].add(item)
        }
    }
    return resultList
}

fun <T> List<T>.head() = first()
fun <T> List<T>.dropHead() = drop(1)

fun String.getAllInts() = "(-?\\d+)".toRegex().findAll(this).map { it.value.toInt() }.toList()

fun String.getAllByRegex(regex: Regex) = regex.findAll(this).map { it.value }.toList()

fun List<Long>.factorial() = reduce { acc, it -> acc * it }
fun List<Int>.factorial() = reduce { acc, it -> acc * it }

typealias Position = Pair<Int, Int>

operator fun Position.plus(other: Position) = Position(x + other.x, y + other.y)
operator fun Position.minus(other: Position) = Position(x - other.x, y - other.y)
val Position.x: Int
    get() = first
val Position.y: Int
    get() = second

fun Position.manhattanDistanceTo(other: Position) = abs(x - other.x) + abs(y - other.y)
fun <T> MutableList<T>.popHead(): T {
    val head = head()
    removeFirst()
    return head
}

fun IntRange.limitValuesInRange(valueRange: IntRange): IntRange {
    return first.coerceAtLeast(valueRange.first)..last.coerceAtMost(valueRange.last)
}

fun List<Int>.valueRange(): IntRange {
    return min()..max()
}
