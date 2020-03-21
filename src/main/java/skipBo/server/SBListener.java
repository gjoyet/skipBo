package skipBo.server;

import skipBo.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static skipBo.server.SBLobby.nameIsTaken;

/**
 * Thread waiting for any action from client.
 */
public class SBListener implements Runnable {
    private Socket sock;
    private PrintWriter pw;
    private BufferedReader br;
    private int id;

    SBListener(Socket sock, int id) {
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

            analyze(input, this.id);
        }

    }

    /**
     * Acts according to network protocol input from client.
     * @param input: Slices input from client.
     */
    private void analyze(String[] input, int id) {
        switch(input[0]) {
            case "SETTO": setTo(input, id);
                break;
            case "CHNGE":
        }

    }

    /**
     * Method for command "SETTO".
     */
    private void setTo(String[] input, int id) {
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];

                if (!skipBo.server.SBLobby.nameIsTaken(name)) {
                    SBLobby.addPlayer(new Player(id, name));
                } else {
                    int i = 1;
                    String nameWithNumber = name + i;
                    while (skipBo.server.SBLobby.nameIsTaken(nameWithNumber)) {
                        i++;
                        nameWithNumber = name + i;
                    }
                    SBLobby.addPlayer(new Player(id, nameWithNumber));
                }
            } else throw new main.java.skipBo.userExceptions.NoCommandException();
        } catch (main.java.skipBo.userExceptions.NoCommandException nce) {
            System.out.println(input[1] + ": no option for SETTO command.");
        }
    }



}
