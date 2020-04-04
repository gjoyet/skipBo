package skipbo.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * A Skip-Bo client.
 */
public class SBClient {

    public static Logger clientLog = LogManager.getLogger(SBClient.class);

    /**
     * Establishes a connection to the Skip-Bo server via SBClientListener thread and SBServerListener thread and
     * opens the GUI
     * @param args command-line arguments. <hostAddress>:<port> [<username>]
     */
    public static void main(String[] args) {

        try {
            String[] ipAndPort = args[0].split(":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);

            clientLog.info("Connecting to port " + port + "…");
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
            clientLog.fatal("Error while connecting to server.");
        }
    }
}