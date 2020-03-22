package skipBo.server;

import skipBo.game.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Stores all new players in an ArrayList.
 */
public class SBLobby {
    private ArrayList<Player> playerLobby;

    SBLobby() {
        this.playerLobby = new ArrayList<Player>(0);
    }

    Player getPlayer(int index) {
        return playerLobby.get(index);
    }
    void addPlayer(Player p) {
        playerLobby.add(p);
    }

    void removePlayer(Player p) {
        playerLobby.remove(p);
    }

    SBListener getSBL(int index) {
    return playerLobby.get(index).getSBL();
    }
    int getLength() {
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
     * Checks if name is valid. Only letters and digits are allowed, name can neither
     * be shorter than 2 nor be longer than 13 characters.
     */
    public boolean nameIsValid(String name) {
        if(name.length() > 13 || name.length() < 3) return false;
        for(int i=0; i < name.length(); i++) {
            if(!Character.isLetterOrDigit(name.charAt(i))) return false;
        }

        return true;
    }

}
