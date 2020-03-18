//package ch.unibas.game;

import java.net.Socket;
import java.util.ArrayList;

public class PlayerMaster {
    //List of players that are in the game, can be shuffled through with a loop for every turn
    public static ArrayList<Player> players = new ArrayList<Player>(4);
    public static int numPlayers;
    public static int minPlayers = 0;
    Socket sock;
    static ServerResponse srp;
    public static String name;

    /**
     * Add a player to the game with given parameters.
     * @param name
     * @param socket
     * @return
     */

    public static ServerResponse newPlayer(String name, Socket socket){
        if(nameIsTaken(name)){
            return ServerResponse.NAME_USED;
        }else if (players.size() > 4){
            return ServerResponse.SERVER_FULL;
        }
        return srp.LOGIN_SUCCESS;
    }

    public static Player getPlayerName(String name){
        for (Player p : players){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public static boolean nameIsTaken(String name){
        for (Player pl : players){
            if(pl.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
