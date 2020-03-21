package skipBo.client;

import java.io.IOException;
import java.net.Socket;

/**
 * Client for Skip-Bo. Argument 1 is the port, argument 2 is the IP address.
 * Starts SBClientListener, handles Server input
 */
public class SBClient {
    public static void main(String[] args) {

        try {
            System.out.println("Connecting to port " + args[0] + "…");
            Socket sock = new Socket(args[1], Integer.parseInt(args[0]));

            //Start SBClientListener Thread
            SBClientListener clientListener = new SBClientListener(sock);
            Thread cListener = new Thread(clientListener);
            cListener.start();

            //Start SBServerListener Thread
            SBServerListener serverListener = new SBServerListener(sock);
            Thread sListener = new Thread(serverListener);
            sListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
