package skipboTest.serverTest;

import org.junit.Before;
import skipbo.server.NWPListener;
import skipbo.server.NoCommandException;
import skipbo.server.Protocol;
import skipbo.server.ProtocolExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static skipbo.server.SBServer.servLog;

public class SBListenerTest {

    public class testingSBL implements NWPListener {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] input;
        String trigger;
        boolean running;

        public testingSBL() {
            running = true;
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
                        trigger = "SETTO";
                        break;
                    case CHNGE:
                        trigger = "CHNGE";
                        break;
                    case CHATM:
                        trigger = "CHATM";
                        break;
                    case LGOUT:
                        trigger = "LGOUT";
                        break;
                    case NWGME:
                        trigger = "NWGME";
                        break;
                    case PUTTO:
                        trigger = "PUTTO";
                        break;
                    case DISPL:
                        trigger = "DISPL";
                        break;
                    case PLAYR:
                        trigger = "PLAYR";
                        break;
                }
            } catch(IllegalArgumentException iae) {
                servLog.warn(input[0] + ": not a command.");
            }
        }

        @Override
        public void stopRunning() {
            this.running = false;
        }
    }

    PrintWriter pw = new PrintWriter(System.out);

    @Before
    public void initialize() {

    }
}
