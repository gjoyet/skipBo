package skipbo.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import skipbo.server.Protocol;

import static skipbo.client.SBClient.clientLog;

/**
 * Handles user input by forwarding correct network protocol command to the Server
 */
class SBClientListener {
    Socket sock;
    PrintWriter pw;

    /**
     *Creates a Skip-Bo client listener
     * @param sock A client socket
     * @throws IOException: If an I/O error occurs
     */
    SBClientListener(Socket sock) throws IOException {
        this.sock = sock;
        pw = new PrintWriter(sock.getOutputStream(),true);
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
            case "/msg":
                protocolString = getPrivateMessageString(input);
                break;
            case "/broadcast":
                protocolString = getBroadcastString(input);
                break;
            case "/new":
                protocolString = getNewString(input);
                break;
            case "/play":
                protocolString = getPlayString(input);
                break;
            case "/list":
                protocolString = getListString(input);
                break;
            case "/quit":
                protocolString = Protocol.LGOUT + "";
                pw.println(protocolString);
                logOut();
                return;
            default:
                throw new NotACommandException("Please enter a valid command");
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
     * Builds network protocol string for the "broadcast" command
     * @param input Input from client
     * @return The network protocol string for the "broadcast" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getBroadcastString(String input) throws NotACommandException {
        String[] line = input.split(" ", 2);
        if (line.length < 2) {
            throw new NotACommandException("Please add a message");
        }
        String message = line[1];
        return Protocol.CHATM + "§Broadcast§" + message;
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
        if (line.length < 5) {
            throw new NotACommandException("Usage: /play [PlaceFrom] [n] [PlaceTo] [n]");
        }

        String placeFrom = line[1];
        String fromNumber = line[2]; //probably can change to int later
        String placeTo = line[3];
        String toNumber = line[4]; //probably can change to int later

        clientLog.debug("Command: " + Protocol.PUTTO + "§Card§" + placeFrom + "§" + fromNumber + "§" + placeTo + "§" + toNumber);
        return Protocol.PUTTO + "§Card§" + placeFrom + "§" + fromNumber + "§" + placeTo + "§" + toNumber;
    }

    /**
     * Builds network protocol string for the "list" command
     * @param input Input from client
     * @return The network protocol string for the "list" command
     * @throws NotACommandException If the input doesn't match any command
     */
    String getListString(String input) throws NotACommandException {
        String[] line = input.split(" ",2);
        if (line.length < 2) {
            throw new NotACommandException("Please add an option to your command");
        }
        String option = line[1].toLowerCase();
        if (!(option.equals("games") || option.equals("players"))) {
            throw new NotACommandException("Please add a valid option to your command");
        }
        return Protocol.DISPL + "§" + option;
    }

    /**
     * Terminates SBClientListener thread
     */
    void logOut() {
        pw.close();
        try {
            sock.close();
        } catch (IOException e) {
            clientLog.warn("Issue with closing the socket");
        }
    }

}