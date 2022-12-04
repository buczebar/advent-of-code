typealias SectionRange = Pair<Int, Int>

fun main() {
    infix fun SectionRange.contains(range: SectionRange): Boolean {
        return first <= range.first && this.second >= range.second
    }

    infix fun SectionRange.overlap(range: SectionRange): Boolean {
        return first <= range.second && range.first <= second
    }

    fun part1(input: List<Pair<SectionRange, SectionRange>>) =
        input.count { it.first contains it.second || it.second contains it.first }


    fun part2(input: List<Pair<SectionRange, SectionRange>>) =
        input.count { it.first overlap it.second || it.second overlap it.first }

    fun parseInput(name: String): List<Pair<SectionRange, SectionRange>> {
        fun parseSectionRange(input: String) =
            input.split("-").map { it.toInt() }.pair()

        return readInput(name)
            .map {
                it.split(",").pair()
                    .let { ranges ->
                        Pair(parseSectionRange(ranges.first), parseSectionRange(ranges.second))
                    }
            }
    }

    val testInput = parseInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = parseInput("Day04")
    println(part1(input))
    println(part2(input))
}

