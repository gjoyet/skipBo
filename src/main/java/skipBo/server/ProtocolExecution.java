package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.NameTakenException;
import skipBo.userExceptions.NoCommandException;

import java.io.IOException;

import static skipBo.server.SBServer.sbLobby;

/**
 * The execution of the network protocol is implemented in this class. Every command has one method,
 * with further branching inside each method according to the options.
 */
public class ProtocolExecution {

    /**
     * Method for command "SETTO".
     */
    static void setTo(String[] input, SBListener sbL) throws NoCommandException {
        String name = "NoName";
        try {
            if (input[1].equals("Nickname")) {
                if(!SBServer.sbLobby.nameIsValid(input[2])) {
                    sbL.pw.println("PRINT§Terminal§Invalid name. Name set to system username.");
                    if(!SBServer.sbLobby.nameIsTaken(System.getProperty("user.name"))) {
                        sbL.player = new Player(sbL.id, name = System.getProperty("user.name"), sbL);
                        SBServer.sbLobby.addPlayer(sbL.player);
                    } else throw new NameTakenException(System.getProperty("user.name"), sbL);
                } else {
                    name = input[2];
                    if (!SBServer.sbLobby.nameIsTaken(name) && SBServer.sbLobby.nameIsValid(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        SBServer.sbLobby.addPlayer(sbL.player);
                    } else if(SBServer.sbLobby.nameIsTaken(name)) {
                        throw new NameTakenException(name, sbL);
                    }
                }
            } else throw new NoCommandException(input[0], input[1]);
        } catch(NameTakenException nte) {
            name = nte.findName();
            sbL.player = new Player(sbL.id, name, sbL);
            SBServer.sbLobby.addPlayer(sbL.player);
        } finally {
            System.out.println("Welcome to Skip-Bo, " + name + "!");
            sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
            sendAllExceptOne("PRINT§Terminal§" + name + " joined the room. Say hi!", sbL);
        }
    }

    /**
     * Method for command "CHNGE".
     */
    static void changeTo(String[] input, SBListener sbL) throws NoCommandException {
        String formerName = sbL.player.getName();
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if (!SBServer.sbLobby.nameIsTaken(name) && SBServer.sbLobby.nameIsValid(name)) {
                    sbL.player.changeName(name);
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    System.out.println(formerName + " changed name to " + name + ".");
                    sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
                } else if (!SBServer.sbLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                } else if (SBServer.sbLobby.nameIsTaken(name)) {
                    throw new NameTakenException(name, sbL);
                }
            } else throw new NoCommandException(input[0], input[1]);
        } catch (NameTakenException nte) {
            sbL.player.changeName(nte.findName());
        }
    }

    /**
     * Method for command "CHATM".
     */
    static void chatMessage(String[] input, SBListener sbL) throws NoCommandException {
        try {
            if (input.length < 3) return;
            System.out.println("Received chat message: " + input[2]);
            if (input[1].equals("Global")) {
                String message = input[2];
                sendAllExceptOne("CHATM§Global§" + sbL.player.getName() + ": " + message, sbL);
            } else throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    /**
     * Method for command "LGOUT".
     */
    static void logout(String[] input, SBListener sbL) {
        sbL.pw.println("LGOUT");
        sbLobby.removePlayer(sbL.player);
        sbL.stopRunning();
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.");
        System.out.println("Bye Bye, " + sbL.player.getName() + ".");

    }

    /**
     * @param message: String sent to all clients
     */
    static void sendAll(String message) {
        for(int i = 0; i < sbLobby.getLength(); i++) {
            sbLobby.getSBL(i).pw.println(message);
        }
    }

    /**
     * @param message: String sent to all clients...
     * @param sbL: ... except this one
     */
    static void sendAllExceptOne(String message, SBListener sbL) {
        for(int i = 0; i < sbLobby.getLength(); i++) {
            if(!sbLobby.getSBL(i).equals(sbL)) {
                sbLobby.getSBL(i).pw.println(message);
            }
        }
    }

}
