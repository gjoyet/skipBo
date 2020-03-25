package skipbo.game;//package ch.unibas.game;
//import ch.unibas.game.Pile;
import skipbo.server.SBListener;

import java.util.*;

/**
 * Class Player constructs a Player object with defined parameters
 */

public class Player {

    private String name;
    private int id;
    private SBListener sbListen;
    // private InetAddress ip;
    // private Socket sock;
    private int port;
    private Pile piles;
    /**
     * Player constructor to build a Player object with
     * a String name  and port numbers.
     * @param id
     * @param name
     * @param sbListen
     */
    public Player (int id, String name, SBListener sbListen) {
        this.id = id;
        this.name = name;
        this.sbListen = sbListen;
        this.piles = new Pile(id);
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

    public void changeName(String name) {this.name = name;}

    public SBListener getSBL() { return this.sbListen; }

    public int getId(){ //returns the id number of the player object
        return this.id;
    }



}
