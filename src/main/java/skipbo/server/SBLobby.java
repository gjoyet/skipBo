package skipbo.server;

import skipbo.game.Game;
import skipbo.game.Player;

import java.util.ArrayList;

/**
 * Stores all new players in an ArrayList.
 */
public class SBLobby {
    private ArrayList<Player> playerLobby;
    private ArrayList<Game> gameList;

    SBLobby() {
        this.playerLobby = new ArrayList<Player>(0);
    }

    ArrayList<Player> getPlayerLobby() { return this.playerLobby; }

    ArrayList<Game> getGameList() { return this.gameList; }

    int getLength() { return playerLobby.size(); }

    SBListener getSBL(int index) { return playerLobby.get(index).getSBL(); }

    Player getPlayer(int index) {
        return playerLobby.get(index);
    }

    Player getPlayerByName(String name) {
        for(Player p : this.playerLobby) {
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

    void addPlayer(Player p) { playerLobby.add(p); }

    void removePlayer(Player p) { playerLobby.remove(p); }

    void addGame(Game g) { gameList.add(g); }


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
