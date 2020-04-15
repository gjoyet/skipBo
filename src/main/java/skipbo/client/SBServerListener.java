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
            } catch (NoCommandException e) {
                clientLog.warn("Error with network protocol command (NoCommand)");
            } catch (IllegalArgumentException e) {
                clientLog.warn("Error with network protocol command (IllegalArgument)");
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
            case CHECK:
                check(command);
                break;
            case ENDGM:
                chatGraphic.endGame(command[2]);
                break;
            default:
                throw new NoCommandException();
        }
    }

    void putTo(String[] command) {
        clientLog.debug("got into putTo with command " + command[1]);
        String[] argument = command[2].split("§");
        if (command[1].equals("Response")) {
            if (argument[0].equals("H")) {
                if (argument[2].equals("B")) {
                    chatGraphic.getGameGraphic().handToBuild(Integer.parseInt(argument[1]),
                            Integer.parseInt(argument[3]), argument[4], "", -1);
                } else { //2nd pile must be Discard
                    chatGraphic.getGameGraphic().handToDiscard(Integer.parseInt(argument[1]),
                            Integer.parseInt(argument[3]), argument[4], "", -1);
                }
            } else if (argument[0].equals("D")) { //pile must be Discard
                chatGraphic.getGameGraphic().discardToBuild(Integer.parseInt(argument[1]),
                        Integer.parseInt(argument[3]), argument[4]);
            }
        } else if (command[1].equals("StockResponse")) {
            chatGraphic.getGameGraphic().stockToBuild(Integer.parseInt(argument[3]), argument[4], argument[5],
                    Integer.parseInt(argument[6]), "", -1);
        } else { //must be Update
            if (argument.length == 3) { //Discard to build
                clientLog.debug("updated discard to build");
                chatGraphic.getGameGraphic().discardToBuild(Integer.parseInt(argument[0]), Integer.parseInt(argument[1]),
                        argument[2]);
            } else if (argument[0].equals("D")) { //Hand to discard
                clientLog.debug("updated hand to discard");
                chatGraphic.getGameGraphic().handToDiscard(Integer.parseInt(argument[1]), Integer.parseInt(argument[2]),
                        argument[3], argument[4], Integer.parseInt(argument[5]));
            } else if (argument[0].equals("B")){ //Hand to build
                clientLog.debug("updated hand to build");
                chatGraphic.getGameGraphic().handToBuild(Integer.parseInt(argument[1]), Integer.parseInt(argument[2]),
                        argument[3], argument[4], Integer.parseInt(argument[5]));
            } else { //Must be stock to build
                clientLog.debug("updated stock to build");
                chatGraphic.getGameGraphic().stockToBuild(Integer.parseInt(argument[1]), argument[2], argument[3],
                        Integer.parseInt(argument[4]), argument[5], Integer.parseInt(argument[6]));
            }
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

    private void check(String[] command) {
        clientLog.debug("got into check method");
        if (command[1].equalsIgnoreCase("HandCards")) {
            String[] cards = command[2].split("§");
            String string = "";
            for (int i = 0; i < cards.length; i++) {
                string = string + cards[i];
            }
            //clientLog.debug("cards array= " + string);
            String[] colours = new String[cards.length/2];
            int[] numbers = new int[colours.length];
            for (int i = 0, j = 0; i < colours.length; i++) {
                colours[i] = cards[j++];
                numbers[i] = Integer.parseInt(cards[j++]);
            }
            StringBuilder str = new StringBuilder();
            for(int m = 0; m < colours.length; m++){
                str.append(colours[m]);
            }
            StringBuilder numArr = new StringBuilder();
            for(int k = 0; k <numbers.length;k++){
                numArr.append(numbers[k]);
            }
            //clientLog.debug("Num Array = " + numArr);
            //clientLog.debug("Colours array = " + str);
            chatGraphic.getGameGraphic().updateHandCards(colours, numbers);
            //clientLog.debug("got into update method in GameGraphic");
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