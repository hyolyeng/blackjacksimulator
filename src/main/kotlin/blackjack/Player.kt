package blackjack

class Hand {
    var cards: MutableList<Card> = mutableListOf()
    val handValue: Int
        get() {
            var value = 0
            for (card in cards) {
                value += card.value
            }
            return value
        }

    val allValues: IntArray
        get() {
            if (!this.soft) return intArrayOf(handValue)
            val numAces = cards.filter { it.rank == 1 }.size
            val valueNoAces = cards.filter { it.rank != 1 }.sumBy { it.value }
            return intArrayOf(valueNoAces + 11 + (numAces - 1), valueNoAces + numAces)
        }

    fun addCard(card: Card): Hand {
        cards.add(card)
        return this
    }

    fun clear() {
        cards.clear()
    }

    val canSplit: Boolean
        get() {
            if (cards.size != 2) return false
            return cards[0].equals(cards[1])
        }

    val soft: Boolean
        get() {
            return cards.filter { it.soft }.size > 0
        }

    fun print(dealerHandValue: Int) {
        cards.forEach{print("$it ")}
        var actualHandValue = handValue
        if (soft) {
            actualHandValue = allValues.filter { it <= 21 }.max() ?: handValue
        }
        val result = if (actualHandValue > 21) "L" else (if (dealerHandValue > 21) "W" else (if (dealerHandValue == actualHandValue) "D" else (if (dealerHandValue > actualHandValue) "L" else "W")))
        println(": totalValue: $actualHandValue, Result: $result")
    }

    val toString: String
        get() {
            return "$cards"
        }
}

abstract class Player {
    abstract fun nextMove(hand: Hand, upcard: Card): Move

    var hands: MutableList<Hand> = mutableListOf()
    var currentHandIndex = 0

    fun reset() {
        hands = mutableListOf()
        currentHandIndex = 0
    }

    fun cardDealt(card: Card) {
        if (hands.size == 0) {
            var hand = Hand()
            hand.addCard(card)
            hands.add(hand)
            currentHandIndex = 0
        } else {
            hands[currentHandIndex].addCard(card)
        }
    }

    fun split() {
        assert(hands[currentHandIndex].canSplit)
        val hand = hands[currentHandIndex]
        hands.removeAt(currentHandIndex)
        hands.addAll(currentHandIndex, listOf(Hand().addCard(hand.cards[0]), Hand().addCard(hand.cards[1])))
    }

    fun popNextMove(upcard: Card): Move {
        assert(currentHandIndex < hands.size)
        val nextMove = nextMove(hands[currentHandIndex], upcard)
        if (nextMove == Move.STAND) {
            currentHandIndex += 1
        }
        return nextMove
    }

    fun surrenderCurrentHand() {
        currentHandIndex += 1
    }

    val done: Boolean
        get() {
            return currentHandIndex >= hands.size
        }

    fun print(dealerHandValue: Int) {
        println("======= New Player: $this ========")
        hands.forEach{
            print("Hand: ")
            it.print(dealerHandValue)
        }
    }
}

class BasicStrategyPlayer: Player() {

    // shit can have multiple hands errrrr.
    override fun nextMove(hand: Hand, upcard: Card): Move {
        // First, see if hand already bust, and return STAND
        if (hand.handValue >= 21) {
            return Move.STAND
        }

        // check for pair splitting
        if (hand.canSplit) {
            val card = hand.cards[0]
            if (card.rank == 1 || card.rank == 8) { // always split As and 8s
                return Move.SPLIT
            }

            if (upcard.value in 2..6 && card.rank in intArrayOf(2, 3, 6, 7, 8, 9)) {
                return Move.SPLIT
            }

            if (upcard.value == 7 && card.rank in intArrayOf(2, 3, 7, 8)) {
                return Move.SPLIT
            }

            if (upcard.value in intArrayOf(5, 6) && card.rank == 4) {
                return Move.SPLIT
            }

            if (upcard.value in intArrayOf(8, 9) && card.rank == 9) {
                return Move.SPLIT
            }
        }

        if (hand.soft) {
            // soft totals decisions
            if (hand.handValue == 10 || hand.handValue == 11) {
                return Move.STAND
            }

            if (hand.handValue == 9 && upcard.rank != 6) {
                return Move.STAND
            } else if (hand.handValue == 9 && upcard.rank == 6) {
                return Move.DOUBLE
            }

            if (upcard.rank in intArrayOf(5, 6)) {
                return Move.DOUBLE
            }

            if (upcard.rank == 4 && hand.handValue in intArrayOf(5, 6, 7, 8)) {
                return Move.DOUBLE
            }

            if (upcard.rank == 3 && hand.handValue in intArrayOf(7, 8)) {
                return Move.DOUBLE
            }

            if (upcard.rank == 2 && hand.handValue == 8) {
                return Move.DOUBLE
            }

            if (upcard.rank in intArrayOf(7, 8) && hand.handValue == 8) {
                return Move.STAND
            }

            return Move.HIT
        }

        // not soft..
        if (hand.handValue >= 17) {
            return Move.STAND
        }

        if (hand.handValue in 13..16 && upcard.rank in 2..6) {
            return Move.STAND
        }

        if (hand.handValue == 12 && upcard.rank in 4..6) {
            return Move.STAND
        }

        if (hand.handValue == 11) {
            return Move.DOUBLE
        }

        if (hand.handValue == 10 && upcard.rank in 2..9) {
            return Move.DOUBLE
        }

        if (hand.handValue == 9 && upcard.rank in 3..6) {
            return Move.DOUBLE
        }

        return Move.HIT
    }
}