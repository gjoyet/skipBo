package skipBo.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client for Skip-Bo. Argument 1 is the port, argument 2 is the IP address.
 * Starts SBClientListener, handles Server input
 */
public class SBClient {

    public static void main(String[] args) {
        try {
            System.out.println("Connecting to port " + args[0] + "…");
            Socket sock = new Socket(args[1], Integer.parseInt(args[0]));

            setName();

            //Start SBClientListener Thread
            SBClientListener clientListener = new SBClientListener(sock);
            Thread listener = new Thread(clientListener);
            listener.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();


    }

}