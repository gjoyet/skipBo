package skipbo.server;

import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.game.Status;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static skipbo.server.SBServer.*;

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
            servLog.info(name + " logged in.");
            sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
            sendAllExceptOne("PRINT§Terminal§" + name + " joined the room. Say hi!", sbL);
            servLog.debug("Players connected: " + SBServer.getWholePlayerList());
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
                    servLog.info(formerName + " changed name to " + name + ".");
                    sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
                } else if (!SBServer.serverLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Invalid name. Try again.");
                } else if (SBServer.serverLobby.nameIsTaken(name)) {
                    throw new NameTakenException(name, sbL);
                }
            } else if(input[1].equals("Status")) {
                String status = Status.valueOf(input[2]).toString();
                if(sbL.player.getStatus().equals(Status.valueOf(status))) {
                    sbL.getPW().println("PRINT§Terminal§Status is already: " + status + ".");
                    return;
                } else {
                    sbL.player.changeStatus(Status.valueOf(input[2]));
                    sbL.getPW().println("PRINT§Terminal§Status changed to " + status + ".");
                    sendAllExceptOne("PRINT§Terminal§" + sbL.player.getName() + " is " + status + ".", sbL);
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
    void chatMessage() throws NoCommandException {
        try {
            if (input.length < 3) return;
            servLog.debug("Received  '" + input[1] + "' chat message from " + sbL.player.getName() + ": " + input[2]);
            if(input[1].equals("Global")) {
                sbL.getPW().println("CHATM§Global§You: " + input[2]);
                sendAllExceptOne("CHATM§Global§" + sbL.player.getName() + ": " + input[2], sbL);
            } else if(input[1].equals("Private")) {
                String[] nameAndMes = input[2].split("§", 2);
                if(SBServer.getLobby().getPlayerByName(nameAndMes[0]) == null) {
                    sbL.getPW().println("PRINT§Terminal§This name does not exist.");
                } else if(nameAndMes[0].equals(sbL.player.getName())) {
                    sbL.getPW().println("PRINT§Terminal§You private messaged yourself, duh...");
                } else {
                    sbL.getPW().println("CHATM§Private§(to " + nameAndMes[0] + "): " + nameAndMes[1]);
                    SBServer.getLobby().getPlayerByName(nameAndMes[0]).getSBL().getPW().
                            println("CHATM§Private§(from " + sbL.player.getName() + "): " + nameAndMes[1]);
                }
            } else if(input[1].equals("Broadcast")) {
                sbL.getPW().println("CHATM§Broadcast§(BC) You: " + input[2]);
                broadcastExceptOne("CHATM§Broadcast§(BC) " + sbL.player.getName() + input[2], sbL);
            } else throw new NoCommandException(input[0], input[1]);
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
            servLog.warn("Issues while closing the socket at logout.");
        }
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.", sbL);
        servLog.info(sbL.player.getName() + " logged out.");
        playerCount--;
        try {
            sleep(30000); //Prevents server from shutting down immediately, in case a player is being started right in that moment.
        } catch (InterruptedException e) {
            servLog.warn("Sleeptime of server before shutdown interrupted.");
        }
        if(playerCount == 0) {
            try {
                for(Game g : SBServer.getLobby().getGames()) {
                    g.terminateGame();
                }
                File gameFile = new File("skipBoLogs/Games.txt");
                PrintWriter filePW = new PrintWriter(new FileOutputStream(gameFile), true);
                String[] gamesList = SBServer.getGamesList();
                for(String s : gamesList) {
                    filePW.println(s);
                }
            } catch (FileNotFoundException e) {
                servLog.warn("Problem with writing games into a file.");
            }
            System.exit(0);
        }

    }

    /**
     * Method for command "NWGME". Starts a new game with the first 4 players found with PlayerStatus 'READY'.
     */
    void newGame() throws NoCommandException {
        if(input[1].equals("New")) {
            ArrayList<Player> newPlayers = new ArrayList<Player>();
            newPlayers.add(sbL.player);
            int playerCount = 1;
            for (int i = 0; i < SBServer.getLobby().getSize(); i++) {
                if (SBServer.getLobby().getPlayer(i).getStatus().equals(Status.READY)
                                && !SBServer.getLobby().getPlayer(i).equals(sbL.player)) {
                    newPlayers.add(SBServer.getLobby().getPlayer(i));
                    ++playerCount;
                }
                if (playerCount == 2) break;
            }
            if(playerCount == 2) {
                Game game = new Game(newPlayers);
                serverLobby.addGame(game);
                for (Player p : newPlayers) {
                    p.changeGame(game);
                    p.getSBL().getPW().println("NWGME§New§");
                    p.changeStatus(Status.INGAME);
                }
                Thread gameT = new Thread(game); gameT.start();
                servLog.info("Game started.");
                servLog.debug("Size of gameList: " + serverLobby.getGames().size());
                return;
            } else {
                sbL.getPW().println("PRINT§Terminal§Not enough people are ready.");
                servLog.info(sbL.player.getName() + " tried to start game: not enough people were ready.");
            }
        } else throw new NoCommandException(input[0], input[1]);
    }

    /**
     * Method for command "PUTTO". Triggers needed methods of the Game class.
     */
    void putTo() {
        if(!sbL.getGameLobby().get(sbL.player.getGame().getPlayersTurn()).equals(sbL.player)) {
            sbL.getPW().println("PRINT§Terminal§Wait until it's your turn, you impatient little rascal!");
            return;
        }
        String[] arguments = input[2].split("§");
        if(arguments.length < 4) return;
        String pF = arguments[0]; // pile from
        String pT = arguments[2]; // pile to
        int iF = Integer.parseInt(arguments[1])-1; // index from
        int iT = Integer.parseInt(arguments[3])-1; // index to
        servLog.debug("putTo triggered with: Piles: "
                + pF + pT + ", Indexes: " + iF + iT + "." );
        if((pF == "H" && (iF < 1 || iF > 5)) || (pF == "S" && iF != 1) ||
                            (pF == "D" && (iF < 0 || iF > 4)) || iT < 1 || iT > 4) {
            sbL.getPW().println("PRINT§Terminal§Invalid indexes in this move.");
            return;
        }
        switch(pF + pT) {
            case "HB": sbL.player.getGame().playToMiddle(sbL.player, iF, iT); break;
            case "SB": sbL.player.getGame().playFromStockToMiddle(sbL.player, iT); break;
            case "DB": sbL.player.getGame().playFromDiscardToMiddle(sbL.player, iF, iT); break;
            case "HD": sbL.player.getGame().playToDiscard(sbL.player, iF, iT); break;
            default: sbL.getPW().println("PRINT§Terminal§This move is not allowed.");
        }
    }

    void display() throws NoCommandException {
        try {
            switch (input[1]) {
                case "players":
                    sbL.getPW().println("PRINT§Terminal§Players list: " + SBServer.getWholePlayerList());
                    break;
                case "games":
                    String[] gamesList = SBServer.getGamesList();
                    if(gamesList.length == 0) {
                        sbL.getPW().println("PRINT§Terminal§No games have been started until now.");
                    } else {
                        sbL.getPW().println("PRINT§Terminal§Games list:");
                        for (String s : gamesList) {
                            if(s != null) sbL.getPW().println("PRINT§Terminal§" + s);
                        }
                    }
                    break;
                default:
                    throw new NoCommandException(input[0], input[1]);
            }
        } finally {}
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
            for(int i = 0; i < serverLobby.getSize(); i++) {
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
            for(int i = 0; i < serverLobby.getSize(); i++) {
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
