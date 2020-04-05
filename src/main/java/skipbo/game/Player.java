package skipbo.game;

import skipbo.server.SBListener;

import java.util.ArrayList;

import static skipbo.server.SBServer.servLog;

/**
 * Class Player constructs a Player object with defined parameters
 */
public class Player {

    private String name;
    private int id;
    private SBListener sbListen;
    private Game game;
    private Pile piles;
    private Status status;

    /**
     * Player constructor to build a Player object with
     * an int id, String name and a ServerListener object specific to the player
     *
     * @param id
     * @param name
     * @param sbListen
     */
    public Player(int id, String name, SBListener sbListen) {
        this.id = id;
        this.name = name;
        this.sbListen = sbListen;
        this.piles = new Pile(id);
        this.status = Status.valueOf("WAITING");

    }

    public String getName() {
        return this.name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public SBListener getSBL() {
        return this.sbListen;
    }

    public Status getStatus() {
        return this.status;
    }

    public void changeStatus(Status ps) {
        this.status = ps;
        servLog.info(this.name + " status set to: " + ps + ".");
    }

    public Game getGame() {
        return this.game;
    }

    public void changeGame(Game game) {
        this.game = game;
    }

    public ArrayList<Card> getStockPile() {
        return this.piles.stockPile;
    }

    public ArrayList<Card> getHandCards() { // returns the name of the Player object
        return this.piles.handCards;
    }

    public ArrayList<ArrayList<Card>> getDiscardPile() {
        return this.piles.discardPiles;
    }

    public void addCardToHand(Card card) {
        this.getHandCards().add(card);
    }

}
