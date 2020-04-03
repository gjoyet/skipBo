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
     * The method gamePiles() creates all cards of a full set and
     * puts them in a "ArrayList<Card>-pile" (named: drawPile).
     * <p>
     * It also creates an "ArrayList<ArrayList<Cards>>-set" (named: buildPiles),
     * which contains four empty "ArrayList<Card>-piles".
     * <p>
     * Every card has a number and a colour.
     * <p>
     * Booth piles are elements of a Pile-Object.
     */

    public void gamePiles() {    // All Cards are created, and the build-pile is added

        int colourCount = 0;

        ArrayList<Color> colours = new ArrayList<Color>();     // Save five different colours
        colours.add(Color.yellow);      // Color index Nr. 0
        colours.add(Color.orange);      // Color index Nr. 1
        colours.add(Color.green);       // Color index Nr. 2
        colours.add(Color.red);         // Color index Nr. 3
        colours.add(Color.cyan);        // Color index Nr. 4

        for (int j = 0; j < 12; j++) {        // Add Normal Cards (144 pcs.)
            for (int i = 0; i < 12; i++) {
                Card card = new Card(i + 1, colours.get(colourCount));
                this.drawPile.add(card);
            }
            colourCount++;
            if (colourCount == 3) colourCount = 0;
        }
        for (int l = 0; l < 18; l++) {       // Add Special Cards (18 pcs.)
            Card card = new Card(colours.get(4));
            this.drawPile.add(card);
        }
        Object[] AllCards = this.drawPile.toArray();     // Print Array (ONLY TESTING PURPOSE)
        for (int i = 0; i < 144 + 18; i++) {

            Card karte = (Card) AllCards[i];
            // System.out.println("|" + karte.number + "| " + karte.col);
        }
        for (int i = 0; i < 4; i++) {        // Add four empty card piles (buildPiles)

            ArrayList<Card> deck = new ArrayList<Card>();
            this.buildPiles.add(deck);
        }

    }

    public String handCardPrint(Player player) {
        ArrayList<Card> handCards = player.getHandCards();
        int[] a = new int[5];
        for (int i = 0; i < 5; i++) {
            a[i] = handCards.get(i).number;
        }
        return Arrays.toString(a);
    }

    /**
     * The Pile-constructor is overloaded because we have different
     * types of piles for the Game and Player.
     * <p>
     * The Pile-constructor for the Player needs the Player-ID, this
     * assures that we can identify the different piles.
     */
    public Pile() {   // Pile without id (for Game)
        this.drawPile = new ArrayList<Card>();
        this.buildPiles = new ArrayList<ArrayList<Card>>();
    }

    public Pile(int id) {   // Pile with id (for Player)
        this.id = id;
        this.discardPiles = new ArrayList<ArrayList<Card>>();

        for (int i = 0; i < 4; i++) {
            ArrayList<Card> deck = new ArrayList<Card>();
            discardPiles.add(deck);
        }

        this.stockPile = new ArrayList<Card>();
        this.handCards = new ArrayList<Card>();
    }

    public Card getDrawPileTopCard() {
        return drawPile.get(drawPile.size());
    }

    /**
     * Returns the specific hand card at the index in the parameter
     *
     * @param index
     * @return
     */

    public Card getHandCardAtIndex(int index) {
        return this.handCards.get(index);
    }

    public int getNumOfStockPile() {
        return stockPile.size();
    }

    public void removeDrawPileTopCard() {       // apparently redundant as ArrayList has a remove() method
        Card card = this.getDrawPileTopCard();
    }
}

