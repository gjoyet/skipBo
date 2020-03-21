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

        //Client has to set name
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        pw.println("SETTO§Nickname§" + name);

        while(true) {
            input = scanner.nextLine();
            try {
                forward(input);
            } catch (IndexOutOfBoundsException | NoCommandException e) {
                System.out.println("Please enter a valid command.");
            }
        }
    }


    /**
     * Forwards user input to server
     */
    void forward(String input) throws NoCommandException {

        Protocol networkCommand;
        String networkOption;
        String networkArgument;

        //It's a chat message
        if (!(input.startsWith("/"))) {
            networkCommand = Protocol.CHATM;
            networkOption = "Global";
            networkArgument = input;
            pw.println(networkCommand + "§" + networkOption + "§" + networkArgument);
            return;
        }

        //It's not a chat message
        int pos = input.indexOf(" ");
        String command = input.substring(0, pos);

        switch (command) {
            case "/change":
                networkCommand = Protocol.CHNGE;
            default:
                throw new NoCommandException();
        }

    }

    //TODO: check if command has right amount of option/argument

}