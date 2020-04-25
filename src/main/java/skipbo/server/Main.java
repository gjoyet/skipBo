package skipbo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import skipbo.client.SBClient;

public class Main {

    static Logger mainLog = LogManager.getLogger(Main.class);

    public static SBServer server;
    public static SBClient client;

    public static void main(String[] args) {
        mainLog.debug("Program starting.");
        if (args[0].equalsIgnoreCase("server")) {
            mainLog.debug("Main triggered Server.");
            SBServer sbServer = new SBServer(Integer.parseInt(args[1]));
            Thread serverT = new Thread(sbServer);
            serverT.start();
            server = sbServer;
        } else if (args[0].equalsIgnoreCase("client") || args[0].equalsIgnoreCase("testClient")) {
            mainLog.debug("Main triggered Client.");
            client = new SBClient(args);
        }
    }
}
