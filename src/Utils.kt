import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Reads input separated by empty line
 */
fun readGroupedInput(name: String) = File("src", "$name.txt").readText().split("\n\n")

fun List<String>.splitLinesToInts() = this.map { it.split("\n").toListOfInts() }

fun List<String>.toListOfInts() = this.map { it.toInt() }
/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
