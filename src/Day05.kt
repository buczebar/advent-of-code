typealias Stack = List<Char>

fun main() {

    fun solve(input: Pair<MutableList<Stack>, List<StackMove>>, inputModifier: (List<Char>) -> List<Char>): String {
        val (stacks, moves) = input
        moves.forEach { move ->
            stacks[move.to] = stacks[move.from].take(move.amount).let { inputModifier.invoke(it) } + stacks[move.to]
            stacks[move.from] = stacks[move.from].drop(move.amount)
        }
        return stacks.filter { it.isNotEmpty() }.map { it.first() }.joinToString("")
    }

    fun part1(input: Pair<MutableList<Stack>, List<StackMove>>) = solve(input) { it.reversed() }

    fun part2(input: Pair<MutableList<Stack>, List<StackMove>>) = solve(input) { it }

    check(part1(parseInput("Day05_test")) == "CMZ")
    check(part2(parseInput("Day05_test")) == "MCD")

    println(part1(parseInput("Day05")))
    println(part2(parseInput("Day05")))
}

private fun parseInput(name: String): Pair<MutableList<Stack>, List<StackMove>> {

    fun parseStacks(inputString: String): MutableList<Stack> {
        val stackValues = inputString.lines().dropLast(1)
            .map { line ->
                line.replace("    ", "[-]")
                    .remove("[^\\w-]".toRegex())
                    .toList()
            }
            .transpose()
            .map { stack -> stack.filter { it != '-' }.toMutableList() }

        return stackValues.toMutableList()
    }

    fun parseMoves(inputString: String): List<StackMove> {
        val lineRegex = "(\\d+)".toRegex()
        return inputString.lines()
            .map { line -> lineRegex.findAll(line).map { it.value.toInt() }.toList() }
            .map { (amount, from, to) -> StackMove(amount, from - 1, to - 1) }
    }

    val (stacks, moves) = readGroupedInput(name)
    return Pair(parseStacks(stacks), parseMoves(moves))
}

private data class StackMove(
    val amount: Int,
    val from: Int,
    val to: Int
)
