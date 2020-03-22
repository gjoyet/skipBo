package skipBo.client;

import skipBo.userExceptions.NoCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Thread waiting for any input from server and executes the input on client
 */
public class SBServerListener implements Runnable {
    Socket socket;
    BufferedReader br;
    Boolean isLoggedIn = true;

    SBServerListener(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            } catch (NoCommandException e) {
                System.out.println("Not a command");
            }

        }
    }

    /**
     * Executes commands coming from the server according to network protocol
     * @param commandLine Network protocol string from server
     * @throws NoCommandException If commandLine string doesn't match network protocol
     */
    void executeCommand(String commandLine) throws NoCommandException {
        String[] command = commandLine.split("§");

        switch (command[0]) {
            case "CHATM":
                sendChatMessage(command);
                break;
            case "CHNGE":
                changeTo(command);
                break;
            case "LGOUT":
                logOut();
                break;
            case "PRINT":
                print(command);
                break;
            default:
                throw new NoCommandException();
        }
    }

    /**
     * Sends a chat message to the client.
     * @param command String array according to network protocol with command, option and arguments.
     */
    void sendChatMessage(String[] command) {
        System.out.println(command[2]);
    }

    /**
     * Sends status message after changing name.
     * @param command:String array according to network protocol with command, option and arguments.
     */
    void changeTo(String[] command) {
        System.out.println(command[2]);
    }

    void logOut() {
        isLoggedIn = false;
        System.out.println("Logout successful");
    }

    void print(String[] command) {
        System.out.println(command[2]);
    }

    void printMessage(String message) {
        System.out.println(message);
    }

}