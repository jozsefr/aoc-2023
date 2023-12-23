package aoc.year2023.day02

import java.io.File

const val RED_LIMIT = 12
const val GREEN_LIMIT = 13
const val BLUE_LIMIT = 14
fun main() {
    fun part1(lines: List<String>) = lines.sumOf { line ->
        val (gameId, turns) = line.split(":")
        turns.split(";").map { t ->
            t.split(",").map { cubes ->
                val (count, colour) = cubes.trim().split(" ")
                val validGame = when (colour) {
                    "red" -> count.toInt() <= RED_LIMIT
                    "green" -> count.toInt() <= GREEN_LIMIT
                    "blue" -> count.toInt() <= BLUE_LIMIT
                    else -> throw IllegalArgumentException("Unexpected cube colour: $colour")
                }
                if (!validGame) {
                    return@sumOf 0
                }
            }
        }
        gameId.split(" ").last().toInt()
    }

    fun part2(lines: List<String>) = lines.sumOf { line ->
        val turns = line.split(":").last()
        var minRed = 0
        var minGreen = 0
        var minBlue = 0
        turns.split(";").forEach { t ->
            t.trim().split(",").forEach { cubes ->
                val (count, colour) = cubes.trim().split(" ")
                when (colour) {
                    "red" -> minRed = maxOf(minRed, count.toInt())
                    "green" -> minGreen = maxOf(minGreen, count.toInt())
                    "blue" -> minBlue = maxOf(minBlue, count.toInt())
                }
            }
        }
        minRed * minGreen * minBlue
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day02/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day02/input.txt").readLines()

    println("Test Part One: ${part1(testLines).also { check(it == 8) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 2286) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 2512) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 67335) { "part 2 wrong answer: $it" } }}")
}
