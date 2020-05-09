package skipboTest.serverTest;

import org.junit.Before;
import org.junit.Test;
import skipbo.server.NWPListener;
import skipbo.server.NoCommandException;
import skipbo.server.Protocol;
import skipbo.server.ProtocolExecutor;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static skipbo.server.SBServer.servLog;

public class SBListenerTest {

    public class testingSBL implements NWPListener {

        BufferedReader br;
        String[] input;
        String trigger;
        boolean running;

        public testingSBL(PipedInputStream pis) {
            this.br = new BufferedReader(new InputStreamReader(pis));
            this.running = true;
            Thread testingSBLT = new Thread(this); testingSBLT.start();
        }

        @Override
        public void run() {
            while(running) {
                input = null;
                try {
                    input = this.br.readLine().split("§", 3);
                } catch (IOException e) {
                    servLog.warn("Error with reading input.");
                }

                if(input != null) this.analyze(input);
            }
        }

        @Override
        public void analyze(String[] input) {
            Protocol protocol = Protocol.valueOf(input[0]);
            try {
                switch (protocol) {
                    case SETTO:
                        trigger = "setTo()";
                        break;
                    case CHNGE:
                        trigger = "changeTo()";
                        break;
                    case CHATM:
                        trigger = "chatMessage()";
                        break;
                    case LGOUT:
                        trigger = "logout()";
                        break;
                    case NWGME:
                        trigger = "newGame()";
                        break;
                    case PUTTO:
                        trigger = "putTo()";
                        break;
                    case DISPL:
                        trigger = "display()";
                        break;
                    case PLAYR:
                        trigger = "playerLeavingGame()";
                        break;
                }
            } catch(IllegalArgumentException iae) {
                trigger = input[0] + ": not a command.";
            }
        }

        @Override
        public void stopRunning() {
            this.running = false;
        }
    }

    PipedOutputStream pos;
    PipedInputStream pis;
    PrintWriter pw;
    testingSBL tSBL;

    @Before
    public void initialize() {
        pos = new PipedOutputStream();
        pis = new PipedInputStream();
        pw = new PrintWriter(pos);

        try {
            pis.connect(pos);
        } catch (IOException e) {
            servLog.error("Problem with connecting pis and pos.");
        }

        tSBL = new testingSBL(pis);
    }

    /**
     * Tests what happens in the case of a normal command.
     */
    @Test
    public void testNormalCommand() {
        pw.println("SETTO§Nickname§Marc");
        assertEquals(tSBL.trigger, ("setTo()"));
        pw.println("CHNGE§Status§READY");
        assertEquals(tSBL.trigger, ("changeTo()"));
        pw.println("CHATM§Global§Hi");
        assertEquals(tSBL.trigger, ("chatMessage()"));
        pw.println("NWGME§New§2§20");
        assertEquals(tSBL.trigger, ("newGame()"));
        pw.println("DISPL§players");
        assertEquals(tSBL.trigger, ("display()"));
    }
}
