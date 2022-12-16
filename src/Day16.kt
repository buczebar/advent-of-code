fun main() {
    lateinit var flowRates: Map<String, Int>
    lateinit var connections: Map<String, List<String>>
    lateinit var workingValves: List<String>
    lateinit var distances: Map<Pair<String, String>, Int>

    fun getDistanceToValve(
        current: String,
        destination: String,
        visited: List<String> = emptyList(),
        depth: Int = 1
    ): Int {
        val nextValves =
            (connections[current]!!).filter { !visited.contains(it) }.takeIf { it.isNotEmpty() }
                ?: return Int.MAX_VALUE
        return if (nextValves.contains(destination)) {
            depth
        } else {
            val newVisited = visited + current
            nextValves.minOf { getDistanceToValve(it, destination, newVisited, depth + 1) }
        }
    }

    fun parseInput(fileName: String) {
        val valves = mutableMapOf<String, Int>()
        val tunnels = mutableMapOf<String, List<String>>()

        readInput(fileName)
            .map { it.split(";") }
            .forEach { (valveRaw, tunnelsRaw) ->
                val label = valveRaw.getAllByRegex("[A-Z][A-Z]".toRegex()).single()
                val flowRate = valveRaw.getAllInts().single()
                val accessibleValves = tunnelsRaw.getAllByRegex("[A-Z][A-Z]".toRegex())

                valves[label] = flowRate
                tunnels[label] = accessibleValves
            }
        flowRates = valves
        connections = tunnels
        workingValves = flowRates.keys.filter { flowRates[it]!! > 0 }
    }

    fun calculateDistancesBetweenValves(valves: List<String>): Map<Pair<String, String>, Int> {
        val resultDistances = mutableMapOf<Pair<String, String>, Int>()
        for (start in valves) {
            for (end in valves) {
                resultDistances[start to end] = getDistanceToValve(start, end)
            }
        }
        return resultDistances
    }

    fun maxPath(node: String, countDown: Int, opened: Set<String> = emptySet(), pressure: Int = 0): Int {
        val nextOpened = opened + node
        val openedFlowRate = nextOpened.sumOf { flowRates[it]!! }
        return distances
            .filter { (path, distance) -> path.first == node && !opened.contains(path.second) && distance <= countDown - 1 }
            .map { (path, distance) -> Pair(path.second, distance) }
            .map { (nextNode, distance) ->
                val nextCountDown = countDown - distance - 1
                val nextPressure = pressure + (distance + 1) * openedFlowRate
                maxPath(nextNode, nextCountDown, nextOpened, nextPressure)
            }.plus(pressure + countDown * openedFlowRate)
            .max()
    }

    fun part1(): Int {
        val startingValve = "AA"
        distances = calculateDistancesBetweenValves(workingValves + startingValve)
        return maxPath(startingValve, 30)
    }

    parseInput("Day16_test")
    check(part1() == 1651)

    parseInput("Day16")
    println(part1())
}