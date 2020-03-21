package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.*;

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
    private Player player;

    SBListener(Socket sock, int id) {
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

            this.analyze(input, this.id);
        }

    }

    /**
     * Acts according to network protocol input from client.
     * @param input: Slices input from client.
     */
    private void analyze(String[] input, int id) {
        switch(input[0]) {
            case "SETTO": setTo(input, id, this);
                break;
            case "CHNGE": changeTo(input, id, this);
                break;
        }

    }

    /**
     * Method for command "SETTO".
     */
    private void setTo(String[] input, int id, SBListener sbL) {
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if (!nameIsTaken(name) && nameIsValid(name)) {
                    this.player = new Player(id, name, sbL);
                    SBLobby.addPlayer(this.player);
                } else if(nameIsTaken(name)) {
                    throw new NameTakenException(name);
                } else if(!nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                }
            } else throw new NoCommandException();
        } catch(NoCommandException nce) {
            System.out.println(input[1] + ": no option for SETTO command.");
        } catch(NameTakenException nte) {
            sbL.player = new Player(id, nte.findName(), sbL);
            SBLobby.addPlayer(this.player);
        }
    }

    /**
     * Method for command "CHNGE".
     */
    private void changeTo(String[] input, int id, SBListener sbL) {
        try {
            if(input[1].equals("Nickname")) {
                String name = input[2];
                if(!nameIsTaken(name) && nameIsValid(name)) {
                    sbL.player.name = name;
                } else if(!nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                } else if(nameIsTaken(name)) {
                    throw new NameTakenException(name);
                }
            } else throw new NoCommandException();
        } catch (NoCommandException nce) {
            System.out.println(input[1] + ": no option for CHNGE command.");
        } catch (NameTakenException nte) {
            sbL.player.name = nte.findName();
        }
    }

    /**
     * Checks if name is valid.
     */
    private static boolean nameIsValid(String name) {
        for(int i=0; i < name.length(); i++) {
            if(!Character.isLetterOrDigit(name.charAt(i))) return false;
        }
        if(name.length() > 13) return false;

        return true;
    }



}
