import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    public Object[] players;
    public Pile piles;
    private Player winner, whosTurn;
    private boolean gameRunning, turnFinished;
    Random random = new Random();
    int sizeOfDtockPile;
    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game (Object[] players){

        this.whosTurn = whosTurn;
        this.players = players;
        this.piles = new Pile();
        this.gameRunning = gameRunning;
        this.turnFinished = turnFinished;
        this.winner = winner;
        this.sizeOfDtockPile = 20;
    }

    public ArrayList<Card> getDrawPile(){
        return this.piles.drawPile;
    }

    public void setUpGame() {

        this.piles.gamePiles();   // Game gets complete set of cards

        for(int i=0; i < players.length; i++) {     // Players getting their cards

            for (int j = 0; j < 5 ;j++){    // Draw hand-cards for each player
                        Card c = (Card) this.getDrawPile().get(random.nextInt(this.getDrawPile().size()));
                        Player tempPlayer = (Player) this.players[i];
                        tempPlayer.getStockPile().add(c);
                        this.players[i] = (Player) tempPlayer;
            }
            for (int j = 0; j < sizeOfDtockPile ;j++){    // Draw Stock-Pile cards for each player
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

        Object[] players = new Object[4];
        Player sp1 = new Player(1, "Manfred");
        Player sp2 = new Player(2, "Franz Ferdinandt");
        Player sp3 = new Player(3, "Peter");
        Player sp4 = new Player(3, "Meinrad");
        players[0] = sp1;
        players[1] = sp2;
        players[2] = sp3;
        players[3] = sp4;

        Game spiel = new Game(players);
        spiel.setUpGame();
    }
}
