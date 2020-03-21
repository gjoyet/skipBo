package skipBo.chat_obsolete;

import java.net.*;
import java.io.*;

/**
 * Chat server reading messages and printing them on console. This simple server with restricted maximum user number
 * takes a name from the any newly connected client and then starts a thread that reads messages from the client,
 * then prints them on its own terminal.
 */
public class ChatServer {
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for port 63001...");
            ServerSocket chatServerSocket = new ServerSocket(63001);


            for(int i=0; i < Integer.parseInt(args[0]); i++) {
                Socket chatSocket = chatServerSocket.accept(); // Accepting connection, the establishing communication

                PrintWriter pw = new PrintWriter(chatSocket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

                pw.println("Connection successful. Please enter name: "); // Taking name
                String name = br.readLine();
                System.out.println(name + " joined the room.");
                pw.println("Welcome, " + name + "!");
                ChatServerReader csReader = new ChatServerReader(br, name); // Starting a reader thread that expects messages from client
                Thread reader = new Thread(csReader);
                reader.start();
            }

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
