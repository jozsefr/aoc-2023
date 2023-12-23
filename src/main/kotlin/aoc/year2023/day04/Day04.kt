package aoc.year2023.day04

import java.io.File
import kotlin.math.pow

fun main() {
    fun parseNumbers(line: String) = line.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

    fun part1(lines: List<String>) = lines.map { line ->
        val (title, numbers) = line.split(":")
        val (winners, yours) = numbers.split("|")
        ScratchCard(
            cardNumber = title.split(" ").last().toInt(),
            winningNumbers = parseNumbers(winners),
            yourNumbers = parseNumbers(yours)
        )
    }.sumOf { it.score() }


    fun part2(lines: List<String>): Int {
        val cards = lines.map { line ->
            val (title, numbers) = line.split(":")
            val (winners, yours) = numbers.split("|")
            ScratchCard(
                cardNumber = title.split(" ").last().toInt(),
                winningNumbers = parseNumbers(winners),
                yourNumbers = parseNumbers(yours)
            )
        }.associateBy { it.cardNumber }.toMutableMap()

        cards.values.forEach { currentCard ->
            val numOfCardsWon = currentCard.cardsWon()
            if (numOfCardsWon > 0) {
                (currentCard.cardNumber + 1..currentCard.cardNumber + numOfCardsWon).filter { it in cards.keys }
                    .forEach { cardNumberWon ->
                        val currentStash = cards[cardNumberWon]!!
                        val additionalCopies = currentCard.instances
                        cards[cardNumberWon] = currentStash.makeCopies(additionalCopies)
                    }
            }
        }

        return cards.values.sumOf { it.instances }
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day04/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day04/input.txt").readLines()


    println("Test Part One: ${part1(testLines).also { check(it == 13.toDouble()) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 30) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 26443.toDouble()) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 6284877) { "part 2 wrong answer: $it" } }}")
}

data class ScratchCard(
    val cardNumber: Int,
    val winningNumbers: List<Int>,
    val yourNumbers: List<Int>,
    val instances: Int = 1
) {
    fun score(): Double {
        val howManyWon = cardsWon()
        return if (howManyWon > 0)
            2.toDouble().pow((howManyWon - 1).toDouble())
        else
            0.0
    }

    fun cardsWon(): Int = yourNumbers.count { it in winningNumbers }
    fun makeCopies(additionalCopies: Int) = copy(instances = instances + additionalCopies)

}
