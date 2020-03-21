package skipBo.server;

import skipBo.game.Player;

import java.util.ArrayList;

/**
 * Stores all new players and eventually starts game.
 */
public class SBLobby {
    static private ArrayList<Player> playerLobby;

    public static void addPlayer(Player p) {
        playerLobby.add(p);
    }

    public static Player getPlayer(int index) {
        return playerLobby.get(index);
    }

    public static int getLength() {
        return playerLobby.size();
    }

    /**
     * Checks if the name is already in use.
     */
    public static boolean nameIsTaken(String name){
        return playerLobby.stream().anyMatch(pl -> pl.getName().equals(name));
    }

    /**
     * Checks if name is valid.
     */
    public static boolean nameIsValid(String name) {
        if(name.length() > 13 || name.length() == 0) return false;
        for(int i=0; i < name.length(); i++) {
            if(!Character.isLetterOrDigit(name.charAt(i))) return false;
        }

        return true;
    }

}
