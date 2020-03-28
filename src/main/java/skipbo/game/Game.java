package skipbo.game;

import skipbo.server.SBListener;
import skipbo.server.SBLobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Game {

    public ArrayList<Player> players;
    public Pile piles;
    private Player winner, whosTurn;
    private boolean gameRunning, turnFinished;
    int sizeOfStockPile, playersTurn;


    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game(ArrayList<Player> players) {

        this.players = players;
        this.whosTurn = whosTurn;
        this.winner = winner;
        this.piles = new Pile();
        this.gameRunning = gameRunning;
        this.turnFinished = turnFinished;
        this.sizeOfStockPile = 20;

        start();
        dealCards();

        gameRunning = true;
    }


    /**
     * Returns the drawPile of the main Game
     */
    public ArrayList<Card> getDrawPile() {
        return this.piles.drawPile;
    }

    /**
     * Method start() runs at the beginning of the Game, and waits until
     * boolean value turnFinished
     */

    public void start() {
        while (gameRunning) {
            startTurn();
            while (!turnFinished) {
                try {
                    sleep(100);
                } finally {
                }
            }
            try {
                sleep(100);
            } finally {
            }
        }
    }


    /**
     * setUpGame creates all card Decks and hands out random cards
     * from the main deck to all players in the game.
     * Game and Player Objects have an Object of type Pile, which contain
     * all the different pile-types, which are specifically needed.
     */
    public void dealCards() {

        this.piles.gamePiles();   // Game gets complete set of cards

        for (int i = 0; i < players.size(); i++) {     // Players getting their cards
            Player tempPlayer = players.get(i);
            Random random = new Random();   // Object random for card distribution by chance

            for (int j = 0; j < 5; j++) {    // Draw hand-cards for each player
                Card c = this.getDrawPile().get(random.nextInt(this.getDrawPile().size()));
                tempPlayer.getHandCards().add(c);
                //this.players[i] = tempPlayer; // possibly redundant code
            }
            for (int j = 0; j < sizeOfStockPile; j++) {    // Draw Stock-Pile cards for each player
                Card c = this.getDrawPile().get(random.nextInt(this.getDrawPile().size()));
                tempPlayer.getStockPile().add(c);
                //this.players[i] = tempPlayer; // possibly redundant cod
            }

            //   Print Array (ONLY TESTING PURPOSE)
            Object[] tempHandCards = tempPlayer.getHandCards().toArray();
            System.out.println();
            System.out.println(tempPlayer.getName());
            for (int t = 1; t < tempHandCards.length; t++) {
                Card karte = (Card) tempHandCards[t];
                System.out.println("|" + karte.number + "| " + karte.col);
            }
        }
    }

    /**
     * Method to be executed at the start of each player's turn
     * and to fill their hand cards.
     */

    public void startTurn() {
        turnFinished = false;
        Player ply = PlayerMaster.getPlayerByID(playersTurn);
        //Execute: sysout to Player ply = "It's your turn!"
        //Execute to Players ply: sysout ("Your hand cards are now: " + ply.getHandCards().toString());
        ply.fillHandCards();
        //Execute to all players in lobby: sysout ("Gave " + this.getName() + " " + their missing " + toFill + " cards");

    }

    /**
     * Method playToMiddle processes which player, which Card index they wish to play
     * and which buildPile they wish to play to and carries out the command if valid.
     * Furthermore, removes the specified card from their hand cards.
     *
     * @param id
     * @param handCardIndex
     * @param buildDeckIndex
     */

    public void playToMiddle(int id, int handCardIndex, int buildDeckIndex) {
        Player currentPly = PlayerMaster.getPlayerByID(id);     //TODO: Does this make sense? Maybe better with SBLobby.getPlayer - Discuss with GUILL
        Card card = currentPly.getHandCards().get(handCardIndex);

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        ArrayList<Card> specBuildPile = buildPiles.get(buildDeckIndex);

        Card topCard = specBuildPile.get(specBuildPile.size());

        if (!(specBuildPile.isEmpty())) {
            if (topCard.number == (card.number - 1)) {
                specBuildPile.add(card);
                currentPly.getHandCards().remove(card);
                topCard = card;     // could be redundant
                // Execute: card op valid - update board and hand cards.
                if (card.number == 12) {
                    //Execute: make that BuildPile go away
                    for (int i = 0; i < 12; i++) {    // remove all cards from the buildPile if the top card is 12
                        specBuildPile.remove(i);
                    }
                }
            } else {
                //EXECUTE: the move is invalid!
            }
        } else {
            if (card.number == 1) {
                specBuildPile.add(card);
                //Execute: update build pile
            } else {
                //Execute: invalid move! Card has to be Num 1 to be first on an empty build pile.
            }
        }
    }

    /**
     * This method plays a hand card into a discard pile of the player's choice
     * Parameter handCardIndex to know which hand card should be selected to be played
     * Parameter id to know whose turn it is. Furthermore, removes the specified card
     * from player's hand.
     * Parameter discardPileIndex to know which Discard pile to play to.
     *
     * @param id
     * @param handCardIndex
     * @param discardPileIndex
     */

    public void playToDiscard(int id, int handCardIndex, int discardPileIndex) {
        Player currentPly = PlayerMaster.getPlayerByID(id);     //TODO: Does this make sense? Maybe better with SBLobby.getPlayer - Discuss with GUI
        ArrayList<ArrayList<Card>> discardPiles = piles.discardPiles;
        ArrayList<Card> specDiscard = discardPiles.get(discardPileIndex);

        Card card = currentPly.getHandCards().get(handCardIndex);

        specDiscard.add(card);
        currentPly.getHandCards().remove(card);
        //Execute: update Discard pile and hand cards
        endTurn();

    }

    /**
     * This method is to play the top card from the Stock Pile to a build pile
     * of the player's choosing.
     * Param id for player, Param id to know which Build pile to play to.
     *
     * @param id
     * @param buildPileIndex
     */

    public void playFromStockToMiddle(int id, int buildPileIndex) {
        Player currentPlayer = PlayerMaster.getPlayerByID(id);  //TODO: Does this make sense? Maybe better with SBLobby.getPlayer - Discuss with GUILL
        ArrayList<Card> stockPile = currentPlayer.getStockPile();
        Card stockCard = stockPile.get(stockPile.size());

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        var specBuildPile = buildPiles.get(buildPileIndex);

        Card topCard = specBuildPile.get(specBuildPile.size());

        if (!(specBuildPile.isEmpty())) {
            if (topCard.number == (stockCard.number - 1)) {
                specBuildPile.add(stockCard);
                currentPlayer.getStockPile().remove(stockCard);
                topCard = stockCard;     // could be redundant
                // Execute: card op valid - update board and stock pile card.
                if (stockCard.number == 12) {
                    //Execute: make that BuildPile go away
                    for (int i = 0; i < 12; i++) {    // remove all cards from the buildPile if the top card is 12
                        specBuildPile.remove(i);
                    }
                }
            } else {
                //EXECUTE: the move is invalid!
            }
        } else {
            if (stockCard.number == 1) {
                specBuildPile.add(stockCard);
                //Execute: update build pile
            } else if (stockCard.number != 1) {
                //Execute: invalid move! Card 1 has to be first card on build pile.
            }
        }

        if (currentPlayer.getStockPile().isEmpty()) {
            endGame(currentPlayer);
        }
    }

    /**
     * Method to be run at the end of a player's turn, which
     * then changes turn from one player to the next.
     */
    public void endTurn() {
        turnFinished = true;
        if (!(playersTurn == 3)) {
            playersTurn++;
        } else {
            playersTurn = 0;
        }
    }


    /**
     * A method to run the End Game network protocol to
     *
     * @param winner
     */
    public void endGame(Player winner) {
        gameRunning = false;
        //EXECUTE: EndGame Protocol (sysout: Player X won game!) server shut, reshuffle etc.
    }

    /**
     * A sleep method with long parameter to sleep the given amount of time.
     *
     * @param ms
     */
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

//        Game spiel = new Game(players);
//        spiel.setUpGame();
    }
}
