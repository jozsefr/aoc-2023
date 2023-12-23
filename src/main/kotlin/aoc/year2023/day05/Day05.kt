package aoc.year2023.day05

import aoc.year2023.day05.Category.*
import java.io.File
import java.rmi.UnexpectedException

enum class Category {
    SEED,
    SOIL,
    FERTILIZER,
    WATER,
    LIGHT,
    TEMPERATURE,
    HUMIDITY,
    LOCATION
}

data class Conversion(val from: Category, val to: Category, val rangeMaps: List<RangeMap>)
data class RangeMap(val source: LongRange, val destination: LongRange)


fun parseSeeds(line: String): List<Long> = line
    .substringAfter(":")
    .trim()
    .split(" ")
    .map { it.toLong() }

fun parseSeedRanges(line: String): List<LongRange> = line
    .substringAfter(":")
    .trim()
    .split(" ")
    .map { it.toLong() }
    .chunked(2)
    .map { pair -> pair[0]..<pair[0] + pair[1] }

fun parseMaps(lines: List<String>): Map<Category, Conversion> =
    "\n\n".toPattern()
        .split(lines.joinToString("\n"))
        .map { section ->
            val (fromCategory, toCategory) = section
                .substringBefore(" ")
                .split("-")
                .filter { it != "to" }
                .map { valueOf(it.uppercase()) }

            val rangeMaps = section
                .split("\n")
                .filter { it[0].isDigit() }
                .map { mappingLine ->
                    val (destinationStart, sourceStart, rangeLength) = mappingLine
                        .split(" ")
                        .map { it.toLong() }
                    RangeMap(
                        source = sourceStart..<sourceStart + rangeLength,
                        destination = destinationStart..<destinationStart + rangeLength
                    )
                }

            Conversion(
                from = fromCategory,
                to = toCategory,
                rangeMaps = rangeMaps
            )
        }.associateBy { it.from }

fun main() {

    fun part1(lines: List<String>): Long {
        val seeds = parseSeeds(lines.first())
        val conversionMap = parseMaps(lines.drop(2))

        fun getLocationFor(from: Category, id: Long): Long {
            val mapping = conversionMap[from] ?: return id
            val afterConversion = mapping.rangeMaps
                .filter { id in it.source }
                .map { it.destination.first + (id - it.source.first) }
                .firstOrNull() ?: id
            return getLocationFor(mapping.to, afterConversion)
        }

        return seeds.minOf { seed -> getLocationFor(SEED, seed) }
    }

    fun part2(lines: List<String>): Long {
        val seeds = parseSeedRanges(lines.first())
        val conversionMap = parseMaps(lines.drop(2))

        fun findSeed(fromCat: Category, id: Long): Long {
            val mapping = conversionMap.values.find { it.to == fromCat } ?: return id
            val correspondingNum = mapping.rangeMaps
                .filter { it.destination.contains(id) }
                .map { it.source.first + (id - it.destination.first) }
                .firstOrNull() ?: id
            val nextMap = conversionMap.values.find { it.to == mapping.from } ?: return correspondingNum
            return findSeed(nextMap.to, correspondingNum)
        }

        for (loc in 1..Long.MAX_VALUE) {
            val seed = findSeed(LOCATION, loc)
            if (seeds.any { seedRange -> seed in seedRange }) {
                return loc
            }
        }
        throw UnexpectedException("No solution")
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day05/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day05/input.txt").readLines()

    println("Test Part One: ${part1(testLines).also { check(it == 35L) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 46L) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 265018614L) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 63179500L) { "part 2 wrong answer: $it" } }}")
}
