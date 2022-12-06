fun main() {
    fun solve(input: String, distLength: Int) =
        input.indexOf(input.windowed(distLength).first { it.toList().allDistinct()}) + distLength

    fun part1(input: String) = solve(input, 4)
    fun part2(input: String) = solve(input, 14)

    val testInput = readInput("Day06_test")
    val testResults = listOf(
        7 to 19, // mjqjpqmgbljsphdztnvjfqwrcgsmlb
        5 to 23, // bvwbjplbgvbhsrlpgdmjqwftvncz
        6 to 23, // nppdvjthqldpwncqszvftbrmjlhg
        10 to 29, // nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg
        11 to 26 // zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw
    )
    testInput.zip(testResults).forEach { (input, results) ->
        check(part1(input) == results.first)
        check(part2(input) == results.second)
    }

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
