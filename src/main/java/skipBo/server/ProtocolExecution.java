package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.NameTakenException;
import skipBo.userExceptions.NoCommandException;

import static skipBo.server.SBLobby.nameIsTaken;
import static skipBo.server.SBLobby.nameIsValid;
import static skipBo.server.SBServer.allListeners;

public class ProtocolExecution {

    /**
     * Method for command "SETTO".
     */
    static void setTo(String[] input, int id, SBListener sbL) {
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if (!nameIsTaken(name) && nameIsValid(name)) {
                    sbL.player = new Player(id, name, sbL);
                    SBLobby.addPlayer(sbL.player);
                } else if(nameIsTaken(name)) {
                    throw new NameTakenException(name);
                } else if(!nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Name set to system username.");
                    sbL.player = new Player(id, System.getProperty("user.name"), sbL);
                    SBLobby.addPlayer(sbL.player);
                }
            } else throw new NoCommandException();
        } catch(NoCommandException nce) {
            System.out.println(input[1] + ": no option for SETTO command.");
        } catch(NameTakenException nte) {
            sbL.player = new Player(id, nte.findName(), sbL);
            SBLobby.addPlayer(sbL.player);
        }
    }

    /**
     * Method for command "CHNGE".
     */
    static void changeTo(String[] input, int id, SBListener sbL) {
        try {
            if(input[1].equals("Nickname")) {
                String name = input[2];
                if(!nameIsTaken(name) && nameIsValid(name)) {
                    sbL.player.name = name;
                } else if(!nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                } else if(nameIsTaken(name)) {
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
            if(input[1].equals("Global")) {
                String message = input[2];
                for(SBListener el : allListeners) {
                    if(!el.equals(sbL)) {
                        el.pw.println("CHATM§Global§" + sbL.player.name + ": " + message);
                    }
                }
            } else throw new NoCommandException();
        } catch (NoCommandException nce) {
            System.out.println(input[1] + ": no option for CHNGE command.");
        }
    }

}
