package skipBo.server;

import skipBo.game.Player;

import java.util.ArrayList;

/**
 * Stores all new players and eventually starts game.
 */
public class SBLobby {
    private ArrayList<Player> playerLobby;

    public void addPlayer(Player p) {
        playerLobby.add(p);
    }

    public Player getPlayer(int index) {
        return playerLobby.get(index);
    }

    public int getLength() {
        return playerLobby.size();
    }
}
