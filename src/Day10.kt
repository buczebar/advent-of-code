import kotlin.math.abs

fun main() {
    fun getValuesInEachCycle(input: List<String>): List<Int> {
        val valuesInCycles = mutableListOf(1)
        var valueAfterLastCycle = valuesInCycles.first()
        input.forEach { instruction ->
            when {
                instruction.startsWith("noop") -> {
                    valuesInCycles.add(valueAfterLastCycle)
                }

                instruction.startsWith("addx") -> {
                    val (_, value) = instruction.splitWords()
                    repeat(2) { valuesInCycles.add((valueAfterLastCycle)) }
                    valueAfterLastCycle += value.toInt()
                }
            }
        }
        return valuesInCycles
    }

    fun part1(input: List<String>): Int {
        val indexes = listOf(20, 60, 100, 140, 180, 220)
        val cycles = getValuesInEachCycle(input)
        return indexes.sumOf { if (it < cycles.size) cycles[it] * it else 0 }
    }

    fun part2(input: List<String>) {
        getValuesInEachCycle(input)
            .dropHead()
            .chunked(40)
            .map { row ->
                row.forEachIndexed { index, x ->
                    print(
                        when {
                            abs(x - index) <= 1 -> "#"
                            else -> "."
                        }
                    )
                }
                println()
            }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13_140)
    part2(testInput)

    val input = readInput("Day10")
    println(part1(input))
    part2(input)
}
