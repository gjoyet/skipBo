import java.net.*;
import java.util.Scanner;
import java.io.*;

public class OneWayChatServer {

    public static void main(String[] args) {
        try {
            System.out.println("Warte auf Port 63000...");
            ServerSocket serverSocket = new ServerSocket(63000);
            Socket chat = serverSocket.accept();
            System.out.println("Verbindung erfolgreich hergestellt.");

            InputStream inStream = chat.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

            String line = new String("");

            while(true) {
                line = reader.readLine();
                System.out.println(line);
                if(line.equalsIgnoreCase("quit")) break;
            }

            reader.close();
            inStream.close();
            chat.close();
            serverSocket.close();
            
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}