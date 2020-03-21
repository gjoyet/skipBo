package skipBo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import skipBo.userExceptions.*;
import skipBo.enums.Protocol;

/**
 * Thread waiting for any action from user input (from console) and forwards command to Server
 */
class SBClientListener implements Runnable {
    Socket sock;
    PrintWriter pw;
    Scanner scanner;

    /**
     *Creates a SBClientListener with a Socket
     */
    SBClientListener(Socket sock, Scanner scanner) throws IOException {
        this.sock = sock;
        pw = new PrintWriter(sock.getOutputStream(),true);
        this.scanner = scanner;
    }

    /**
     * Waits for input from user
     */
    @Override
    public void run() {
        String line;

        while(true) {
            line = scanner.nextLine();
            try {
                forward(line);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please enter a valid command.");
            } catch (NoCommandException e) {
                System.out.println("Please enter a valid command.");
            }

        }
    }

    /**
     * Forwards user input to server
     */
    void forward(String string) throws NoCommandException {

        int pos = string.indexOf(" ");
        String command = string.substring(0,pos);
        String arguments = string.substring(pos);
        arguments = arguments.replace(" ","§");
        Protocol protocolCommand;

        switch (command) {
            case "/chat":
                protocolCommand = Protocol.CHATM;
                break;
            default:
                throw new NoCommandException();
        }

        String protocolString = protocolCommand + arguments;
        pw.println(protocolString);

    }

}