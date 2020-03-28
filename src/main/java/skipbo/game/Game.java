package skipbo.game;

import skipbo.server.SBListener;

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

    public void cardOperation() {

    }

    public void endTurn() {
        turnFinished = true;
        if (!(playersTurn == 3)) {
            playersTurn++;
        } else {
            playersTurn = 0;
        }
    }


    //public void cardOperation(from, to where, which card, ){}

    public void endGame(Player winner) {
        gameRunning = false;
        //ServerEvents: EndGame Protocol
    }

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
