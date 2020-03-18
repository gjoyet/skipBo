


import java.util.*;

public class Game {
    public ArrayList<ArrayList<Card>> buildDeck; // ArrayList for the 4 build decks in the middle
    public ArrayList<Card> drawDeck = new ArrayList<Card>(); // 1 draw pile
    public ArrayList<Card> discardDeck;
    public int buildDeckNum;
    public int drawDeckNum;
    public int discardDeckNum;

    private Player player;
    private boolean gameRunning, turnFinished;

    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game (){
        Pile drawPile = new Pile(drawDeck, drawDeckNum);

    }
    public static void main(String[] args) {
    }

    //public void setUpGame(){}
    //public void startTurn(){}
    //public void dealCards(){}
    //public void cardOperation(from, to where, which card, ){}

    //public void endGame(){}
}
