package skipbo.server;

import skipbo.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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


    SBListener(Socket sock, int id) throws IOException {
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

            this.analyze(input);
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
                    servLog.debug("Got into setTo method.");
                    break;
                case CHNGE:
                    new ProtocolExecutor(input, this).changeTo();
                    servLog.debug("Got into changeTo method.");
                    break;
                case CHATM:
                    new ProtocolExecutor(input, this).chatMessage();
                    servLog.debug("Got into chatMessage method.");
                    break;
                case LGOUT:
                    new ProtocolExecutor(input, this).logout();
                    servLog.debug("Got into logout method.");
                    break;
                case NWGME:
                    servLog.debug("Got into newGame method.");
                    new ProtocolExecutor(input, this).newGame();
                    break;
                case PUTTO:
                    servLog.debug("Got into putTo method with input: " + input[2] + ".");
                    new ProtocolExecutor(input, this).putTo();
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

    public ArrayList<Player> getGameLobby() { return this.player.getGame().players; }

}


