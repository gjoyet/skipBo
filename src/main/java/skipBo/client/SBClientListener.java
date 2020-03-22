package skipBo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import skipBo.userExceptions.*;
import skipBo.enums.Protocol;

/**
 * Thread waiting for any action from user input (from terminal) and forwards command to Server
 */
class SBClientListener implements Runnable {
    Socket sock;
    PrintWriter pw;
    Scanner scanner;
    Boolean isLoggedIn = true;

    /**
     *Creates a Skip-Bo client listener
     * @param sock A client socket
     * @throws IOException: If an I/O error occurs
     */
    SBClientListener(Socket sock) throws IOException {
        this.sock = sock;
        pw = new PrintWriter(sock.getOutputStream(),true);
        this.scanner = scanner = new Scanner(System.in);
    }

    /**
     * Waits for input from user and forwards input to server according to network protocol
     */
    @Override
    public void run() {

        printWelcomeInformation();
        setName();

        String input;
        while(isLoggedIn) {
            input = scanner.nextLine();
            try {
                forward(input);
            } catch (IndexOutOfBoundsException | NoCommandException e) {
                System.out.println("Please enter a valid command");
            }
        }
    }

    /**
     * Sends general Information to the client about how the server works and client
     */
    void printWelcomeInformation() {
        System.out.println("Connection successful\n");
        System.out.println("Commands:\n/change name [name]\n/quit\n"); //List of Commands
        System.out.println("Name can only contain letters or digits and cannot be longer than 13 characters");
    }

    /**
     * Let's client set their name
     */
    void setName() {
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        pw.println("SETTO§Nickname§" + name);
    }

    /**
     * Forwards user input to server according to network protocol
     * @param input Input from client
     * @throws NoCommandException If the input doesn't match any command
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


        switch (command.toLowerCase()) {
            case "/change":
                protocolString = getChangeString(input);
                break;
            case "/quit":
                protocolString = Protocol.LGOUT + "";
                isLoggedIn = false;
                break;
            default:
                throw new NoCommandException();
        }
        pw.println(protocolString);
    }

    /**
     * Builds network protocol string for the "change" command
     * @param input Input from client
     * @return The network protocol string for the "change" command
     * @throws NoCommandException If the input doesn't match any command
     */
    String getChangeString(String input) throws NoCommandException {

        int pos = input.indexOf(" ",8);
        String option = input.substring(8,pos);
        String argument;

        if (option.equalsIgnoreCase("name")) {
            argument = input.substring(13);
            return Protocol.CHNGE + "§Nickname§" + argument;
        } else {
            throw new NoCommandException();
        }

    }

}