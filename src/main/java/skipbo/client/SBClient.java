package skipbo.client;

import java.io.IOException;
import java.net.Socket;

/**
 * A Skip-Bo client.
 */
public class SBClient {

    /**
     * Establishes a connection to the Skip-Bo server via SBClientListener thread and SBServerListener thread
     *
     * @param args command-line arguments. Argument 1 is the port, argument 2 is the IP address.
     */
    public static void main(String[] args) {

        try {
            System.out.println("Connecting to port " + args[0] + "…");
            Socket sock = new Socket(args[1], Integer.parseInt(args[0]));

            //Start SBClientListener Thread
            SBClientListener clientListener = new SBClientListener(sock);
            Thread cListener = new Thread(clientListener);
            cListener.start();

            //GUI
            ChatGraphic frame = new ChatGraphic(clientListener);
            frame.setVisible(true);

            //Start SBServerListener Thread
            SBServerListener serverListener = new SBServerListener(sock, frame);
            Thread sListener = new Thread(serverListener);
            sListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}