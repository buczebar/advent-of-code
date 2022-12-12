private typealias MapOfHeights = List<List<Char>>

private fun Char.elevation() =
    when (this) {
        'S' -> 0 // a
        'E' -> 25 // z
        else ->
            ('a'..'z').indexOf(this)
    }


private fun MapOfHeights.getFirstPositionOf(mark: Char): Position {
    forEachIndexed { index, row ->
        if (row.contains(mark)) {
            return row.indexOf(mark) to index
        }
    }
    return -1 to -1
}

private fun MapOfHeights.getAllPositionsWithElevation(elevation: Int): List<Position> {
    val positions = mutableListOf<Position>()
    forEachIndexed { colIndex, items ->
        items.forEachIndexed { rowIndex, item ->
            if (item.elevation() == elevation) {
                positions.add(rowIndex to colIndex)
            }
        }
    }
    return positions
}

private fun MapOfHeights.getPossibleMoves(position: Position): List<Position> {
    val currentHeight = this[position.y][position.x].elevation()
    val directions = listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
    return directions
        .map { direction -> position + direction }
        .filter { it.x in first().indices && it.y in indices }
        .filter { this[it.y][it.x].elevation() - currentHeight <= 1 }
}

private fun MapOfHeights.findShortestPathLength(
    start: Position,
    destination: Position
): Int {
    val queue = mutableListOf(start)
    val visited = mutableSetOf(start)
    val predecessors = mutableMapOf<Position, Position>()

    while (queue.isNotEmpty()) {
        val currentItem = queue.popHead()
        val availableMoves = getPossibleMoves(currentItem)
        availableMoves.forEach { next ->
            if (!visited.contains(next)) {
                visited.add(next)
                predecessors[next] = currentItem
                queue.add(next)

                if (next == destination) {
                    queue.clear()
                    return@forEach
                }
            }
        }
    }
    var lenth = 0
    var crawl: Position? = destination
    while (predecessors[crawl] != null) {
        crawl = predecessors[crawl]
        lenth++
    }

    return lenth
}

fun main() {
    fun part1(mapOfHeights: MapOfHeights): Int {
        val (start, destination) = mapOfHeights.getFirstPositionOf('S') to mapOfHeights.getFirstPositionOf('E')
        return mapOfHeights.findShortestPathLength(start, destination)
    }

    fun part2(mapOfHeights: MapOfHeights): Int {
        val allStartingPoints = mapOfHeights.getAllPositionsWithElevation(0)
        val destination = mapOfHeights.getFirstPositionOf('E')
        val allPaths = allStartingPoints.map { startingPoint ->
            mapOfHeights.findShortestPathLength(
                startingPoint, destination
            )
        }
        return allPaths.filter { it != 0 }.min()
    }

    val testInput = readInput("Day12_test").map { it.toList() }
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12").map { it.toList() }
    println(part1(input))
    println(part2(input))
}
