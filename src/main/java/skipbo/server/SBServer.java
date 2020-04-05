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
    static int playerID = 0;
    static int playerCount = 0;
    static SBLobby serverLobby = new SBLobby(); // Should this maybe be non-static?

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
    private static void login(ServerSocket serverSo) throws IOException {
        try {
            Socket sock = serverSo.accept();
            playerCount++;

            SBListener sbListen = new SBListener(sock, ++playerID);
            Thread sbListenT = new Thread(sbListen); sbListenT.start();
        } finally {}
    }

    public static SBLobby getLobby() { return serverLobby;}

    /**
     * @return a String containing all players currently connected.
     */
    public static String getWholePlayerList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            allNames.append(p.getName() + ", ");
        }
        allNames.delete(allNames.length()-2, allNames.length());
        return allNames.toString();
    }

    /**
     * @return a String containing all players currently in the main lobby.
     */
    public static String getPlayerNotIngameList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            if(!p.getStatus().equals(Status.INGAME))
            allNames.append(p.getName() + ",");
        }
        if(allNames.length() > 0) allNames.deleteCharAt(allNames.length()-1);
        return allNames.toString();
    }

    /**
     * @return a String with all games, running and finished.
     */
    public static String[] getGamesList() {
        String[] allGames = new String[serverLobby.getGames().size()];
        int counter = 0;
        servLog.debug("In getGamesList, size of getGame: " + serverLobby.getGames().size());
        for(int i=0; i < serverLobby.getGames().size(); i++) {
            servLog.debug("Got into for.");
            if(serverLobby.getGames().get(i).gameIsRunning()) {
                servLog.debug("Added game.");
                servLog.debug("Game to String: " + serverLobby.getGames().get(i).toString());
                allGames[i] = i + ": " + serverLobby.getGames().get(i).toString();
            }
        }
        for(Game g : serverLobby.getGames()) {
            if(!g.gameIsRunning()) {
                counter++;
                allGames[counter-1] = counter + ": " + g.toString();
            }
        }
        if(allGames.length > 0) {
            allGames[allGames.length - 1] = allGames[allGames.length - 1].substring(0, allGames.length - 1);
        }
        for(String s : allGames) {
            servLog.debug("Content of allGame String: " + s);
        }
        for(int i=0; i < allGames.length; i++) {
            servLog.debug("Content of gameList (with normal for): " + allGames[i]);
        }
        return allGames;
    }

}

