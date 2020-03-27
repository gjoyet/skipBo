package skipbo.game;

import skipbo.server.ServerResponse;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Class PlayerMaster to watch over all players at given times
 * Contains methods to identify given players by name, socket or id.
 * Server uses this class to add a new player and to check username and player capacity
 */
public class PlayerMaster {
    //List of players that are in the game, can be shuffled through with a loop for every turn
    public static ArrayList<skipbo.game.Player> players = new ArrayList<skipbo.game.Player>(4);
    public static int numPlayers;
    public static int minPlayers = 0;
    Socket sock;
    static ServerResponse srp;
    public static String name;

    /**
     * Add a player to the game with given parameters.
     * Returns an enum of ServerResponse that the server can then process
     * and communicate to all clients. Checks username validity and player capacity
     * @param name
     * @param socket
     * @return
     */

    public static ServerResponse newPlayer(String name, Socket socket){
        if (players.size() > 4){
            return ServerResponse.SERVER_FULL;
        }
        return ServerResponse.LOGIN_SUCCESS;
    }

    /**
     * Returns the player object with the corresponding name
     *
     * @param name
     * @return player
     */

    public static Player getPlayerName(String name) {
        for (skipbo.game.Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public static Player getPlayerByID(int id) {
        for (Player pl : players) {
            if (pl.getId() == id) {
                return pl;
            }
        }
        return null;
    }
}
