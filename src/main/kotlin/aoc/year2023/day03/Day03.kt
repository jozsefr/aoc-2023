package aoc.year2023.day03

import java.io.File

data class Symbol(val value: Char, val location: Location)
data class Location(val row: Int, val column: Int)
data class Part(val number: Int, val location: Location) {
    fun isAdjacentTo(s: Symbol) =
        s.location.row in (location.row - 1)..(location.row + 1) &&
                s.location.column in (location.column - 1)..(location.column + number.toString().length)
}

fun main() {
    fun Char.isSymbol() = !this.isDigit() && this != '.'

    fun part1(lines: List<String>): Int {
        val parts = mutableListOf<Part>()
        val symbols = mutableListOf<Symbol>()
        lines.forEachIndexed { row, line ->
            var column = 0
            while (column < line.length) {
                val ch = line[column]
                when {
                    ch.isDigit() -> {
                        val partNo = line.substring(column).takeWhile { it.isDigit() }
                        parts.add(Part(partNo.toInt(), Location(row, column)))
                        column += partNo.length - 1
                    }

                    ch.isSymbol() -> symbols.add(Symbol(ch, Location(row, column)))
                }
                column++
            }
        }
        return parts.filter { symbols.any { s -> it.isAdjacentTo(s) } }.toSet().sumOf { it.number }
    }

    fun part2(lines: List<String>): Int {
        val parts = mutableListOf<Part>()
        val symbols = mutableListOf<Symbol>()
        lines.forEachIndexed { row, line ->
            var column = 0
            while (column < line.length) {
                val ch = line[column]
                when {
                    ch.isDigit() -> {
                        val partNo = line.substring(column).takeWhile { it.isDigit() }
                        parts.add(Part(partNo.toInt(), Location(row, column)))
                        column += partNo.length - 1
                    }

                    ch.isSymbol() -> symbols.add(Symbol(ch, Location(row, column)))
                }
                column++
            }
        }
        return symbols.filter { it.value == '*' }.sumOf { gear ->
            val adjacentParts = parts.filter { it.isAdjacentTo(gear) }
            val gearRatios = if (adjacentParts.size == 2) {
                adjacentParts.map { it.number }.reduce(Int::times)
            } else {
                0
            }
            gearRatios
        }
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day03/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day03/input.txt").readLines()


    println("Test Part One: ${part1(testLines).also { check(it == 4361) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 467835) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 532331) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 82301120) { "part 2 wrong answer: $it" } }}")
}
