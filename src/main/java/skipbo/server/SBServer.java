package skipbo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.game.Status;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server for Skip-Bo: manages lobby, chat, starts game. This server accepts players while starting
 * a listener for every new player and is the highest instance of the program.
 */
public class SBServer implements Runnable {
    int playerID = 0;
    int playerCount = 0;

    public SBLobby serverLobby = new SBLobby(); // Should this maybe be non-static?
    public ArrayList<SBListener> sbListenerList = new ArrayList<>(); // not needed in program itself, just for testing

    public static Logger servLog = LogManager.getLogger(SBServer.class);

    int port;

    /**
     * Creates new SBServer Object.
     * @param port: port on which ServerSocket will be based on.
     */
    public SBServer(int port) {
        this.port = port;
    }

    public void run() {
        ServerSocket sbServerSocket = null;

        try {
            sbServerSocket = new ServerSocket(port);
            servLog.info("Server waiting for port " + port + ".");
        } catch(IOException ioe) {
            servLog.fatal("Issue with opening Serversocket. Try with another port.");
        }

        while(true) {
            try {
                login(sbServerSocket);
            } catch (IOException e) {
                servLog.fatal("Issue with login.");
            }
        }

    }

    /**
     * Accepts a new socket and starts a SBListener thread.
     */
    private void login(ServerSocket serverSo) throws IOException {
        try {
            Socket sock = serverSo.accept();
            playerCount++;

            SBListener sbListen = new SBListener(this, sock, ++playerID);
            Thread sbListenT = new Thread(sbListen); sbListenT.start();
            sbListenerList.add(sbListen);
        } finally {}
    }

    public SBLobby getLobby() { return serverLobby;}

    public ArrayList<SBListener> getSblList() { return sbListenerList; }

    /**
     * @return a String containing all players currently connected.
     */
    public String getWholePlayerList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            allNames.append(p.getName() + " (" + p.getStatus().toString() + "), ");
        }
        allNames.delete(allNames.length()-2, allNames.length());
        return allNames.toString();
    }

    /**
     * @return a String containing all players currently in the main lobby.
     */
    public String getPlayerNotIngameList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            if(!p.getStatus().equals(Status.INGAME))
            allNames.append(p.getName() + ", ");
        }
        if(allNames.length() > 0) {
            allNames = allNames.replace(allNames.length()-2, allNames.length(), "");
        }
        return allNames.toString();
    }

    /**
     * @return a String with all games, running and finished.
     */
    public synchronized String[] getGamesList() {
        String[] allGames = new String[serverLobby.getGames().size()];
        int counter = 0;
        for(Game g : serverLobby.getGames()) {
            if(g.gameIsRunning()) {
                allGames[counter++] = counter + ": " + g.toString(false);
            }
        }
        for(Game g : serverLobby.getGames()) {
            if(!g.gameIsRunning()) {
                allGames[counter++] = counter + ": " + g.toString(false);
            }
        }
        return allGames;
    }

}

