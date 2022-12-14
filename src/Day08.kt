fun main() {
    fun parseInput(name: String) =
        readInput(name).map { it.toList() }.map { row -> row.map { height -> height.digitToInt() } }

    fun getListOfTrees(input: List<List<Int>>): List<Tree> {
        val columns = input.transpose()
        val (width, height) = columns.size to input.size
        val resultList = mutableListOf<Tree>()

        input.forEachIndexed { rowIndex, row ->
            columns.forEachIndexed { colIndex, column ->
                resultList.add(Tree(
                    height = input[rowIndex][colIndex],
                    allOnLeft = row.subList(0, colIndex).reversed(),
                    allOnRight = colIndex.takeIf { it < width - 1 }?.let { row.subList(it + 1, width) } ?: emptyList(),
                    allOnTop = column.subList(0, rowIndex).reversed(),
                    allOnBottom = rowIndex.takeIf { it < height - 1 }?.let { column.subList(it + 1, height) }
                        ?: emptyList()
                ))
            }
        }
        return resultList
    }

    fun part1(input: List<Tree>) = input.count { it.isVisible }

    fun part2(input: List<Tree>) = input.maxOfOrNull { it.scenicScore }

    val testInput = getListOfTrees(parseInput("Day08_test"))
    check(part1((testInput)) == 21)
    check(part2(testInput) == 8)

    val input = getListOfTrees(parseInput("Day08"))
    println(part1(input))
    println(part2(input))

}

private data class Tree(
    val height: Int,
    val allOnLeft: List<Int>,
    val allOnRight: List<Int>,
    val allOnTop: List<Int>,
    val allOnBottom: List<Int>
) {
    private val maxInAllDirections: List<Int>
        get() = allDirections.map { it.takeIf { it.isNotEmpty() }?.max() ?: 0 }

    private val allDirections: List<List<Int>>
        get() = listOf(allOnLeft, allOnRight, allOnTop, allOnBottom)

    private val visibleTreesInAllDirection: List<Int>
        get() = allDirections.map { it.numberOfVisibleTrees() }

    val isVisible: Boolean
        get() = allDirections.any { it.isEmpty() }.or(maxInAllDirections.min() < height)
    val scenicScore: Int
        get() = visibleTreesInAllDirection.reduce { acc, numOfTrees -> acc * numOfTrees }

    private fun List<Int>.numberOfVisibleTrees(): Int =
        indexOfFirst { it >= height }.takeIf { it >= 0 }?.plus(1) ?: size
}
