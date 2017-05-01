enum class Suit {
    DIAMONDS, HEARTS, CLUBS, SPADES
}

data class Card(val rank: Int, val suit: Suit) {
    val value: Int
        get() = when (rank) {
            13 -> 10
            12 -> 10
            11 -> 10
            0 -> 10
            else -> rank
        }

    val soft = when (rank) {
        1 -> true
        else -> false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Card
        return rank == other.rank
    }
}

class Shoe(numberOfDecks: Int) {
    val cards = arrayOfNulls<Card>(numberOfDecks * 52)
    var counter = 0
    val cutoff = cards.size - (Math.random() * (cards.size / 5))  // random cutoff near end of deck

    init {
        for (i in 0..cards.size-1) {
            val rank = i % 13 + 1
            val s = cards.size
            val suit = when (i) {
                in 0..s/4 - 1 -> Suit.DIAMONDS
                in s/4..s/2 - 1 -> Suit.HEARTS
                in s/2..s*3/4 - 1 -> Suit.CLUBS
                else -> Suit.SPADES
            }
            cards[i] = Card(rank, suit)
        }
        shuffle()
    }

    private fun shuffle() {
        val range = cards.size
        for (i in cards.indices) {
            val randomIndex = (Math.random() * range).toInt()
            val orig = cards[i]
            cards[i] = cards[randomIndex]
            cards[randomIndex] = orig
        }
    }

    fun drawCard(): Card? {
        counter += 1
        return if (counter < cutoff) {
            cards[counter - 1]
        } else null
    }

    val canDrawCard: Boolean
        get() {
            return counter < cutoff
        }

    fun printShoe() {
        println("$cards.size cards in shoe:")
        for (card in cards) {
            print("$card, ")
        }
        println()
    }
}