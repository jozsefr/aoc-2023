package aoc.year2023.day06

import java.io.File

fun main() {

    fun numberOfWaysToWin(tnd: Pair<Long, Long>): Int {
        val (totalTime, distanceToBeat) = tnd
        return (0L..totalTime).map { buttonHeld ->
            val distanceTravelled = when (buttonHeld) {
                0L,
                totalTime -> 0L

                else -> (totalTime - buttonHeld) * buttonHeld
            }
            distanceTravelled
        }.count { distanceTravelled -> distanceTravelled > distanceToBeat }
    }

    fun part1(lines: List<String>): Int {
        val times = lines.first().removePrefix("Time:").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val distances = lines.last().removePrefix("Distance:").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val tAndD = times.zip(distances)
        return tAndD.fold(1) { acc, it -> acc * numberOfWaysToWin(it) }
    }

    fun part2(lines: List<String>): Int {
        val totalTime = lines.first().removePrefix("Time:").split(" ").filter { it.isNotBlank() }
            .fold("") { acc, digits -> acc + digits }.toLong()
        val distanceToBeat = lines.last().removePrefix("Distance:").split(" ").filter { it.isNotBlank() }
            .fold("") { acc, digits -> acc + digits }.toLong()
        return numberOfWaysToWin(totalTime to distanceToBeat)
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day06/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day06/input.txt").readLines()

    println("Test Part One: ${part1(testLines).also { check(it == 288) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 71503) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 500346) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 42515755) { "part 2 wrong answer: $it" } }}")
}
