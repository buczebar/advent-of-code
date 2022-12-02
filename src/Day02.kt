import java.lang.IllegalArgumentException

fun main() {
    fun gameResult(yourMove: HandShape, opponentMove: HandShape) =
        when (yourMove) {
            opponentMove.greater() -> {
                GameResult.WIN
            }

            opponentMove.lower() -> {
                GameResult.LOSS
            }

            else -> {
                GameResult.DRAW
            }
        }

    fun part1(input: List<Pair<HandShape, HandShape>>): Int {
        return input.sumOf { (opponentMove, yourMove) ->
            yourMove.score + gameResult(yourMove, opponentMove).score
        }
    }

    fun part2(input: List<Pair<HandShape, GameResult>>) =
        input.sumOf {
            val (opponentMove, gameResult) = it
            gameResult.score + when (gameResult) {
                GameResult.LOSS -> opponentMove.lower().score
                GameResult.DRAW -> opponentMove.score
                GameResult.WIN -> opponentMove.greater().score
            }
        }

    val testInputPart1 = parseInput("Day02_test", String::toHandShape, String::toHandShape)
    check(part1(testInputPart1) == 15)

    val testInputPart2 = parseInput("Day02_test", String::toHandShape, String::toGameResult)
    check(part2(testInputPart2) == 12)

    val inputForPart1 = parseInput("Day02", String::toHandShape, String::toHandShape)
    println(part1(inputForPart1))

    val inputForPart2 = parseInput("Day02", String::toHandShape, String::toGameResult)
    println(part2(inputForPart2))
}

private fun <T, U> parseInput(name: String, firstArgumentParser: (String) -> T, secondArgParser: (String) -> U) =
    readInput(name).map { round ->
        round.split(" ").let {
            Pair(firstArgumentParser.invoke(it[0]), secondArgParser.invoke(it[1]))
        }
    }

private fun String.toHandShape(): HandShape {
    return when (this) {
        "A", "X" -> HandShape.ROCK
        "B", "Y" -> HandShape.PAPER
        "C", "Z" -> HandShape.SCISSORS
        else -> throw IllegalArgumentException()
    }
}

private fun String.toGameResult(): GameResult {
    return when (this) {
        "X" -> GameResult.LOSS
        "Y" -> GameResult.DRAW
        "Z" -> GameResult.WIN
        else -> throw IllegalArgumentException()
    }
}

private enum class HandShape(val score: Int) {
    ROCK(1) {
        override fun greater() = PAPER
        override fun lower() = SCISSORS
    },
    PAPER(2) {
        override fun greater() = SCISSORS
        override fun lower() = ROCK
    },
    SCISSORS(3) {
        override fun greater() = ROCK
        override fun lower() = PAPER
    };

    abstract fun greater(): HandShape
    abstract fun lower(): HandShape
}

private enum class GameResult(val score: Int) {
    LOSS(0),
    DRAW(3),
    WIN(6)
}