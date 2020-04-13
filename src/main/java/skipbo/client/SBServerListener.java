package skipbo.client;

import skipbo.server.Protocol;
import skipbo.server.NoCommandException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static skipbo.client.SBClient.clientLog;

/**
 * Thread waiting for any input from server and executes the input on client
 */
class SBServerListener implements Runnable {
    Socket socket;
    BufferedReader br;
    ChatGraphic chatGraphic;
    Boolean isLoggedIn = true;

    SBServerListener(Socket socket, ChatGraphic chatGraphic) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.chatGraphic = chatGraphic;
    }

    /**
     * Constantly reads input from server. Forwards input to executeCommand method which executes the input on client
     */
    @Override
    public void run() {
        String input;

        while(isLoggedIn) {
            try {
                input = br.readLine();
                executeCommand(input);
                //clientLog.debug(input);
            } catch (IOException e) {
                clientLog.warn("Error with reading input from server");
            } catch (NoCommandException | IllegalArgumentException e) {
                clientLog.warn("Error with network protocol command");
            }
        }
    }

    /**
     * Executes commands coming from the server according to network protocol
     * @param commandLine Network protocol string from server
     * @throws NoCommandException If commandLine string doesn't match network protocol
     */
    void executeCommand(String commandLine) throws NoCommandException  {
        String[] command = commandLine.split("§", 3);
        Protocol protocol = Protocol.valueOf(command[0]);

        switch (protocol) {
            case CHATM:
                chatGraphic.printChatMessage(command[2]);
                break;
            case CHNGE:
            case SETTO:
                chatGraphic.changePlayerName(command[2]);
                break;
            case PUTTO:
                putTo(command);
                //TODO
                break;
            case LGOUT:
                logOut();
                break;
            case PRINT:
                chatGraphic.printInfoMessage(command[2]);
                break;
            case NWGME:
                newGame(command);
                break;
            case DISPL:
                chatGraphic.printCommandList();
                break;
            default:
                throw new NoCommandException();
        }
    }

    void putTo(String[] command) {
        String[] argument = command[2].split("§");
        if (command[1].equals("Response")) {
            if (argument[0].equals("H")) {
                if (argument[2].equals("B")) {
                    chatGraphic.getGameGraphic().handToBuild(Integer.parseInt(argument[1]),
                            Integer.parseInt(argument[3]), argument[4]);
                } else { //2nd pile must be Discard
                    chatGraphic.getGameGraphic().handToDiscard(Integer.parseInt(argument[1]),
                            Integer.parseInt(argument[3]), argument[4]);
                }
            } else { //pile must be Discard
                chatGraphic.getGameGraphic().discardToBuild(Integer.parseInt(argument[1]),
                        Integer.parseInt(argument[3]), argument[4]);
            }
        } else {
            chatGraphic.getGameGraphic().stockToBuild(Integer.parseInt(argument[1]),
                    Integer.parseInt(argument[3]), argument[4], argument[5], Integer.parseInt(argument[6]));
        }
    }

    void newGame(String[] command) {
        if (command[1].equals("Names")) {
            chatGraphic.setGameGraphic();
            String[] names = command[2].split("§");
            chatGraphic.getGameGraphic().setOpponentNames(names);

        } else if (command[1].equals("Cards")) {
            String[] cards = command[2].split("§");
            chatGraphic.getGameGraphic().setInitialCards(cards);
        }
    }

    /**
     * Terminates SBServerListener thread and sends status message to client
     */
    void logOut() {
        try {
            br.close();
            socket.close();
            //printMessage("Logout successful");
        } catch (IOException e) {
            clientLog.warn("Error with closing BufferedReader or Socket");
        }
        isLoggedIn = false;
    }

}