package aoc.year2023.day09

import java.io.File

fun main() {

    fun calcDiffs(sensorValues: List<Int>) = sensorValues.windowed(2).map { (a, b) -> b - a }

    fun part1(lines: List<String>): Int {
        return lines.map { line ->
            val sensorValues = line.split(" ").map { it.toInt() }

            val diffSequences = mutableListOf(sensorValues)
            var diff = calcDiffs(sensorValues)
            while (diff.any { it != 0 }) {
                diffSequences.add(diff)
                diff = calcDiffs(diff)
            }
            diffSequences.reversed()
                .fold(0) { carry, seq -> seq.last() + carry }
        }.sum()
    }

    fun part2(lines: List<String>): Int {
        return lines.map { line ->
            val sensorValues = line.split(" ").map { it.toInt() }

            val diffSequences = mutableListOf(sensorValues)
            var diff = calcDiffs(sensorValues)
            while (diff.any { it != 0 }) {
                diffSequences.add(diff)
                diff = calcDiffs(diff)
            }
            diffSequences.reversed()
                .fold(0) { carry, seq -> seq.first() - carry }
        }.sum()
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day09/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day09/input.txt").readLines()

    println("Test Part One: ${part1(testLines).also { check(it == 114) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 2) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 2105961943) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 1019) { "part 2 wrong answer: $it" } }}")
}
