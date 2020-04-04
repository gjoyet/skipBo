package skipbo.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Pile {

    public ArrayList<Card> drawPile;
    public ArrayList<Card> stockPile;
    public ArrayList<Card> handCards; // 1 draw pile
    public ArrayList<ArrayList<Card>> buildPiles;// ArrayList for the 4 build decks in the middle
    public ArrayList<ArrayList<Card>> discardPiles;
    public Player player;
    private int numOfCards;
    private int size;
    private int id;

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

        for (int i = 0; i < 4; i++) {        // Add four empty card piles (buildPiles)

            ArrayList<Card> deck = new ArrayList<Card>();
            this.buildPiles.add(deck);
        }

    }

    public String handCardPrint(Player player) {
        ArrayList<Card> handCards = player.getHandCards();
        int len = handCards.size();
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = handCards.get(i).number;
        }
        return Arrays.toString(a);
    }

    public String buildPilesPrint() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ArrayList<Card> specBuildPile = buildPiles.get(i);
            if (specBuildPile.isEmpty()) {
                str.append("Build pile " + i + " is : [ ]");
            } else {
                str.append("Build pile " + i + " is: [" + specBuildPile.get(specBuildPile.size() - 1).number + "]" + "\t");
            }
        }
        return str.toString();
    }

    public String discardPilesPrint(Player player) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ArrayList<Card> specDiscardPile = player.getDiscardPile().get(i);
            int[] printArray = new int[specDiscardPile.size()];
            for (int j = 0; j < specDiscardPile.size(); j++) {
                printArray[j] = specDiscardPile.get(j).number;
            }
            str.append("Discard Pile " + i + " of " + player.getName() + " is: " + Arrays.toString(printArray) + "\t");
        }
        return str.toString();
    }

    public Card getDrawPileTopCard() {
        return drawPile.get(drawPile.size() - 1);
    }

    public int getNumOfStockPile() {
        return stockPile.size();
    }

}

