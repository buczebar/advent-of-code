import java.io.File
import java.lang.IllegalArgumentException

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

fun<T> List<T>.allDistinct() = distinct().size == size

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
fun<T> List<T>.head() = first()
fun<T> List<T>.dropHead() = drop(1)
