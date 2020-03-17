package ch.unibas.game;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.net.*;

public class Player {
    private String name; //name of the user

    private InetAddress ip;
    private int port;

    /**
     * Player constructor to build a Player object with
     * a String name and ip and port numbers.
     * @param name
     * @param ip
     * @param port
     */
    public Player (String name, InetAddress ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName(){ // returns the name of the Player object
        return name;
    }


}
