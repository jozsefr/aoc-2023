package aoc.year2023.day07

import aoc.year2023.day07.Hand.*
import java.io.File

const val CARD_STRENGTHS = "AKQJT98765432"
const val CARD_STRENGTHS_WITH_JOKER = "AKQT98765432J"

enum class Hand {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

fun main() {

    fun getRank(cards: String, strength: String) = cards
        .map { strength.indexOf(it) }
        .fold("") { acc, i ->
            acc + i.toString().padStart(2, '0')
        }

    fun getHand(cards: String): Hand {
        if (cards.length != 5) {
            throw IllegalArgumentException("need 5 cards and you have: $cards")
        }
        val cardToCount = cards.toSet().map { c -> c to cards.count { it == c } }
        return when {
            cardToCount.any { it.second == 5 } -> FIVE_OF_A_KIND
            cardToCount.any { it.second == 4 } -> FOUR_OF_A_KIND
            cardToCount.any { it.second == 3 } && cardToCount.any { it.second == 2 } -> FULL_HOUSE
            cardToCount.size == 3 && cardToCount.any { it.second == 3 } -> THREE_OF_A_KIND
            cardToCount.count { it.second == 2 } == 2 -> TWO_PAIR
            cardToCount.size == 4 && cardToCount.any { it.second == 2 } -> ONE_PAIR
            cardToCount.all { it.second == 1 } -> HIGH_CARD
            else -> throw IllegalArgumentException("Cannot recognise hand: $cards")
        }
    }

    fun getHandWithJokers(cards: String): Hand {
        if (cards.none { it == 'J' } || cards.all { it == 'J' }) {
            return getHand(cards)
        }
        return cards
            .filter { it != 'J' }
            .map { card -> getHand(cards.replace('J', card)) }
            .minBy { it.ordinal }
    }

    fun parseCardsAndBids(lines: List<String>) = lines.map { line ->
        val (cards, bid) = line.split(" ")
        cards to bid.toInt()
    }

    fun part1(lines: List<String>): Int {
        val cardsToBid = parseCardsAndBids(lines)

        val orderedByRankAsc = cardsToBid
            .sortedWith(
                compareBy<Pair<String, Int>> { (cards, _) -> getHand(cards).ordinal }
                    .thenBy { (cards, _) -> getRank(cards, CARD_STRENGTHS) }
                    .reversed()
            )
        return orderedByRankAsc
            .mapIndexed { i, hand -> (i + 1) * hand.second }
            .sum()
    }

    fun part2(lines: List<String>): Int {
        val cardsToBid = parseCardsAndBids(lines)

        val orderedByRankAsc = cardsToBid
            .sortedWith(
                compareBy<Pair<String, Int>> { (cards, _) -> getHandWithJokers(cards).ordinal }
                    .thenBy { (cards, _) -> getRank(cards, CARD_STRENGTHS_WITH_JOKER) }
                    .reversed()
            )
        return orderedByRankAsc
            .mapIndexed { i, hand -> (i + 1) * hand.second }
            .sum()
    }

    val testLines = File("src/main/kotlin/aoc/year2023/day07/test-input.txt").readLines()
    val lines = File("src/main/kotlin/aoc/year2023/day07/input.txt").readLines()

    println("Test Part One: ${part1(testLines).also { check(it == 6440) { "part 1 test wrong answer: $it" } }}")
    println("Test Part Two: ${part2(testLines).also { check(it == 5905) { "part 2 test wrong answer: $it" } }}")

    println("Part One: ${part1(lines).also { check(it == 249638405) { "part 1 wrong answer: $it" } }}")
    println("Part Two: ${part2(lines).also { check(it == 249776650) { "part 2 wrong answer: $it" } }}")
}
