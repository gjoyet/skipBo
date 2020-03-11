package com.company;

import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) {
        int clients = 0;
        try {
            System.out.println("Wartet auf Port 63241…");
            ServerSocket ssocket = new ServerSocket(63241);
            while (true) {
                Socket socket = ssocket.accept();
                ClientThread cT = new ClientThread(++clients, socket);
                Thread cThread = new Thread(cT);
                cThread.start();
            }

        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
}
