package skipbo.game;

import skipbo.server.SBListener;

import java.util.ArrayList;

import static skipbo.server.SBServer.servLog;

/**
 * Class Player constructs a Player object with defined parameters
 */
public class Player {

    private String name;
    private final int id;
    private final SBListener sbListen;
    private Game game;
    private final Pile piles;
    private Status status;

    /**
     * Player constructor to build a Player object with
     * an int id, String name and a ServerListener object specific to the player
     *
     * @param id       (A player specific int that corresponds to the Player object)
     * @param name     (A String name, which will be the player's name)
     * @param sbListen (SBListener object for communication between server and client)
     */
    public Player(int id, String name, SBListener sbListen) {
        this.id = id;
        this.name = name;
        this.sbListen = sbListen;
        this.piles = new Pile(id);
        this.status = Status.valueOf("WAITING");

    }

    /**
     * Simplified constructor for unit-testing purposes.
     */
    public Player(int id) {
        this.id = id;
        this.name = "" + id;
        this.sbListen = null;
        this.piles = new Pile(id);
        this.status = Status.INGAME;
    }

    /**
     * Returns the name of the player
     **/
    public String getName() {
        return this.name;
    }

    /**
     * Changes player name to given name parameter
     **/
    public void changeName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of the Player object
     **/
    public int getId() {
        return this.id;
    }

    /**
     * Returns the SBListener Object of the player
     **/
    public SBListener getSBL() {
        return this.sbListen;
    }

    /**
     * Returns the status of the player
     **/
    public Status getStatus() {
        return this.status;
    }

    /**
     * Method to change the status of a player
     *
     * @param ps (The status enum to change to)
     */
    public void changeStatus(Status ps) {
        this.status = ps;
        servLog.info(this.name + " status set to: " + ps + ".");
    }

    /**
     * returns the current game
     *
     * @return The game object
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Changes the game according to its parameter
     *
     * @param game (The game that is to be assigned)
     */
    public void changeGame(Game game) {
        this.game = game;
    }

    /**
     * Method to return the player's stock pile
     *
     * @return The stock pile object of a specific player
     */
    public ArrayList<Card> getStockPile() {
        return this.piles.stockPile;
    }

    /**
     * Method to return the hand cards of a player
     *
     * @return The ArrayList object that contains the player's hand cards.
     */
    public ArrayList<Card> getHandCards() { // returns the name of the Player object
        return this.piles.handCards;
    }

    /**
     * Empties handcards of player.
     */
    public void clearHandCards() { this.piles.handCards.clear(); }

    /**
     * For testing purposes.
     *
     * @param hc: handcards of players are set to this.
     */
    public void setHandCards(ArrayList<Card> hc) {
        this.piles.handCards = hc;
    }

    /**
     * Method to return the discard piles of a player
     *
     * @return The ArrayList object that contains the player's discard piles.
     */

    public ArrayList<ArrayList<Card>> getDiscardPile() {
        return this.piles.discardPiles;
    }

    /**
     * Method to add a card to a player's hand
     *
     * @param card (The card object you wish to add)
     */
    public void addCardToHand(Card card) {
        this.getHandCards().add(card);
    }

}
