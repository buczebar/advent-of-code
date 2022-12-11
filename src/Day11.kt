import java.lang.IllegalArgumentException

private fun parseMonkey(monkeyData: String): Monkey {
    return Monkey.build {
        monkeyData.lines().forEach { line ->
            with(line.trim()) {
                when {
                    startsWith("Monkey") -> {
                        id = getAllInts().first()
                    }

                    startsWith("Starting items:") -> {
                        items = getAllInts().map { it.toLong() }.toMutableList()
                    }

                    startsWith("Operation") -> {
                        operation = "\\w+ [*+-/]? \\w+".toRegex().find(this)?.value.orEmpty()
                    }

                    startsWith("Test:") -> {
                        testDivisor = getAllInts().first().toLong()
                    }

                    startsWith("If true:") -> {
                        idIfTrue = getAllInts().first()
                    }

                    startsWith("If false:") -> {
                        idIfFalse = getAllInts().first()
                    }
                }
            }
        }
    }
}

private fun parseInput(name: String) = readGroupedInput(name).map { parseMonkey(it) }
fun main() {
    fun simulate(monkeys: List<Monkey>, rounds: Int, reduceOperation: (Long) -> Long): List<Long> {
        val inspectionCounters = MutableList(monkeys.size) { 0L }
        repeat(rounds) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val newItemValue = reduceOperation(monkey.calculate(item))
                    monkeys[monkey.getMonkeyIdToPass(newItemValue)].items.add(newItemValue)
                    inspectionCounters[monkey.id]++
                }
                monkey.items.clear()
            }
        }
        return inspectionCounters
    }

    fun monkeyBusiness(inspections: List<Long>) =
        inspections
            .sortedDescending()
            .take(2)
            .factorial()

    fun part1(monkeys: List<Monkey>) = monkeyBusiness(simulate(monkeys, 20) { it / 3 })

    fun part2(monkeys: List<Monkey>) =
        monkeyBusiness(
            simulate(monkeys, 10_000) {
                it % monkeys.map { monkey -> monkey.testDivisor }.factorial()
            }
        )

    check(part1(parseInput("Day11_test")) == 10605L)
    check(part2(parseInput("Day11_test")) == 2713310158L)

    println(part1(parseInput("Day11")))
    println(part2(parseInput("Day11")))
}

private data class Monkey(
    val id: Int,
    val items: MutableList<Long> = mutableListOf(),
    private val operation: String,
    val testDivisor: Long,
    private val idIfTrue: Int,
    private val idIfFalse: Int
) {
    private constructor(builder: Builder) : this(
        builder.id,
        builder.items,
        builder.operation,
        builder.testDivisor,
        builder.idIfTrue,
        builder.idIfFalse
    )

    fun getMonkeyIdToPass(value: Long) =
        idIfTrue.takeIf { value % testDivisor == 0L } ?: idIfFalse

    fun calculate(value: Long): Long {
        val (_, type, second) = operation.takeIf { it.isNotEmpty() }?.splitWords() ?: return -1L
        val arg = if (second == "old") value else second.toLong()
        return when (type) {
            "+" -> value + arg
            "-" -> value - arg
            "*" -> value * arg
            "/" -> value / arg
            else -> throw IllegalArgumentException("wrong operation type $operation")
        }
    }

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var id = 0
        var items: MutableList<Long> = mutableListOf()
        var operation: String = ""
        var testDivisor: Long = 0L
        var idIfTrue: Int = 0
        var idIfFalse: Int = 0

        fun build() = Monkey(this)
    }
}

