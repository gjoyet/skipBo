package skipbo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import skipbo.game.Game;
import skipbo.game.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server for Skip-Bo: manages lobby, chat, starts game. This server accepts players while starting
 * a listener for every new player and is the highest instance of the program.
 */
public class SBServer {
    static int playerID = 0;
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

            SBListener sbListen = new SBListener(sock, ++playerID);
            Thread sbListenT = new Thread(sbListen); sbListenT.start();
        } finally {}
    }

    public static SBLobby getLobby() { return serverLobby;}

    public String getPlayerList() {
        StringBuilder allNames = new StringBuilder();
        for(Player p : serverLobby.getPlayerLobby()) {
            allNames.append(p.getName() + "\n");
        }
        return allNames.toString();
    }

    public String getGamesList() {
        StringBuilder allGames = new StringBuilder();
        for(Game g : serverLobby.getGameList()) {
            if(g.gameIsRunning()) {
                allGames.append(g.toString());
            }
        }
        for(Game g : serverLobby.getGameList()) {
            if(!g.gameIsRunning()) {
                allGames.append(g.toString());
            }
        }

        return allGames.toString();
    }

}

