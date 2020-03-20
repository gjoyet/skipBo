package ch.unibas.client;

import java.net.Socket;

/**
 * Client for Skip-Bo. Argument 1 is the port, argument 2 is the IP address.
 * Starts SBClientListener, handels Server input
 */
public class SBClient {

    public static void main(String[] args) {
        try {
            System.out.println("Connecting to port " + args[0] + "…");
            Socket sock = new Socket(args[1],args[0]);

        } catch (Exeption e) {

        }
    }


}