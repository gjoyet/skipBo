package skipbo.server;

import skipbo.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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


    SBListener(Socket sock, int id) {
        this.sock = sock;
        try {
            this.pw = new PrintWriter(sock.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            System.out.println("Issue with getting Input- and OutputStream.");
        }
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
                System.out.println("Error with reading input from client.");
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
                    //System.out.println("LOG: Got into setTo method."); (For testing purposes)
                    break;
                case CHNGE:
                    new ProtocolExecutor(input, this).changeTo();
                    //System.out.println("LOG: Got into changeTo method.");
                    break;
                case CHATM:
                    new ProtocolExecutor(input, this).chatMessage();
                    //System.out.println("LOG: Got into chatMessage method.");
                    break;
                case LGOUT:
                    new ProtocolExecutor(input, this).logout();
                    //System.out.println("LOG: Got into logout method.");
                    break;
                case NWGME:
                    new ProtocolExecutor(input, this).newGame();
                    break;
            }
        } catch(IllegalArgumentException iae) {
            System.out.println(input[0] + ": not a command.");
        } catch(NoCommandException nce) {
            if(nce.command != null && nce.option != null) {
                System.out.println(nce.option + ": not an option for command " + nce.command + ".");
            } else {
                System.out.println("No valid protocol.");
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

}


