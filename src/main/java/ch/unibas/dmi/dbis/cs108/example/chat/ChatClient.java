import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to port 63001...");
            Socket sock = new Socket(args[0], 63001);

            Scanner scanner = new Scanner(System.in);
            String line = new String("");
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            System.out.println(br.readLine());
            pw.println(scanner.nextLine());
            System.out.println(br.readLine());

            while(true) {
                line = scanner.nextLine();
                pw.println(line);

                if(line.equalsIgnoreCase("quit")) break;
            }

            scanner.close();
            sock.close();

        } catch(IOException ioe) {
            System.out.println(ioe.toString());
        }
    } 
}