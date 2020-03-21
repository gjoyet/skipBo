package skipBo.client;

import skipBo.userExceptions.NoNameException;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client for Skip-Bo. Argument 1 is the port, argument 2 is the IP address.
 * Starts SBClientListener, handles Server input
 */
public class SBClient {
    public static void main(String[] args) {

        try {
            System.out.println("Connecting to port " + args[0] + "…");
            Socket sock = new Socket(args[1], Integer.parseInt(args[0]));
            Scanner scanner = new Scanner(System.in);

            setName(scanner);

            //Start SBClientListener Thread
            SBClientListener clientListener = new SBClientListener(sock, scanner);
            Thread listener = new Thread(clientListener);
            listener.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setName(Scanner scanner) {
        boolean nameIsValid = false;

        while(!nameIsValid) {
            System.out.println("Please enter your name: ");
            String name = scanner.nextLine();
            try {
                isNameValid(name);
                nameIsValid = true;
            } catch (NoNameException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    /*
    Name is not allowed to have more than 13 characters. Name cannot contain symbol §. Name cannot be empty.
     */
    static void isNameValid(String name) throws NoNameException {
        if (name.length() > 13) {
            throw new NoNameException("Name cannot exceed 13 characters.");
        }
        if (name.contains("§")) {
            throw new NoNameException("Name contains invalid characters.");
        }
        if (name.isEmpty()) {
            throw new NoNameException("Name cannot be empty");
        }

    }



}