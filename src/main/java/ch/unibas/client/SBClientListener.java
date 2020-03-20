package ch.unibas.client;

import java.util.Scanner;
import java.net.Socket;

/**
 * Waiting for any action from user input (from console) and forwards command to Server
 */
class SBClientListener {
    Socket sock;
    PrintWriter pw;

    /**
     *Creates a SBClientListener with a Socket
     */
    SBClientListener(Socket sock) {
        this.sock = sock;
        pw = new PrintWriter(sock.getOutputStream(),true);
    }

    /**
     * waits for input from user
     */
    void listen() {
        Scanner scanner = new Scanner(System.in);
        String line;

        while(true) {
            line = scanner.nextLine();
            forward(line);
        }

    }

    /**
     * Forwards user input to server
     */
    static void forward(String string) {

    }

}