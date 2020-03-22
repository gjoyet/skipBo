package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.NoCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;

import static skipBo.server.ProtocolExecution.*;

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
            System.out.printf("Issue with getting Input- and OutputStream.");
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
                input = this.br.readLine().split("§");
            } catch (IOException e) {
                System.out.println("Error with reading input from client.");
            }

            analyze(input, this);
        }

    }

    /**
     * First branching out for protocol execution. Triggers required method depending on input protocol command.
     * @param input: Sliced input String from client.
     */
    private static void analyze(String[] input, SBListener sbL) {
        try {
            switch (input[0]) {
                case "SETTO":
                    setTo(input, sbL);
                    //System.out.println("LOG: Got into setTo method.");
                    break;
                case "CHNGE":
                    changeTo(input, sbL);
                    //System.out.println("LOG: Got into changeTo method.");
                    break;
                case "CHATM":
                    chatMessage(input, sbL);
                    //System.out.println("LOG: Got into chatMessage method.");
                    break;
                case "LGOUT":
                    logout(input, sbL);
                    //System.out.println("LOG: Got into logout method.");
            }
        } catch(NoCommandException nce) {
            System.out.println(nce.option + ": not an option for command " + nce.command + ".");
        }
    }

    public PrintWriter getPW() {
        return this.pw;
    }

    public void stopRunning() {
        this.running = false;
    }

                /*
                   TODO: Handle logout, players which have not given name yet don't get messages until
                    they have given a name, message to all when someone logs in, make name change possible (Manuela
                    has 'name' as option, I have 'nickname'), comment protocol enums, Guillaume should use protocol enums
                    and not just "CHATM"
                 */

}


