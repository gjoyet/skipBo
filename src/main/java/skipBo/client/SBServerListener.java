package skipBo.client;

import skipBo.enums.Protocol;
import skipBo.userExceptions.NoCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Waiting for any action from server.
 */
public class SBServerListener implements Runnable {
    Socket socket;
    BufferedReader br;

    SBServerListener(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Constantly reads input from server. Input gets forwarded to executeCommand method.
     */
    @Override
    public void run() {
        String input;

        while(true) {
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
     * Acts according to network protocol input from server.
     * @param commandLine:Network protocol string
     * @throws NoCommandException:Input string doesn't match network protocol
     */
    void executeCommand(String commandLine) throws NoCommandException {
        String[] command = commandLine.split("§");

        switch (command[0]) {
            case "CHATM":
                sendMessage(command);
                break;
            case "CHNGE":
                changeTo(command);
                break;
            case "LGOUT":
                logOut(command);
                break;
            case "PRINT":
                print(command);
                break;
            default:
                throw new NoCommandException();
        }
    }

    /**
     * Sends chat message to the client.
     * @param command:String array according to network protocol with command, option and arguments.
     */
    //TODO
    void sendMessage(String[] command) {
        System.out.println(command[2]);

    }

    /**
     * Sends status message after changing name.
     * @param command:String array according to network protocol with command, option and arguments.
     */
    void changeTo(String[] command) {
        System.out.println(command[2]);
    }

    void logOut(String[] command) {
        //TODO end threads SBClientListener and SBServerListener
    }

    void print(String[] command) {
        System.out.println(command[2]);
    }

}