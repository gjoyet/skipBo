package skipBo.client;

import skipBo.enums.Protocol;
import skipBo.userExceptions.NoCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Thread waiting for any input from server and executes the input on client
 */
class SBServerListener implements Runnable {
    Socket socket;
    BufferedReader br;
    Boolean isLoggedIn = true;

    SBServerListener(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            } catch (IOException e) {
                System.out.println("Error with reading input from server");
            } catch (NoCommandException | IllegalArgumentException | NullPointerException e) {
                System.out.println("Error with network protocol command");
            }
        }
    }

    /**
     * Executes commands coming from the server according to network protocol
     * @param commandLine Network protocol string from server
     * @throws NoCommandException If commandLine string doesn't match network protocol
     */
    void executeCommand(String commandLine) throws NoCommandException  {
        String[] command = commandLine.split("§");
        Protocol protocol = Protocol.valueOf(command[0]);

        switch (protocol) {
            case CHATM:
                sendChatMessage(command);
                break;
            case CHNGE:
                changeTo(command);
                break;
            case LGOUT:
                logOut();
                break;
            case PRINT:
                print(command);
                break;
            default:
                throw new NoCommandException();
        }
    }

    /**
     * Sends a chat message to the client
     * @param command String array according to network protocol with command, option and arguments
     */
    void sendChatMessage(String[] command) {
        System.out.println(command[2]);
    }

    /**
     * Sends status message after changing name
     * @param command String array according to network protocol with command, option and arguments
     */
    void changeTo(String[] command) {
        printMessage(command[2]);
    }

    /**
     * Terminates SBServerListener thread and sends status message to client
     */
    void logOut() {
        isLoggedIn = false;
        printMessage("Logout successful");
    }

    /**
     * Processes PRINT command and sends message according to network protocol
     * @param command String array according to network protocol with command, option and arguments
     */
    void print(String[] command) {
        printMessage(command[2]);
    }

    /**
     *Displays a message to the client
     * @param message A message
     */
    void printMessage(String message) {
        System.out.println(message);
    }

}