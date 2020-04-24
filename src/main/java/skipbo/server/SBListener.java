package skipbo.server;

import skipbo.game.Player;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static skipbo.server.SBServer.servLog;

/**
 * Thread waiting for any action from client.
 */
public class SBListener implements Runnable {
    Socket sock;
    PrintWriter pw;
    BufferedReader br;
    boolean running;
    int id;
    Player player;


    public SBListener(Socket sock, int id) throws IOException {
        this.sock = sock;
        try {
            this.pw = new PrintWriter(sock.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } finally {}
        this.running = true;
        this.id = id;
        this.player = null;
    }

    /**
     * Constantly reads input from assigned client. Read input is sliced and then passes it as argument to analyze method.
     */
     public void run() {
        while(running) {
            String[] input = null;
            try {
                input = this.br.readLine().split("§", 3);
            } catch (IOException e) {
                servLog.warn("Error with reading input from client.");
            }

            if(input != null) this.analyze(input);
        }

    }

    /**
     * First branching out for protocol execution. Triggers required method depending on input protocol command.
     * @param input: Sliced input String from client.
     */
    private void analyze(String[] input) {
        Protocol protocol = Protocol.valueOf(input[0]);
        try {
            switch (protocol) {
                case SETTO:
                    new ProtocolExecutor(input, this).setTo();
                    break;
                case CHNGE:
                    new ProtocolExecutor(input, this).changeTo();
                    break;
                case CHATM:
                    new ProtocolExecutor(input, this).chatMessage();
                    break;
                case LGOUT:
                    new ProtocolExecutor(input, this).logout();
                    break;
                case NWGME:
                    new ProtocolExecutor(input, this).newGame();
                    break;
                case PUTTO:
                    new ProtocolExecutor(input, this).putTo();
                    break;
                case DISPL:
                    new ProtocolExecutor(input, this).display();
                    break;
            }
        } catch(IllegalArgumentException iae) {
            servLog.warn(input[0] + ": not a command.");
        } catch(NoCommandException nce) {
            if(nce.command != null && nce.option != null) {
                servLog.warn(nce.option + ": not an option for command " + nce.command + ".");
            } else {
                servLog.warn("No valid protocol.");
            }
        }
    }

    /**
     * Sets running to false, thus getting the SBListener out of the while loop and terminating the thread.
     */
    public void stopRunning() {
        this.running = false;
    }

    public PrintWriter getPW() {
        return this.pw;
    }

    public ArrayList<Player> getGameLobby() {
        if(this.player.getGame() != null) return this.player.getGame().players;
        else return null;
    }

    public Player getPlayer() { return this.player; }

}


