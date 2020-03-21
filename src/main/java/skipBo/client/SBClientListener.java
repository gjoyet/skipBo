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
    SBClientListener(Socket sock) throws IOException {
        this.sock = sock;
        pw = new PrintWriter(sock.getOutputStream(),true);
        this.scanner = scanner = new Scanner(System.in);
    }

    /**
     * Waits for input from user
     */
    @Override
    public void run() {
        String input;

        System.out.println("Connection successful");
        //Client has to set name
        System.out.println("Commands:\n/change name [name]\n/quit"); //List of Commands
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        pw.println("SETTO§Nickname§" + name);

        while(true) {
            input = scanner.nextLine();
            try {
                forward(input);
            } catch (IndexOutOfBoundsException | NoCommandException e) {
                System.out.println("Please enter a valid command");
            }
        }
    }


    /**
     * Forwards user input to server
     */
    void forward(String input) throws NoCommandException {

        if (input.isEmpty()) {
            return;
        }
        String protocolString;

        //It's a chat message
        if (!(input.startsWith("/"))) {
            protocolString = Protocol.CHATM + "§Global§" + input;
            pw.println(protocolString);
            return;
        }

        //It's not a chat message
        int pos = input.indexOf(" ");
        String command;
        if (pos < 0) {
            command = input;
        } else {
            command = input.substring(0, pos);
        }


        switch (command) {
            case "/change":
                protocolString = getChangeString(input);
                break;
            case "/quit":
                protocolString = Protocol.LGOUT + "";
                break;
            default:
                throw new NoCommandException();
        }
        pw.println(protocolString);
    }

    /**
     * builds network protocol string for the "change"-command
     * @return: network protocol string for the "change"-command
     */
    String getChangeString(String input) throws NoCommandException {

        int pos = input.indexOf(" ",8);
        String option = input.substring(8,pos);
        String argument;

        if (option.equalsIgnoreCase("name")) {
            argument = input.substring(13);
            return Protocol.CHNGE + "§name§" + argument;
        } else {
            throw new NoCommandException();
        }

    }

}