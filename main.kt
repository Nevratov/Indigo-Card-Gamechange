import indigo.Player

var gameDeck: MutableList<Pair<String, String>> = mutableListOf()
var cardsOnTable: MutableList<Pair<String, String>> = mutableListOf()

fun chooseAction() {
    println("Indigo Card Game")
    whoIsPlayFirst()
}

fun whoIsPlayFirst() {
    println("Play first?")
    when (readln().lowercase()) {
        "yes" -> game("player")
        "no" -> game("npc")
        else -> whoIsPlayFirst()
    }
}

fun game(firstTurn: String) {
    val operation = GameLogic()
    val player = Player()
    val npc = Player()

    gameDeck = operation.createDefaultDeck()
    operation.initialCards(4)
    player.deck.addAll(operation.getAndRemoveCards(6))
    npc.deck.addAll(operation.getAndRemoveCards(6))
    player.messageBeforeMove()

    fun printGetPoints(whoIsWin: String) {
        println("$whoIsWin wins cards\nScore: Player ${player.scoreWon} - Computer ${npc.scoreWon}\n" +
                "Cards: Player ${player.cardsWon} - Computer ${npc.cardsWon}")
    }

    val playFirst = firstTurn
    var turn = playFirst
    var lastWinCards = ""

    do {
        if (turn == "player") {
            player.turnPlayer()
            if (cardsOnTable.size == 0) {
                printGetPoints("Player")
                lastWinCards = "player"
            }
            player.messageBeforeMove()
            turn = "npc"
        } else {
            npc.turnNpc()
            if (cardsOnTable.size == 0) {
                printGetPoints("Computer")
                lastWinCards = "npc"
            }
            npc.messageBeforeMove()
            turn = "player"
        }
    } while (gameDeck.isNotEmpty() || player.deck.isNotEmpty() || npc.deck.isNotEmpty())

    fun cardEnd() {
        // Distribution of the remaining cards on the table
        if (lastWinCards == "player") {
            player.cardsWon += cardsOnTable.size
            for (card in cardsOnTable) if (card.first in arrayOf("10", "J", "Q", "K", "A")) player.scoreWon += 1
        }
        else if (lastWinCards == "npc") {
            npc.cardsWon += cardsOnTable.size
            for (card in cardsOnTable) if (card.first in arrayOf("10", "J", "Q", "K", "A")) npc.scoreWon += 1
        }
        else
            if (playFirst == "player") {
                player.cardsWon += cardsOnTable.size
                for (card in cardsOnTable) if (card.first in arrayOf("10", "J", "Q", "K", "A")) player.scoreWon += 1
            } else {
                npc.cardsWon += cardsOnTable.size
                for (card in cardsOnTable) if (card.first in arrayOf("10", "J", "Q", "K", "A")) npc.scoreWon += 1
            }

        // Scoring points for winning cards
        if (player.cardsWon > npc.cardsWon) player.scoreWon += 3
        else if (npc.cardsWon > player.cardsWon) npc.scoreWon += 3
        else
            if (playFirst == "player") player.scoreWon += 3 else npc.scoreWon += 3

        println("Score: Player ${player.scoreWon} - Computer ${npc.scoreWon}\n" +
                "Cards: Player ${player.cardsWon} - Computer ${npc.cardsWon}\nGame Over")
    }

    cardEnd()

}


fun main() {
    chooseAction()
}
