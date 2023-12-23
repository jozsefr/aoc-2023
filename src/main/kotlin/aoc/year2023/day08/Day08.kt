package aoc.year2023.day08

import java.io.File

data class Node(val label: String, val left: String, val right: String)

fun main() {

    fun parse(lines: List<String>): Pair<String, MutableMap<String, Node>> {
        val instructions = lines.first()
        val map = lines.drop(2)
            .map { line ->
                val (node, elements) = line.split("=")
                node.trim() to elements.filter { it !in "() " }
            }.fold(mutableMapOf<String, Node>()) { acc, labelToLeftRight ->
                val label = labelToLeftRight.first
                val (left, right) = labelToLeftRight.second.split(",")
                acc[label] = Node(label, left, right)
                acc
            }
        return instructions to map
    }

    fun part1(lines: List<String>): Int {
        val (instructions, map) = parse(lines)

        var currentNode = "AAA"
        var steps = 0
        navigate@ while (true) {
            for (i in instructions) {
                when (i) {
                    'L' -> currentNode = map[currentNode]?.left
                        ?: throw IllegalArgumentException("$currentNode is not on the map")

                    'R' -> currentNode = map[currentNode]?.right
                        ?: throw IllegalArgumentException("$currentNode is not on the map")
                }
                steps++
                if (currentNode == "ZZZ") {
                    break@navigate
                }
            }
        }

        return steps
    }

    fun part2(lines: List<String>): Long {
        val (instructions, map) = parse(lines)

        val stepsPerNode = map.keys
            .filter { it.last() == 'A' }
            .map { node ->
                var currentNode = node
                var steps = 0
                navigate@ while (true) {
                    for (i in instructions) {
                        when (i) {
                            'L' -> currentNode = map[currentNode]?.left
                                ?: throw IllegalArgumentException("$currentNode is not on the map")

                            'R' -> currentNode = map[currentNode]?.right
                                ?: throw IllegalArgumentException("$currentNode is not on the map")
                        }
                        steps++
                        if (currentNode.endsWith("Z")) {
                            break@navigate
                        }
                    }
                }
                steps.toLong()
            }

        fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
        fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
        fun lcmOf(numbers: List<Long>): Long = numbers.reduce { a, b -> lcm(a, b) }

        return lcmOf(stepsPerNode)
    }

    val testLinesPart1A = File("src/main/kotlin/aoc/year2023/day08/part1-test-input-a.txt").readLines()
    val testLinesPart1B = File("src/main/kotlin/aoc/year2023/day08/part1-test-input-b.txt").readLines()
    val testLinesPart2 = File("src/main/kotlin/aoc/year2023/day08/part2-test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day08/input.txt").readLines()

    println("Test Part One A: ${part1(testLinesPart1A).also { check(it == 2) { "part 1 test A wrong answer: $it" } }}")
    println("Test Part One B: ${part1(testLinesPart1B).also { check(it == 6) { "part 1 test B wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLinesPart2).also { check(it == 6L) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 18023) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 14449445933179) { "part 2 wrong answer :$it" } }}")

}
