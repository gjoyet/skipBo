package ch.unibas.chat;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatServerReader implements Runnable {
    BufferedReader chatBR;
    String name;

    public ChatServerReader(BufferedReader br, String name) {
        this.chatBR = br;
        this.name = name;
    }
    
    public void run() {
        String line;
        
         try {
             while (true) {
                 line = this.chatBR.readLine();
                 System.out.println(this.name + ": " + line);

                 if (line.equalsIgnoreCase("quit")) break;
             }

         } catch(Exception e) {
             System.out.println(e.toString());
         }
    }
}