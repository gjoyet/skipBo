package skipbo.client;

import java.io.IOException;
import java.net.Socket;

/**
 * A Skip-Bo client.
 */
public class SBClient {

    /**
     * Establishes a connection to the Skip-Bo server via SBClientListener thread and SBServerListener thread and
     * opens the GUI
     * @param args command-line arguments. <hostAddress>:<port> [<username>]
     */
    public static void main(String[] args) {

        try {
            String[] iPAndPort = args[0].split(":");
            String ip = iPAndPort[0];
            int port = Integer.parseInt(iPAndPort[1]);

            System.out.println("Connecting to port " + port + "…");
            Socket sock = new Socket(ip , port);

            //Start SBClientListener Thread
            SBClientListener clientListener = new SBClientListener(sock);

            //GUI
            ChatGraphic frame;
            if (args.length == 2) {
                frame = new ChatGraphic(clientListener, args[1]);
            } else {
                frame = new ChatGraphic(clientListener);
            }
            frame.addWindowListener(new WindowHandler(clientListener));
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