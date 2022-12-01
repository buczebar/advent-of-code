fun main() {
    fun part1(input: List<List<Int>>): Int {
        return input.map { it.sum() }.maxOf { it }
    }

    fun part2(input: List<List<Int>>): Int {
        return input.map { it.sum() }.sortedDescending().take(3).sum()
    }

    val testInput = readGroupedInput("Day01_test").splitLinesToInts()
    check(part1(testInput) == 24_000)
    check(part2(testInput) == 45_000)

    val input = readGroupedInput("Day01").splitLinesToInts()
    println(part1(input))
    println(part2(input))
}
