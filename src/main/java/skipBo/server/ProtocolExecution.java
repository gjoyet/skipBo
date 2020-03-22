package skipBo.server;

import skipBo.game.Player;
import skipBo.userExceptions.NameTakenException;
import skipBo.userExceptions.NoCommandException;

import java.io.IOException;

import static skipBo.server.SBServer.sbLobby;

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
                    sbL.player = new Player(sbL.id, name = System.getProperty("user.name"), sbL);
                    SBServer.sbLobby.addPlayer(sbL.player);
                } else {
                    name = input[2];
                    if (!SBServer.sbLobby.nameIsTaken(name) && SBServer.sbLobby.nameIsValid(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        SBServer.sbLobby.addPlayer(sbL.player);
                    } else if(SBServer.sbLobby.nameIsTaken(name)) {
                        throw new NameTakenException(name);
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
        }
    }

    /**
     * Method for command "CHNGE".
     */
    static void changeTo(String[] input, SBListener sbL) throws NoCommandException {
        try {
            if (input.length < 3) return;
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if (!SBServer.sbLobby.nameIsTaken(name) && SBServer.sbLobby.nameIsValid(name)) {
                    sbL.player.changeName(name);
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    System.out.println("Name changed to " + name + ".");
                } else if (!SBServer.sbLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Name contains invalid symbols. Try again.");
                } else if (SBServer.sbLobby.nameIsTaken(name)) {
                    throw new NameTakenException(name);
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
                sendAll("CHATM§Global§" + sbL.player.getName() + ": " + message);
            } else throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    /**
     * Method for command "LGOUT".
     */
    static void logout(String[] input, SBListener sbL) {
        sbL.pw.println("LGOUT");
        sbLobby.removePlayer(sbL.player);
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.");

    }

    static void sendAll(String message) {
        for(int i = 0; i < sbLobby.getLength(); i++) {
            sbLobby.getSBL(i).pw.println(message);
        }
    }

    static void sendAllExcept(String message, SBListener sbL) {}

}
