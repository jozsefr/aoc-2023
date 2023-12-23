package aoc.year2023.day01

import java.io.File

fun main() {
    val textToDigits = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun part1(lines: List<String>): Int = lines.sumOf { line ->
        "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
    }

    fun part2(lines: List<String>) = lines.sumOf { line ->
        val firstTextDigit = textToDigits.keys
            .map { it to line.indexOf(it) }
            .filter { it.second > -1 }
            .minByOrNull { it.second }
            ?.first
        val lastTextDigit = textToDigits.keys
            .map { it to line.reversed().indexOf(it.reversed()) }
            .filter { it.second > -1 }
            .minByOrNull { it.second }
            ?.first
        val firstDigit = firstTextDigit?.let {
            line.replaceFirst(it, textToDigits[it]!!)
        } ?: line
        val secondDigit = lastTextDigit?.let {
            line.reversed().replaceFirst(it.reversed(), textToDigits[it]!!).reversed()
        } ?: line
        "${firstDigit.first { it.isDigit() }}${secondDigit.last { it.isDigit() }}".toInt()
    }

    val testLinesPart1 = File("src/main/kotlin/aoc/year2023/day01/part1-test-input.txt").readLines()
    val testLinesPart2 = File("src/main/kotlin/aoc/year2023/day01/part2-test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day01/input.txt").readLines()

    println("Test Part One: ${part1(testLinesPart1).also { check(it == 142) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLinesPart2).also { check(it == 281) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 55971) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 54719) { "part 2 wrong answer :$it" } }}")
}
