import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;
import java.net.Socket;
import java.io.IOException;

public class DateClient {
    public static void main(String[]args) throws IOException{
        if (args.length != 1){
            System.err.println("Server address isn't correct");
            return;
        }
        int port = 59090;
        var socket = new Socket("", port);
        var in = new Scanner(socket.getInputStream());
        System.out.println("Server response: " + in.nextLine());
    }
}
