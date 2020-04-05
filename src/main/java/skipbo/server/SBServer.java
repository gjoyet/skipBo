package skipbo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.game.Status;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server for Skip-Bo: manages lobby, chat, starts game. This server accepts players while starting
 * a listener for every new player and is the highest instance of the program.
 */
public class SBServer {
    static int playerID = 0;
    static int playerCount = 0;
    static SBLobby serverLobby = new SBLobby(); // Should this maybe be non-static?

    public static Logger servLog = LogManager.getLogger(SBServer.class);

    public static void main(String[] args) {
        ServerSocket sbServerSocket = null;

        try {
            sbServerSocket = new ServerSocket(Integer.parseInt(args[0]));
            servLog.info("Server waiting for port " + args[0] + ".");
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

    public static String getWholePlayerList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            allNames.append(p.getName() + ", ");
        }
        allNames.delete(allNames.length()-2, allNames.length());
        return allNames.toString();
    }

    public static String getPlayerNotIngameList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            if(!p.getStatus().equals(Status.INGAME))
            allNames.append(p.getName() + ",");
        }
        if(allNames.length() > 0) allNames.deleteCharAt(allNames.length()-1);
        return allNames.toString();
    }

    public static String getGamesList() {
        StringBuilder allGames = new StringBuilder();
        int counter = 0;
        for(Game g : serverLobby.getGames()) {
            if(g.gameIsRunning()) {
                allGames.append(++counter + ": " + g.toString());
            }
        }
        if(allGames.length() > 0) allGames.append("\n");
        for(Game g : serverLobby.getGames()) {
            if(!g.gameIsRunning()) {
                allGames.append(++counter + ": " + g.toString());
            }
        }
        if(allGames.length() > 0) allGames.deleteCharAt(allGames.length()-1);
        return allGames.toString();
    }

}

