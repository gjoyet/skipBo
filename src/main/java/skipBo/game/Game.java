package skipBo.game;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    public Object[] players;
    public Pile piles;
    private Player winner, whosTurn;
    private boolean gameRunning, turnFinished;
    int sizeOfStockPile;

    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game (Object[] players){

        this.players = players;
        this.whosTurn = whosTurn;
        this.winner = winner;
        this.piles = new Pile();
        this.gameRunning = gameRunning;
        this.turnFinished = turnFinished;
        this.sizeOfStockPile = 20;
    }

    /**
     * Returns the drawPile of the main Game
     */
    public ArrayList<Card> getDrawPile(){

        return this.piles.drawPile;
    }

    /**
     * setUpGame creates all card Decks and hands out random cards
     * from the main deck to all players in the game.
     * Game and Player Objects have an Object of type Pile, which contain
     * all the different pile-types, which are specifically needed.
     */
    public void setUpGame() {

        this.piles.gamePiles();   // Game gets complete set of cards

        for(int i=0; i < players.length; i++) {     // Players getting their cards

            Random random = new Random();   // Object random for card distribution by chance

            for (int j = 0; j < 5 ;j++){    // Draw hand-cards for each player

                        Card c = (Card) this.getDrawPile().get(random.nextInt(this.getDrawPile().size()));
                        Player tempPlayer = (Player) this.players[i];
                        tempPlayer.getStockPile().add(c);
                        this.players[i] = (Player) tempPlayer;
            }
            for (int j = 0; j < sizeOfStockPile ;j++){    // Draw Stock-Pile cards for each player

                        Card c = (Card) this.getDrawPile().get(random.nextInt(this.getDrawPile().size()));
                        Player tempPlayer = (Player) this.players[i];
                        tempPlayer.getHandCards() .add(c);
                        this.players[i] = (Player) tempPlayer;
            }

            //   Print Array (ONLY TESTING PURPOSE)
            Player tempPlayer = (Player) this.players[i];
            Object[] tempHandCards = tempPlayer.getHandCards().toArray();
            System.out.println("");
            System.out.println(tempPlayer.getName());
            for(int t = 1 ; t<tempHandCards.length;t++) {
                Card karte = (Card) tempHandCards[t];
                System.out.println("|"+karte.number +"| " + karte.col);
            }
        }
    }


    //public void startTurn(){}
    //public void dealCards(){}
    //public void cardOperation(from, to where, which card, ){}
    //public void endGame(){}

    public static void main(String[] args){

//        Object[] players = new Object[4];
//        Player sp1 = new Player(1, "Manfred");
//        Player sp2 = new Player(2, "Franz Ferdinandt");
//        Player sp3 = new Player(3, "Peter");
//        Player sp4 = new Player(3, "Meinrad");
//        players[0] = sp1;
//        players[1] = sp2;
//        players[2] = sp3;
//        players[3] = sp4;

//        Game spiel = new Game(players);
//        spiel.setUpGame();
    }
}
