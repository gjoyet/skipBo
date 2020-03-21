package skipBo.server;

import skipBo.game.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Stores all new players and eventually starts game.
 */
public class SBLobby {
    private ArrayList<Player> playerLobby;

    public SBLobby() {
        this.playerLobby = new ArrayList<Player>(0);
    }

    public void addPlayer(Player p) {
        playerLobby.add(p);
    }

    public Player getPlayer(int index) {
        return playerLobby.get(index);
    }

    public int getLength() {
        return playerLobby.size();
    }

    /**
     * Checks if the name is already in use.
     */
    public boolean nameIsTaken(String name){
        if(playerLobby.isEmpty()) return false;
        return playerLobby.stream().anyMatch(pl -> pl.getName().equals(name));
    }

    /**
     * Checks if name is valid.
     */
    public boolean nameIsValid(String name) {
        if(name.length() > 13 || name.length() == 0) return false;
        for(int i=0; i < name.length(); i++) {
            if(!Character.isLetterOrDigit(name.charAt(i))) return false;
        }

        return true;
    }

}
