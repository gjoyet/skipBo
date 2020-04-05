package skipbo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import skipbo.client.SBClient;

public class Main {

    static Logger mainLog = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        mainLog.debug("Program starting.");
        if(args[0].equalsIgnoreCase("server")) {
            mainLog.debug("Main triggered Server.");
            SBServer sbServer = new SBServer(Integer.parseInt(args[1]));
            Thread serverT = new Thread(sbServer); serverT.start();
        } else if(args[0].equalsIgnoreCase("client")) {
            mainLog.debug("Main triggered Client.");
            String[] arguments = new String[args.length-1];
            for(int i=1; i < args.length; i++) {
                arguments[i-1] = args[i];
            }
            SBClient sbClient = new SBClient(arguments);
            Thread sbClientT = new Thread(sbClient); sbClientT.start();
        }
    }
}
