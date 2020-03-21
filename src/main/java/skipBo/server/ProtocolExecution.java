package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.NameTakenException;
import skipBo.userExceptions.NoCommandException;

import static skipBo.server.SBServer.allListeners;

public class ProtocolExecution {

    /**
     * Method for command "SETTO".
     */
    static void setTo(String[] input, int id, SBListener sbL) {
        try {
            if (input[1].equals("Nickname")) {
                if(input.length < 3 || !SBServer.lobby.nameIsValid(input[2])) {
                    sbL.pw.println("PRINT§Terminal§Invalid name. Name set to system username.");
                    sbL.player = new Player(id, System.getProperty("user.name"), sbL);
                    SBServer.lobby.addPlayer(sbL.player);
                } else {
                    String name = input[2];
                    if (!SBServer.lobby.nameIsTaken(name) && SBServer.lobby.nameIsValid(name)) {
                        sbL.player = new Player(id, name, sbL);
                        SBServer.lobby.addPlayer(sbL.player);
                        sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
                        System.out.println("Welcome to Skip-Bo, " + name + "!");
                    } else if(SBServer.lobby.nameIsTaken(name)) {
                        throw new NameTakenException(name);
                    }
                }
            } else throw new NoCommandException();
        } catch(NoCommandException nce) {
            System.out.println(input[1] + ": no option for SETTO command.");
        } catch(NameTakenException nte) {
            String name = nte.findName();
            sbL.player = new Player(id, name, sbL);
            SBServer.lobby.addPlayer(sbL.player);
            sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
            System.out.println("Welcome to Skip-Bo, " + name + "!");
        }
    }

    /**
     * Method for command "CHNGE".
     */
    static void changeTo(String[] input, int id, SBListener sbL) {
        try {
            if(input.length < 3) return;
            if(input[1].equals("Nickname")) {
                String name = input[2];
                if(!SBServer.lobby.nameIsTaken(name) && SBServer.lobby.nameIsValid(name)) {
                    sbL.player.name = name;
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    System.out.println("Name changed to " + name +".");
                } else if(!SBServer.lobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                } else if(SBServer.lobby.nameIsTaken(name)) {
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
     * Method for command "CHATM".
     */
    static void chatMessage(String[] input, int id, SBListener sbL) {
        try {
            if(input.length < 3) return;
            System.out.println("Received chat message: " + input[2]);
            if(input[1].equals("Global")) {
                String message = input[2];
                for(SBListener el : SBServer.allListeners) {
                    el.pw.println("CHATM§Global§" + sbL.player.name + ": " + message);
                }
            } else throw new NoCommandException();
        } catch (NoCommandException nce) {
            System.out.println(input[1] + ": no option for CHNGE command.");
        }
    }

    /**
     * Method for command "LGOUT".
     */
    static void logout(String[] input, int id, SBListener sbL) {
        // please implement
    }

}
