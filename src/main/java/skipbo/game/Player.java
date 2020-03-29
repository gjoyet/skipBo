package skipbo.game;
import skipbo.server.SBListener;

import java.util.*;

/**
 * Class Player constructs a Player object with defined parameters
 */

public class Player {

    private String name;
    private int id;
    private SBListener sbListen;
    private Pile piles;
    private PlayerStatus status;
    /**
     * Player constructor to build a Player object with
     * an int id, String name and a ServerListener object specific to the player
     * @param id
     * @param name
     * @param sbListen
     */
    public Player (int id, String name, SBListener sbListen) {
        this.id = id;
        this.name = name;
        this.sbListen = sbListen;
        this.piles = new Pile(id);
        this.status = PlayerStatus.valueOf("WAITING");
    }

    public ArrayList<Card> getStockPile(){
        return this.piles.stockPile;
    }

    public ArrayList<Card> getHandCards() { // returns the name of the Player object
        return this.piles.handCards;
    }

    public String getName() { // returns the name of the Player object
        return this.name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public SBListener getSBL() {
        return this.sbListen;
    }

    public int getId() { //returns the id number of the player object
        return this.id;
    }

    public PlayerStatus getStatus()  { return this.status; }

    public void changeStatus(PlayerStatus ps) { this.status = ps; }

    public void addCardToHand(Card card) {
        this.getHandCards().add(card);
    }


    /**
     * Fills player's cards at the beginning of their turn,
     * if they have less than the maximum of five cards in their hand.
     */
    public void fillHandCards() {
        ArrayList<Card> handCards = this.getHandCards();
        int sizeOfHand = handCards.size();
        if (sizeOfHand < 5) {
            int toFill = (5 - sizeOfHand);
            for (int i = 0; i < toFill; ++i) {
                Card card = piles.getDrawPileTopCard();
                addCardToHand(card);
                piles.drawPile.remove(piles.getDrawPileTopCard());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //Execute to all players in lobby: sysout ("Gave " + this.getName() + " " + their missing " + toFill + " cards");
    }


}
