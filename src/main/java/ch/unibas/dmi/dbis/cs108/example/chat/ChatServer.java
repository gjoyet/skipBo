package ch.unibas.dmi.dbis.cs108.example.chat;

import java.net.*;
import java.io.*;

public class ChatServer {
    public static void main(String[] args) {
        try {
            System.out.println("Waiting for port 63001...");
            ServerSocket chatServerSocket = new ServerSocket(63001);


            for(int i=0; i < Integer.parseInt(args[0]); i++) {
                Socket chatSocket = chatServerSocket.accept();

                PrintWriter pw = new PrintWriter(chatSocket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

                pw.println("Connection successful. Please enter name: ");
                String name = br.readLine();
                System.out.println(name + " joined the room.");
                pw.println("Welcome, " + name + "!");
                ChatServerReader csReader = new ChatServerReader(br, name);
                Thread reader = new Thread(csReader);
                reader.start();
            }

        } catch(IOException ioe) {
            System.out.println(ioe.toString());
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}