import java.io.*;
import java.util.*;
import java.net.*;


public class DateServer {
    private static volatile boolean status = true;
    public static void main(String [] args) throws IOException{
        int port = 59090;
        try(ServerSocket reader = new ServerSocket(port)){
            System.out.println("Server is running now...");
            while (status==true){
                try (var socket = reader.accept()){
                    var out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                }catch(Exception e){
                    e.printStackTrace();
                    status = false;
                }
            }
        }
    }
}
