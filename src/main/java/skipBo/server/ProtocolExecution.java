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
     * Method for command "SETTO". This command sets a parameter
     * given as option to the value given as argument.
     */
    static void setTo(String[] input, SBListener sbL) throws NoCommandException {
        String name = "SBPlayer";
        try {
            if (input[1].equals("Nickname")) {
                if(input.length == 2 || !SBServer.sbLobby.nameIsValid(input[2])) {
                    sbL.pw.println("PRINT§Terminal§Invalid name. Name set to " + name + ".");
                    if(!SBServer.sbLobby.nameIsTaken(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        SBServer.sbLobby.addPlayer(sbL.player);
                    } else throw new NameTakenException(name, sbL);
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
            System.out.println(name + " logged in.");
            sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
            sendAllExceptOne("PRINT§Terminal§" + name + " joined the room. Say hi!", sbL);
        }
    }

    /**
     * Method for command "CHNGE". This command sets a parameter
     * given as option to the value given as argument.
     */
    static void changeTo(String[] input, SBListener sbL) throws NoCommandException {
        String formerName = sbL.player.getName();
        if(input.length < 3) return;
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if(name.equals(sbL.player.getName())) {
                    sbL.pw.println("PRINT§Terminal§Name is already " + name + ".");
                } else if (!SBServer.sbLobby.nameIsTaken(name) && SBServer.sbLobby.nameIsValid(name)) {
                    sbL.player.changeName(name);
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    System.out.println(formerName + " changed name to " + name + ".");
                    sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
                } else if (!SBServer.sbLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Invalid name. Try again.");
                } else if (SBServer.sbLobby.nameIsTaken(name)) {
                    throw new NameTakenException(name, sbL);
                }
            } else throw new NoCommandException(input[0], input[1]);
        } catch (NameTakenException nte) {
            String name = nte.findName();
            sbL.player.changeName(name);
            sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
        }
    }

    /**
     * Method for command "CHATM". Sends received chat message to all
     * players except the one sending it.
     */
    static void chatMessage(String[] input, SBListener sbL) throws NoCommandException {
        try {
            if (input.length < 3) return;
            System.out.println("Received chat message from " + sbL.player.getName() + ": " + input[2]);
            if (input[1].equals("Global")) {
                String message = input[2];
                sendAllExceptOne("CHATM§Global§" + sbL.player.getName() + ": " + message, sbL);
            } else throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    /**
     * Method for command "LGOUT". Terminates the SBListener of the player logging out,
     * closes socket and sends message to all other players on which player logged out.
     */
    static void logout(SBListener sbL) {
        sbL.pw.println("LGOUT");
        sbLobby.removePlayer(sbL.player);
        sbL.stopRunning();
        try {
            sbL.pw.close();
            sbL.br.close();
            sbL.sock.close();
        } catch(IOException ioe) {
            System.out.println("Issues while closing the socket at logout.");
        }
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.");
        System.out.println(sbL.player.getName() + " logged out.");

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
