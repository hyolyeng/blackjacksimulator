enum class DealerRule {
    HIT_ON_SOFT_17, STAND_ON_SOFT_17
}

class Dealer(val dealerRule: DealerRule): Player() {
    override fun nextMove(hand: Hand, upcard: Card): Move {
        if (hand.handValue > 17) {
            return Move.STAND
        } else if (hand.handValue < 17) {
            return Move.HIT
        } else { // 17
            return if (dealerRule == DealerRule.HIT_ON_SOFT_17) Move.HIT else Move.STAND
        }
    }
}