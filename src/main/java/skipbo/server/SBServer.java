package skipbo.server;

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
    static SBLobby serverLobby = new SBLobby();
    //public static Game currentGame; //added static Game field currentGame that will run every game

    public static void main(String[] args) {
        ServerSocket sbServerSocket = null;

        try {
            sbServerSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Waiting for port " + args[0]);
        } catch(IOException ioe) {
            System.out.println("Issue with opening Serversocket. Try with another port.");
        }

        while(true) {
            try {
                login(sbServerSocket);
            } catch (IOException e) {
                System.out.println("Issue with login.");
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

