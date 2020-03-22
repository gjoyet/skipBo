package skipBo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server for Skip-Bo: manages lobby, chat, starts game. This server accepts players while starting
 * a listener for every new player and is the highest instance of the program.
 */
public class SBServer {
    static int playerID = 0;
    static SBLobby sbLobby = new SBLobby();

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
     * Accepts new socket and starts a SBListener thread.
     */
    private static void login(ServerSocket serverSo) throws IOException {
        try {
            Socket sock = serverSo.accept();

            SBListener sbListen = new SBListener(sock, ++playerID);
            Thread sbListenT = new Thread(sbListen); sbListenT.start();
        } finally {}
    }

    public static SBLobby getLobby() { return sbLobby;}
}

/* TODO: What goes wrong when name is changed to name that already exists?
    look at what happens during logout (should i close socket, inputStreams etc.?)
 */
