package ch.unibas.chat;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * Chat client sending messages to Server. This simple client scans input from keyboard
 * and sends it to the server. Client is terminated when user writes "quit".
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to port 63001...");
            Socket sock = new Socket(args[0], 63001);

            Scanner scanner = new Scanner(System.in); // Establishing communication
            String line;
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            System.out.println(br.readLine()); // Giving name
            pw.println(scanner.nextLine());
            System.out.println(br.readLine());

            // Forwards messages to Server until user writes "quit"

            while(true) {
                line = scanner.nextLine();
                pw.println(line);

                if(line.equalsIgnoreCase("quit")) break;
            }

            scanner.close();
            sock.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    } 
}