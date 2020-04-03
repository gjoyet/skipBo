package skipbo.server;

import skipbo.game.Game;
import skipbo.game.Pile;
import skipbo.game.Player;
import skipbo.game.Status;

import java.io.IOException;
import java.util.ArrayList;

import static skipbo.server.SBServer.serverLobby;

/**
 * The execution of the network protocol is implemented in this class. Every command has one method,
 * with further branching inside each method according to the options.
 */
public class ProtocolExecutor {

    private String[] input;
    private SBListener sbL;

    public ProtocolExecutor() { }

    ProtocolExecutor(String[] input, SBListener sbL) {
        this.input = input;
        this.sbL = sbL;
    }

    /**
     * Method for command "SETTO". This command sets a parameter
     * given as option to the value given as argument.
     */
    void setTo() throws NoCommandException {
        String name = "SBPlayer";
        try {
            if (input[1].equals("Nickname")) {
                if(input.length == 2 || !SBServer.serverLobby.nameIsValid(input[2])) {
                    sbL.pw.println("PRINT§Terminal§Invalid name. Name set to " + name + ".");
                    if(!SBServer.serverLobby.nameIsTaken(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        SBServer.serverLobby.addPlayer(sbL.player);
                    } else throw new NameTakenException(name, sbL);
                } else {
                    name = input[2];
                    if (!SBServer.serverLobby.nameIsTaken(name) && SBServer.serverLobby.nameIsValid(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        SBServer.serverLobby.addPlayer(sbL.player);
                    } else if(SBServer.serverLobby.nameIsTaken(name)) {
                        throw new NameTakenException(name, sbL);
                    }
                }
            } else throw new NoCommandException(input[0], input[1]);
        } catch(NameTakenException nte) {
            name = nte.findName();
            sbL.player = new Player(sbL.id, name, sbL);
            SBServer.serverLobby.addPlayer(sbL.player);
        } finally {
            // System.out.println(name + " logged in.");
            sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
            sendAllExceptOne("PRINT§Terminal§" + name + " joined the room. Say hi!", sbL);
        }
    }

    /**
     * Method for command "CHNGE". This command sets a parameter
     * given as option to the value given as argument.
     */
    void changeTo() throws NoCommandException {
        String formerName = sbL.player.getName();
        if(input.length < 3) return;
        try {
            if (input[1].equals("Nickname")) {
                String name = input[2];
                if(name.equals(sbL.player.getName())) {
                    sbL.pw.println("PRINT§Terminal§Name is already " + name + ".");
                } else if (!SBServer.serverLobby.nameIsTaken(name) && SBServer.serverLobby.nameIsValid(name)) {
                    sbL.player.changeName(name);
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    System.out.println(formerName + " changed name to " + name + ".");
                    sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
                } else if (!SBServer.serverLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Invalid name. Try again.");
                } else if (SBServer.serverLobby.nameIsTaken(name)) {
                    throw new NameTakenException(name, sbL);
                }
            } else if(input[1].equals("Status")) {
                String status = Status.valueOf(input[2]).toString();
                sbL.player.changeStatus(Status.valueOf(input[2]));
                sbL.getPW().println("PRINT§Terminal§Status changed to " + status + ".");
                sendAllExceptOne("PRINT§Terminal§" + sbL.player.getName() + " is " + status + ".", sbL);
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
    void chatMessage() throws NoCommandException {
        try {
            if (input.length < 3) return;
            // System.out.println("Received  '" + input[1] + "' chat message from " + sbL.player.getName() + ": " + input[2]);
            if(input[1].equals("Global")) {
                sbL.getPW().println("CHATM§Global§You: " + input[2]);
                sendAllExceptOne("CHATM§Global§" + sbL.player.getName() + ": " + input[2], sbL);
            } else if(input[1].equals("Private")) {
                String[] nameAndMes = input[2].split("§", 2);
                if(SBServer.getLobby().getPlayerByName(nameAndMes[0]) == null) {
                    sbL.getPW().println("PRINT§Terminal§This name does not exist.");
                } else {
                    sbL.getPW().println("CHATM§Private§(to " + nameAndMes[0] + "): " + nameAndMes[1]);
                    SBServer.getLobby().getPlayerByName(nameAndMes[0]).getSBL().getPW().
                            println("CHATM§Private§(from " + sbL.player.getName() + "): " + nameAndMes[1]);
                }
            } throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    /**
     * Method for command "LGOUT". Terminates the SBListener of the player logging out,
     * closes socket and sends message to all other players on which player logged out.
     */
    void logout() {
        sbL.pw.println("LGOUT");
        serverLobby.removePlayer(sbL.player);
        sbL.stopRunning();
        try {
            sbL.pw.close();
            sbL.br.close();
            sbL.sock.close();
        } catch(IOException ioe) {
            System.out.println("Issues while closing the socket at logout.");
        }
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.", sbL);
        // System.out.println(sbL.player.getName() + " logged out.");

    }

    /**
     * Method for command "NWGME". Starts a new game with the first 4 players found with PlayerStatus 'READY'.
     */
    void newGame() {
        if(input[1].equals("New")) {
            ArrayList<Player> newPlayers = new ArrayList<Player>();
            newPlayers.add(sbL.player);
            int playerCount = 1;
            for (int i = 0; i < SBServer.getLobby().getLength(); i++) {
                if (SBServer.getLobby().getPlayer(i).getStatus().equals(Status.READY)
                                && !SBServer.getLobby().getPlayer(i).equals(sbL.player)) {
                    newPlayers.add(SBServer.getLobby().getPlayer(i));
                    playerCount++;
                }
                if (playerCount == 2) {
                    Game game = new Game(newPlayers);
                    serverLobby.addGame(game);
                    for(Player p : newPlayers) {
                        p.changeGame(game);
                        p.changeStatus(Status.INGAME);
                        p.getSBL().getPW().println("NWGME§New§");
                    }
                    return;
                }
            }
        }
    }

    /**
     * Method for command "PUTTO". Triggers needed methods of the Game class.
     */
    void putTo() {
        if(sbL.getGameLobby().get(sbL.player.getGame().getPlayersTurn()).equals(sbL.player)) {
            sbL.getPW().println("PRINT§Terminal§Wait until it's your turn, you impatient bastard!");
            return;
        }
        String[] arguments = input[2].split("§");
        if(arguments.length < 4) return;
        int indexF = Integer.parseInt(arguments[1])-1;
        int indexT = Integer.parseInt(arguments[3])-1;
        System.out.println(arguments[0] + arguments[2] + ": piles; " + indexF + indexT + ": indexes.");
        switch(arguments[0]+arguments[2]) {
            case "HB": sbL.player.getGame().playToMiddle(sbL.player, indexF, indexT); break;
            case "SB": sbL.player.getGame().playFromStockToMiddle(sbL.player, indexT); break;
            case "DB": sbL.player.getGame().playFromDiscardToMiddle(sbL.player, indexF, indexT); break;
            case "HD": sbL.player.getGame().playToDiscard(sbL.player, indexF, indexT); break;
        }
    }

    /**
     * @param message: String sent to all clients in main lobby or game, according
     *               to where the player is.
     */
    public void sendAll(String message, SBListener sbL) {
        if(sbL.player.getStatus().equals(Status.INGAME)) {
            for(int i = 0; i < sbL.getGameLobby().size(); i++) {
                sbL.getGameLobby().get(i).getSBL().pw.println(message);
            }
        } else {
            for(int i = 0; i < serverLobby.getLength(); i++) {
                if(!serverLobby.getPlayer(i).getStatus().equals(Status.INGAME)) {
                    serverLobby.getSBL(i).pw.println(message);
                }
            }
        }
    }

    /**
     * @param message: String sent to all clients in main lobby or game, according
     *               to where the player is.
     * @param sbL: ... except this one
     */
    public void sendAllExceptOne(String message, SBListener sbL) {
        if(sbL.player.getStatus().equals(Status.INGAME)) {
            for(int i = 0; i < sbL.getGameLobby().size(); i++) {
                if(!sbL.getGameLobby().get(i).equals(sbL.player)) {
                    sbL.getGameLobby().get(i).getSBL().pw.println(message);
                }
            }
        } else {
            for(int i = 0; i < serverLobby.getLength(); i++) {
                if(!serverLobby.getSBL(i).equals(sbL) && !serverLobby.getPlayer(i).getStatus().equals(Status.INGAME)) {
                    serverLobby.getSBL(i).pw.println(message);
                }
            }
        }
    }

    /**
     * @param message: String sent to all clients in main lobby AND all games.
     */
    public void broadcast(String message) {
        for(Player p : SBServer.getLobby().getPlayerLobby()) {
            p.getSBL().getPW().println(message);
        }
    }

    /**
     * @param message: String sent to all clients in main lobby AND all games...
     * @param sbL: ... except this one.
     */
    public void broadcastExceptOne(String message, SBListener sbL) {
        for(Player p : SBServer.getLobby().getPlayerLobby()) {
            if(!p.equals(sbL.player)) {
                p.getSBL().getPW().println(message);
            }
        }
    }


}
