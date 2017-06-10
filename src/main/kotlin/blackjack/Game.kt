package blackjack

enum class Move {
    HIT, STAND, DOUBLE, SPLIT, SURRENDER
}

enum class GameState {
    ONGOING, DEALER_BLACKJACK, NO_LIVE_PLAYERS
}

class Game {
    val shoe = Shoe(6)
    val dealer = Dealer(DealerRule.HIT_ON_SOFT_17)
    val players: List<BasicStrategyPlayer> = listOf(BasicStrategyPlayer(), BasicStrategyPlayer()) // 2 players?

    fun playShoe() {
        var currPlayer = players[0]
        while (shoe.canDrawCard) {
            singleGame(shoe, dealer, players)
            // reset everyone..
            dealer.reset()
            players.forEach{it.reset()}
            println("Number of cards left in shoe: ${shoe.cards.size - shoe.counter}, done: ${shoe.canDrawCard}")
        }
        /*do {
            val card = shoe.drawCard()
            when (dealer.nextMove) {
                Move.HIT -> dealer.(card!!)
                Move.STAND -> println("Dealer hand total: ${dealer.handValue}")
            }
        } while (card != null)*/
    }

    fun checkGameState(): GameState {
        assert (dealer.hands.size == 1 && dealer.hands[0].cards.size >= 2)

        // check if dealer has blackjack
        val dealerHand = dealer.hands[0]
        if (dealerHand.cards.size == 0 && dealerHand.handValue == 21) {
            return GameState.DEALER_BLACKJACK
        }

        // check if any of the players are still alive
        val numAlive = players
                .flatMap { it.hands }
                .sumBy { if (it.handValue <= 21) 1 else 0 }
        return if (numAlive > 0) GameState.ONGOING else GameState.NO_LIVE_PLAYERS
    }

    fun singleGame(shoe: Shoe, dealer: Dealer, players: List<Player>) {
        var gameFinished = false
        var everyone = players.toMutableList()
        everyone.add(dealer)
        var currentPlayerIndex = 0

        var card = shoe.drawCard()
        var cardsDrawn = 0

        // initial hands for everyone (2 cards for everyone)
        while (card != null && cardsDrawn < everyone.size * 2) {
            val currentPlayer = everyone[currentPlayerIndex]
            currentPlayer.cardDealt(card)
            cardsDrawn += 1
            currentPlayerIndex = (currentPlayerIndex + 1) % everyone.size
            card = shoe.drawCard()
        }

        if (!shoe.canDrawCard) {
            println("Deck is finished. Game over..")
            return
        }
        val upcard = dealer.hands[0].cards[1] // upcard is the second card dealt to the dealer

        // check if dealer has blackjack
        if (checkGameState() == GameState.DEALER_BLACKJACK) {
            println("Dealer got BlackJack")
            everyone.forEach { it.print(21) }
            return
        }

        // now, go through players while they do their moves.
        for (player in everyone) {
            while (!player.done) {
                val nextMove = player.popNextMove(upcard)
                if (nextMove == Move.HIT) {
                    val card = shoe.drawCard() ?: break
                    player.cardDealt(card)
                } else if (nextMove == Move.SPLIT) {
                    player.split()
                } else if (nextMove == Move.DOUBLE) {
                    val card = shoe.drawCard() ?: break
                    player.cardDealt(card)
                } else if (nextMove == Move.SURRENDER) {
                    player.surrenderCurrentHand()
                }
            }
            if (checkGameState() == GameState.NO_LIVE_PLAYERS) {
                println("Everyone lost!")
                players.forEach { it.print(21) }
                return
            }
        }

        println("*************** GAME START *******************")
        val dealerHand = dealer.hands[0]
        val dealerHandValue = if (dealerHand.soft) dealerHand.allValues.filter { it <= 21 }.max() ?: dealerHand.handValue else dealerHand.handValue
        println("upcard: $upcard, dealerHand: ${dealerHand.toString} with value $dealerHandValue")
        for (player in players) {
            player.print(dealerHandValue!!)
        }
        //everyone.forEach { it.print() }
        println("*************** GAME END *******************")
    }
}