package indigo
import GameLogic
import cardsOnTable
import gameDeck
import kotlin.random.Random
import kotlin.system.exitProcess

class Player(
    var deck: ArrayList<Pair<String, String>> = arrayListOf(),
    var scoreWon: Int = 0,
    var cardsWon: Int = 0
): GameLogic() {

    private fun printCardsInHandPlayer() {
        for (i in deck.indices) print("${i + 1})${deck[i].first}${deck[i].second} ")
    }

    private fun printCardsInHandNpc() {
        for (i in deck.indices) print("${deck[i].first}${deck[i].second} ")
    }

    fun messageBeforeMove() {
        if (cardsOnTable.isNotEmpty()) {
            println(
                "\n${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last().first}${cardsOnTable.last().second}"
            )
            if (cardsOnTable.size == 52) println("Game Over")
        } else println("\nNo cards on the table")
    }

    fun turnPlayer() {
        if (deck.isNotEmpty()) {
            print("Cards in hand: ")
            printCardsInHandPlayer()
            checkChoseCard()
        } else {
            if (gameDeck.isNotEmpty()) deck.addAll(getAndRemoveCards(6))
            else return
            turnPlayer()
        }
    }

    private fun checkChoseCard() {
        var chooseCard = ""
        try {
            println("\nChoose a card to play (1-${deck.size}):")
            chooseCard = readln()
            cardsOnTable.add(deck[chooseCard.toInt() - 1])
            getPoints(deck[chooseCard.toInt() - 1])
            deck.removeAt(chooseCard.toInt() - 1)
        } catch (e: java.lang.Exception) {
            if (chooseCard.lowercase() == "exit") { println("Game Over"); exitProcess(0) }
            checkChoseCard()
        }
    }

    fun turnNpc() {
        if (deck.isNotEmpty()) {
            printCardsInHandNpc()
            val chooseCard = npcTurnLogic()
            println("\nComputer plays ${chooseCard.first}${chooseCard.second}")
            cardsOnTable.add(chooseCard)
            getPoints(chooseCard)
            deck.remove(chooseCard)
        } else {
            if (gameDeck.isNotEmpty()) deck.addAll(getAndRemoveCards(6))
            else return
            turnNpc()
        }
    }

    private fun npcTurnLogic(): Pair<String, String> {
        val allCardCandidates = checkCardCandidate()
        val allSuits = checkSuit(deck)
        val allRanks = checkRanks(deck)

        // 1 card in hand
        if (deck.size == 1) return deck[0]
        // 1 card candidate in hand
        else if (allCardCandidates.size == 1) return allCardCandidates[0]
        // no cards on table || no cards candidate in hand
        else if (cardsOnTable.isEmpty() || allCardCandidates.isEmpty()) {
            for (i in allSuits.indices)
                if (allSuits[i].size > 1) return allSuits[i][Random.nextInt(0, allSuits[i].lastIndex)]
            for (i in allRanks.indices)
                if (allRanks[i].size > 1) return allRanks[i][Random.nextInt(0, allRanks[i].lastIndex)]
            return deck[Random.nextInt(0, deck.size)]
        }
        // 2 and more card candidate in hand
        else {
            val allSuits = checkSuit(allCardCandidates)
            val allRanks = checkRanks(allCardCandidates)
            for (i in allSuits.indices)
                if (allSuits[i].size > 1) return allSuits[i][Random.nextInt(0, allSuits[i].lastIndex)]
            for (i in allRanks.indices)
                if (allRanks[i].size > 1) return allRanks[i][Random.nextInt(0, allRanks[i].lastIndex)]
            return allCardCandidates[Random.nextInt(0, allCardCandidates.size)]
        }

    }

    private fun checkCardCandidate(): ArrayList<Pair<String, String>> {
        val candidates: ArrayList<Pair<String, String>> = arrayListOf()
        if (cardsOnTable.isNotEmpty()) {
            for (card in deck) if (card.first == cardsOnTable.last().first || card.second == cardsOnTable.last().second) {
                candidates.add(card)
            }
        }
        return candidates
    }

    private fun checkSuit(deck: ArrayList<Pair<String, String>>): Array<ArrayList<Pair<String, String>>> {
        val suits = Array(4) { ArrayList<Pair<String, String>>() }

        for (card in deck) {
            when (card.second) {
                "♠" -> suits[0].add(card)
                "♥" -> suits[1].add(card)
                "♦" -> suits[2].add(card)
                "♣" -> suits[3].add(card)
            }
        }
//        for (i in suits.indices) {
//            if (suits[i].size > 1) return suits[i][Random.nextInt(0, suits[i].lastIndex)]
//        }
        return suits
    }

    private fun checkRanks(deck: ArrayList<Pair<String, String>>):  Array<ArrayList<Pair<String, String>>> {
        val ranks = Array(13) { ArrayList<Pair<String, String>>() }

        for (card in deck) {
            when (card.first) {
                "2" -> ranks[0].add(card)
                "3" -> ranks[1].add(card)
                "4" -> ranks[2].add(card)
                "5" -> ranks[3].add(card)
                "6" -> ranks[4].add(card)
                "7" -> ranks[5].add(card)
                "8" -> ranks[6].add(card)
                "9" -> ranks[7].add(card)
                "10" -> ranks[8].add(card)
                "J" -> ranks[9].add(card)
                "Q" -> ranks[10].add(card)
                "K" -> ranks[11].add(card)
                "A" -> ranks[12].add(card)
            }
        }
//        for (i in ranks.indices) {
//            if (ranks[i].size > 1) return ranks[i][Random.nextInt(0, ranks[i].lastIndex)]
//        }
        return ranks
    }


    private fun getPoints(playedCard: Pair<String, String>) {
        if (cardsOnTable.size > 1 && (playedCard.first == cardsOnTable[cardsOnTable.lastIndex - 1].first ||
            playedCard.second == cardsOnTable[cardsOnTable.lastIndex - 1].second)) {
            for (card in cardsOnTable) if (card.first in arrayOf("10", "J", "Q", "K", "A")) scoreWon += 1
            cardsWon += cardsOnTable.size
            cardsOnTable.clear()
        }
    }


}
