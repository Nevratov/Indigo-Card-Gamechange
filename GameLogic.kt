open class GameLogic {

    fun getAndRemoveCards(getCards: Int): ArrayList<Pair<String, String>> {
        val removeCards = arrayListOf<Pair<String, String>>()
        for (index in 1 .. getCards)  {
            removeCards.add(gameDeck[0])
            gameDeck.removeAt(0)
        }
        return removeCards
    }

    fun initialCards(removeCards: Int) {
        val startFourCardOnTable = getAndRemoveCards(removeCards)
        print("Initial cards on the table: ")
        for (card in startFourCardOnTable) print("${card.first}${card.second} ")
        println()
        cardsOnTable = startFourCardOnTable
    }

    open fun createDefaultDeck(): MutableList<Pair<String, String>> {
        val defaultDeck = mutableListOf<Pair<String, String>>()
        val suits = arrayOf("♠", "♥", "♦", "♣")
        val ranks = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
        for (suit in suits)
            for (rank in ranks) defaultDeck.add(rank to suit)
        return defaultDeck.shuffled().toMutableList()
    }


}

