package skipbo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import skipbo.server.Protocol;

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

        System.out.println("Connection successful\n");
        printCommandInfo();
        setName();

        String input;
        while(isLoggedIn) {
            input = scanner.nextLine();
            try {
                forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                System.out.println("Please enter a valid command");
                printCommandInfo();
            }
        }
    }

    /**
     * Sends Information about valid commands to the client
     */
    void printCommandInfo() {
        System.out.println("Commands:\n/change name [name]\n/quit\n"); //List of Commands
    }

    /**
     * Let's client set their name
     */
    void setName() {
        System.out.println("Name can only contain letters or digits (no umlaut) and must have between 3 and 13 characters");
        System.out.println("Your suggested nickname (according to your system username): " + System.getProperty("user.name"));
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        pw.println("SETTO§Nickname§" + name);
    }

    /**
     * Forwards user input to server according to network protocol
     * @param input Input from client
     * @throws NotACommandException If the input doesn't match any command
     */
    void forward(String input) throws NotACommandException {

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
        String[] command = input.split(" ", 2);

        switch (command[0].toLowerCase()) {
            case "/change":
                protocolString = getChangeString(input);
                break;
            case "/quit":
                protocolString = Protocol.LGOUT + "";
                pw.println(protocolString);
                logOut();
                return;
            default:
                throw new NotACommandException();
        }
        pw.println(protocolString);
    }

    /**
     * Builds network protocol string for the "change" command
     * @param input Input from client
     * @return The network protocol string for the "change" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getChangeString(String input) throws NotACommandException {

        String[] line = input.split(" ",3);

        if (line.length < 3) {
            throw new NotACommandException();
        }

        String option = line[1];
        String argument = line[2];

        if (option.equalsIgnoreCase("name")) {
            return Protocol.CHNGE + "§Nickname§" + argument;
        } else {
            throw new NotACommandException();
        }
    }

    void logOut() {
        isLoggedIn = false;
        scanner.close();
        pw.close();
        try {
            sock.close();
        } catch (IOException e) {
            System.out.println("Issue with closing the socket");
        }
    }

}