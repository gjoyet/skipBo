package skipBo.server;

import skipBo.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static skipBo.server.ProtocolExecution.*;

/**
 * Thread waiting for any action from client.
 */
public class SBServerListener implements Runnable {
    Socket sock;
    PrintWriter pw;
    BufferedReader br;
    int id;
    Player player;

    SBServerListener(Socket sock, int id) {
        this.sock = sock;
        try {
            this.pw = new PrintWriter(sock.getOutputStream());
            this.br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.id = id;
        this.player = null;
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

            analyze(input, this.id, this);
        }

    }

    /**
     * First branching out for protocol execution. Triggers required method depending on input protocol command.
     * @param input: Sliced input from client.
     */
    private void analyze(String[] input, int id, SBServerListener sbL) {
        switch(input[0]) {
            case "SETTO": setTo(input, id, sbL);
                break;
            case "CHNGE": changeTo(input, id, sbL);
                break;
            case "CHATM": chatMessage(input, id, sbL);
        }

    }

}
