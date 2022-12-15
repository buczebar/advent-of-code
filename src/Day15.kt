import kotlin.math.abs

private fun parseInput(name: String) =
    readInput(name).map { line ->
        line.split(":").map { it.getAllInts().pair() }
    }.map { (sensorXY, beaconXY) -> Sensor(sensorXY, beaconXY) }

fun main() {
    fun Position.tuningFrequency() = 4000000L * x + y

    fun coverageRangesForY(y: Int, sensors: List<Sensor>, valueRange: IntRange): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        sensors.forEach { sensor ->
            val distanceToY = abs(sensor.y - y)
            val radiusForY = (sensor.distanceToClosestBeacon - distanceToY).coerceAtLeast(0)
            if (radiusForY > 0) {
                ranges.add(((sensor.x - radiusForY)..(sensor.x + radiusForY)).limitValuesInRange(valueRange))
            }
        }
        return ranges
    }

    fun part1(sensors: List<Sensor>, y: Int): Int {
        val xPositionsWithSensorsOrBeacons = sensors
            .flatMap { listOf(it.position, it.closestBeacon) }
            .filter { it.y == y }
            .map { it.x }
            .toSet()

        val valueRange = sensors.map { it.closestBeacon.x }.valueRange()
        val xPositionsWithoutBeacon = coverageRangesForY(y, sensors, valueRange)
            .flatMap { it.toList() }
            .toSet()
            .minus(xPositionsWithSensorsOrBeacons)

        return xPositionsWithoutBeacon.size
    }

    fun part2(sensors: List<Sensor>, valueRange: IntRange): Long {
        valueRange.forEach { y ->
            val coverageRanges = coverageRangesForY(y, sensors, valueRange).sortedBy { it.first }
            var rightBoundary = coverageRanges.first().last
            for (i in 1 until coverageRanges.size) {
                val range = coverageRanges[i]
                if (rightBoundary < range.first) {
                    return Position(rightBoundary + 1, y).tuningFrequency()
                }
                rightBoundary = range.last.coerceAtLeast(rightBoundary)
            }
        }
        return 0L
    }

    val testInput = parseInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 0..20) == 56000011L)

    val input = parseInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 0..4000000))

}

private data class Sensor(val position: Position, val closestBeacon: Position) {
    val x: Int
        get() = position.x
    val y: Int
        get() = position.y

    val distanceToClosestBeacon: Int
        get() = position.manhattanDistanceTo(closestBeacon)
}
