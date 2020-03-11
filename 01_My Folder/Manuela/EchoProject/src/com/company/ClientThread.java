package com.company;

import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {
    private int name;
    private Socket socket;

    public ClientThread(int name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public void run() {
        String msg = "[Server] Verbindung " + name;
        System.out.println(msg + " hergestellt");

        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write((msg + "\r\n").getBytes());
            int c;
            while ((c = in.read()) != -1) {
                out.write((char)c);
                System.out.print((char)c);
            }
            System.out.println(msg + " beendet");
            socket.close();

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
