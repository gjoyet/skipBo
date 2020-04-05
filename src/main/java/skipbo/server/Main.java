package skipbo.server;

import skipbo.client.SBClient;

public class Main {

    public static void main(String[] args) {
        if(args[0].equalsIgnoreCase("server")) {
            SBServer sbServer = new SBServer(Integer.parseInt(args[1]));
            Thread serverT = new Thread(sbServer); serverT.start();
        } else if(args[0].equalsIgnoreCase("client")) {
            String[] arguments = {args[1], args[2]};
            SBClient sbClient = new SBClient(arguments);
            Thread sbClientT = new Thread(sbClient); sbClientT.start();
        }
    }
}
