
import java.awt.Color;
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

    int colourcount;        // Indexing through colours-Array

    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game (){
        this.buildDeck = buildDeck;
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;

        this.buildDeckNum = buildDeckNum;
        this.drawDeckNum = drawDeckNum;
        this.discardDeckNum = discardDeckNum;

        this.player = player;
        this. gameRunning = false;
        this. turnFinished = false;
    }

    public void setUpGame(){

        ArrayList<Color> colours = new ArrayList<Color>();
                colours.add(Color.yellow);
                colours.add(Color.orange);
                colours.add(Color.green);
                colours.add(Color.yellow);

        // Create 12 Decks, coloured in 4 different colours
        for (int j=0;j<12;j++){

            for (int i=0;i<12;i++){
            Card card = new Card(i+1, colours.get(colourcount));
            drawDeck.add(card);
            }
            colourcount++;
            if(colourcount == 3) colourcount=0;
        }
        Arrays.toString(this.drawDeck.toArray());
    }

    //public void startTurn(){}
    //public void dealCards(){}
    //public void cardOperation(from, to where, which card, ){}
    //public void endGame(){}

    public static void main(String[] args){
        Game spiel = new Game();
    spiel.setUpGame();

    }
}
