package ch.unibas.game;
//import ch.unibas.enums;
import java.util.*;
import java.net.*;

public class Game {
    public ArrayList<ArrayList<Card>> buildDeck;
    public ArrayList<Card> drawDeck = new ArrayList<Card>();
    public ArrayList<Card> discardDeck;
    public int buildDeckNum;
    public int drawDeckNum;
    public int discardDeckNum;

    private boolean gameStarted, turnFinished;

    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game (){
        Pile drawPile = new Pile(drawDeck, drawDeckNum);

    }
    public static void main(String[] args) {
    }
}
