import java.net.*;
import java.util.Scanner;
import java.io.*;

public class OneWayChatClient {
    public static void main(String[] args) {
        try  {
            Socket sock = new Socket(args[0], 63000);
            Scanner scanner = new Scanner(System.in);
            OutputStream outStream = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(outStream, true);

            String line = new String("");

            while(true) {
                line = scanner.nextLine();
                writer.println(line);
                if(line.equalsIgnoreCase("quit")) break;
            }

            writer.close();
            outStream.close();
            scanner.close();
            sock.close();
        
        } catch(Exception e) {
            System.out.println(e);
        } 
           
    }
    
}