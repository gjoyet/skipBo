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
        this.scanner = new Scanner(System.in);
    }

    /**
     * Waits for input from user and forwards input to server according to network protocol
     */
    @Override
    public void run() {

        //System.out.println("Connection successful");
        //printCommandInfo();
        //setName();

        String input;
        while(isLoggedIn) {
            input = scanner.nextLine();
            try {
                forward(input);
            } catch (NotACommandException e) {
                System.out.println(e.getMessage());
                //printCommandInfo();
            }
        }
        System.out.println("Client ended run method");
    }

    /**
     * Sends Information about valid commands to the client
     */
    /*void printCommandInfo() {
        //List of Commands
        String listOfCommands = "Commands:\n/change name [name]\n/change status [ready|waiting]\n" +
                "/msg [name] [message]\n/new game\n/play [PlaceFrom] [n] [PlaceTo] [n] | not yet implemented\n/quit\n";
        System.out.println(listOfCommands);
    }*/

    /**
     * Let's client set their name
     */
/*    void setName() {
        System.out.println("Name can only contain letters or digits and must have between 3 and 13 characters");
        System.out.println("Your suggested nickname (according to your system username): " + System.getProperty("user.name"));
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        pw.println("SETTO§Nickname§" + name);
    }*/

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
            case "/msg":
                protocolString = getPrivateMessageString(input);
                break;
            case "/new":
                protocolString = getNewString(input);
                break;
            case "/play":
                protocolString = getPlayString(input);
                break;
            case "/quit":
                protocolString = Protocol.LGOUT + "";
                pw.println(protocolString);
                logOut();
                System.out.println("forward returned"); //TODO delete
                return;
            default:
                throw new NotACommandException("Please enter a valid command");
        }
        pw.println(protocolString);
        System.out.println("ended forward client"); //TODO delete
    }


    /**
     * Builds network protocol string for the "change" command
     * @param input Input from client
     * @return The network protocol string for the "change" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getChangeString(String input) throws NotACommandException {

        String[] line = input.split(" ",3);

        if (line.length < 3) throw new NotACommandException("Please add an argument to your command");

        String option = line[1];
        String argument = line[2];

        if (option.equalsIgnoreCase("name")) {
            return Protocol.CHNGE + "§Nickname§" + argument;

        } else if (option.equalsIgnoreCase("status")) {
            if (argument.equalsIgnoreCase("ready") || argument.equalsIgnoreCase("waiting")) {
                return Protocol.CHNGE + "§Status§" + argument.toUpperCase();
            }
        }
        throw new NotACommandException("Please enter a valid command");
    }

    /**
     * Builds network protocol string for the "msg" command
     * @param input Input from client
     * @return The network protocol string for the "msg" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getPrivateMessageString(String input) throws NotACommandException {
        String[] line = input.split(" ", 3);
        if (line.length < 3) {
            throw new NotACommandException("Please add a name and a message to your command");
        }
        String name = line[1];
        String message = line[2];
        return Protocol.CHATM + "§Private§" + name + "§" + message;
    }

    /**
     * Builds network protocol string for the "new" command
     * @param input Input from client
     * @return The network protocol string for the "new" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getNewString(String input) throws NotACommandException {
        String[] line = input.split(" ",2);
        if (line.length < 2) {
            throw new NotACommandException("Please enter a valid command");
        }
        if (line[1].equalsIgnoreCase("game")) {
            return Protocol.NWGME + "§New";
        }
        throw new NotACommandException("Please enter a valid command");
    }

    /**
     * Builds network protocol string for the "play" command
     * @param input Input from client
     * @return The network protocol string for the "play" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getPlayString(String input) throws NotACommandException {
        String[] line = input.split(" ", 5);
        if (line.length < 4) {
            throw new NotACommandException("Usage: /play [PlaceFrom] [n] [PlaceTo] [n]");
        }
        String placeFrom = line[1];
        String fromNumber; //probably can change to int later
        String placeTo;
        String toNumber; //probably can change to int later
        if (placeFrom.equalsIgnoreCase("StockPile")) {
            fromNumber = "-1";
            placeTo = line[2];
            toNumber = line[3];
        } else {
            if (line.length < 5) {
                throw new NotACommandException("Usage: /play [PlaceFrom] [n] [PlaceTo] [n]");
            }
            fromNumber = line[2];
            placeTo = line[3];
            toNumber = line[4];
        }
        throw new NotACommandException("Command not implemented"); //delete when implemented
        // TODO build and return correct String
    }

    /**
     * Terminates SBClientListener thread
     */
    void logOut() {
        System.out.println("entered logOut Client"); //TODO delete
        isLoggedIn = false;
        scanner.close();
        pw.close();
        try {
            sock.close();
            System.out.println("closed socket"); //TODO delete
        } catch (IOException e) {
            System.out.println("Issue with closing the socket");
        }
    }

}