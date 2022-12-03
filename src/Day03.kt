fun main() {

    fun getPriority(item: Char) =
        (('a'..'z') + ('A'..'Z')).indexOf(item) + 1

    fun part1(input: List<String>) =
        input.map { sack -> sack.chunked(sack.length / 2).map { comp -> comp.toList() } }
            .map { (comp1, comp2) -> comp1.intersect(comp2.toSet()) }
            .flatten()
            .sumOf { getPriority(it) }

    fun part2(input: List<String>) =
        input.chunked(3)
            .map { (a, b, c) -> a.toSet().intersect(b.toSet().intersect(c.toSet())) }
            .flatten()
            .sumOf { getPriority(it) }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

