package skipBo.game;

import java.awt.*;
import java.util.*;

public class Pile {

    public ArrayList<Card> drawPile;
    public ArrayList<Card> stockPile;
    public ArrayList<Card> handCards; // 1 draw pile
    public ArrayList<ArrayList<Card>> buildPiles;// ArrayList for the 4 build decks in the middle
    public ArrayList<ArrayList<Card>> discardPiles;
    private int numOfCards;
    private int size;
    private int id;
    public Player player;


    public void gamePiles(){

        int colourcount = 0;

        ArrayList<Color> colours = new ArrayList<Color>();
        colours.add(Color.yellow);      // Color Nr. 0
        colours.add(Color.orange);      // Color Nr. 1
        colours.add(Color.green);       // Color Nr. 2
        colours.add(Color.red);         // Color Nr. 3
        colours.add(Color.cyan);        // Color Nr. 4

        // Create all Cards for a Game:

        // Normal Cards
        for (int j=0;j<12;j++){
            for (int i=0;i<12;i++){
                Card card = new Card(i+1, colours.get(colourcount));
                drawPile.add(card);
            }
            colourcount++;
            if(colourcount == 3) colourcount=0;
        }
        // Special Cards
        for (int l=0;l<18;l++){
            Card card = new Card(colours.get(4));
            drawPile.add(card);
        }
        // Print Array (ONLY TESTING PURPOSE)
        Object[] test = this.drawPile.toArray();
        for (int i=0;i<144+18;i++) {
            Card karte = (Card) test[i];
            System.out.println(karte.number + " " + karte.col);
        }
        // Add four empty card decks for thr buildDecks
        for(int i=0;i<4;i++){
            ArrayList<Card> deck = new ArrayList<Card>();
            buildPiles.add(deck);
        }
    }

    public Pile() {   // Pile without id (for Game)
        this.drawPile = new ArrayList<Card>(); // 1 draw pile
        this.buildPiles = new ArrayList<ArrayList<Card>>();
    }
    public Pile(int id) {   // Pile with id (for Player)
        this.id = id;
        this.discardPiles = new ArrayList<ArrayList<Card>>();;
        this.stockPile = new ArrayList<Card>(); // 1 draw pile
        this.handCards = new ArrayList<Card>();
    }

    // public Card getTopCard() {
        // return deck.get(0);
    // }

    public int getNumOfCards() {

        return numOfCards;
    }

    public void removeTopCard() {
       // Card topCard = getTopCard();
        //topCard = deck.get(1);
        //int size = deck.size();
        // size--;
    }


    /**
     * switch (Input)
     *      case LOGIN:
     *      // ()
     *      case CHAT_MSG:
     *      // ()
     */
}

