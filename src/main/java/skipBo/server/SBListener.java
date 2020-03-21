package skipBo.server;

import skipBo.enums.Protocol;
import skipBo.userExceptions.NoNameException;
import skipBo.userExceptions.NoProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Thread waiting for any action from client.
 */
public class SBListener implements Runnable {
    private Socket sock;
    private PrintWriter pw;
    private BufferedReader br;
    private int id;

    public SBListener(Socket sock, int id) {
        this.sock = sock;
        try {
            this.pw = new PrintWriter(sock.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.id = id;
    }

    /**
     * Constantly reads input from assigned client. Read input is sliced and then passes it as argument to analyze method.
     */
     public void run() {
        while(true) {
            String[] input = null;
            try {
                input = this.br.readLine().split("§");
            } catch (IOException e) {
                System.out.println("Error with reading input from client.");
            }

            analyze(input);
        }

    }

    /**
     * Acts according to network protocol input from client.
     * @param input: Slices input from client.
     */
    void analyze(String[] input) {
        switch(input[0]) {
        case "SETTO": if(input[1].equals("Nickname")) {

        }
    }



}
