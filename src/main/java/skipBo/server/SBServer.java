package skipBo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server for Skip-Bo: manages lobby, chat, starts game. This server accepts players while starting
 * a listener for every new player and is the highest instance of the program.
 */
public class SBServer {
    static int playerCounter = 0;
    static ArrayList<SBServerListener> allListeners;

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
     * @param serverSo
     */
    private static void login(ServerSocket serverSo) throws IOException {
        try {
            Socket sock = serverSo.accept();

            SBServerListener sbListen = new SBServerListener(sock, ++playerCounter);
            Thread sbListenT = new Thread(sbListen); sbListenT.start();
            allListeners.add(sbListen);

        } catch (IOException e) {
            System.out.println("Issue with opening socket.");
        }
    }


}

