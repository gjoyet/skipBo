package skipbo.game;

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

    /**
     * The method gamePiles() creates all cards of a full set  and
     * puts them in a "ArrayList<Card>-pile" (named: drawPile).
     *
     * It also creates an "ArrayList<ArrayList<Cards>>-set" (named: buildPiles),
     * which contains four empty "ArrayList<Card>-piles".
     *
     * Every card has a number and a colour.

     * Booth piles are elements of a Pile-Object.
     */

    public void gamePiles(){    // All Cards are created and the build-pile is added

        int colourcount = 0;

        ArrayList<Color> colours = new ArrayList<Color>();     // Save four different colours
        colours.add(Color.yellow);      // Color index Nr. 0
        colours.add(Color.orange);      // Color index Nr. 1
        colours.add(Color.green);       // Color index Nr. 2
        colours.add(Color.red);         // Color index Nr. 3
        colours.add(Color.cyan);        // Color index Nr. 4

        for (int j=0;j<12;j++){        // Add Normal Cards (144 pcs.)
            for (int i=0;i<12;i++){
                Card card = new Card(i+1, colours.get(colourcount));
                this.drawPile.add(card);
            }
            colourcount++;
            if(colourcount == 3) colourcount=0;
        }
        for (int l=0;l<18;l++){       // Add Special Cards (18 pcs.)

            Card card = new Card(colours.get(4));
            this.drawPile.add(card);
        }
        Object[] AllCards = this.drawPile.toArray();     // Print Array (ONLY TESTING PURPOSE)
        for (int i=0;i<144+18;i++) {

            Card karte = (Card) AllCards[i];
            System.out.println("|"+karte.number +"| " + karte.col);
        }
        for(int i=0;i<4;i++){        // Add four empty card piles (buildPiles)

            ArrayList<Card> deck = new ArrayList<Card>();
            this.buildPiles.add(deck);
        }
    }

    /**
     * The Pile-constructor is overloaded because whe have different
     * types of piles for the Game and Player.
     *
     * The Pile-constructor for the Player needs the Player-ID, this
     * assures that we can identify the different piles.
     */
    public Pile() {   // Pile without id (for Game)
        this.drawPile = new ArrayList<Card>();
        this.buildPiles = new ArrayList<ArrayList<Card>>();
    }
    public Pile(int id) {   // Pile with id (for Player)
        this.id = id;
        this.discardPiles = new ArrayList<ArrayList<Card>>();;
        this.stockPile = new ArrayList<Card>();
        this.handCards = new ArrayList<Card>();
    }

    // public Card getTopCard() {}

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

