//package ch.unibas.game;
//import ch.unibas.game.Pile;
import java.util.*;
import java.net.*;

public class Player {
    private String name; //name of the user

    private int id;
    private InetAddress ip;
    private Socket sock;
    private int port;

    Pile handpile;
    /**
     * Player constructor to build a Player object with
     * a String name and ip and port numbers.
     * @param id
     * @param name
     * @param socket
     */
    public Player (int id, String name, Socket socket){
        this.id = id;
        this.name = name;
        sock = socket;
    }

    public String getName() { // returns the name of the Player object
        return name;
    }

//    public Pile getHandpile (){
//        Pile handpile = new Pi
//        return ;
//    }


}
