package skipbo.server;

import skipbo.game.Card;
import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.game.Status;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static skipbo.server.SBServer.servLog;

/**
 * The execution of the network protocol is implemented in this class. Every command has one method,
 * with further branching inside each method according to the options.
 */
public class ProtocolExecutor {

    private String[] input;
    private SBListener sbL;

    public ProtocolExecutor() { }

    public ProtocolExecutor(String[] input, SBListener sbL) {
        this.input = input;
        this.sbL = sbL;
    }

    public String[] getInput() { return this.input; }

    /**
     * Only for testing purposes.
     */
    public void setInput(String[] input) {
        this.input = input;
    }

    public SBListener getSBL() { return this.sbL; }

    /**
     * Method for command "SETTO". This command sets a parameter
     * given as option to the value given as argument.
     */
    public void setTo() throws NoCommandException {
        if(input.length < 2) throw new NoCommandException();
        String name = "SBPlayer";
        try {
            if (input[1].equals("Nickname")) {
                if(input.length == 2 || !sbL.getServer().serverLobby.nameIsValid(input[2])) {
                    sbL.pw.println("PRINT§Terminal§Invalid name. Name set to " + name + ".");
                    if(!sbL.getServer().serverLobby.nameIsTaken(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        sbL.getServer().serverLobby.addPlayer(sbL.player);
                    } else throw new NameTakenException(name, sbL);
                } else {
                    name = input[2];
                    if (!sbL.getServer().serverLobby.nameIsTaken(name) && sbL.getServer().serverLobby.nameIsValid(name)) {
                        sbL.player = new Player(sbL.id, name, sbL);
                        sbL.getServer().serverLobby.addPlayer(sbL.player);
                    } else if(sbL.getServer().serverLobby.nameIsTaken(name)) {
                        throw new NameTakenException(name, sbL);
                    }
                }
            } else throw new NoCommandException(input[0], input[1]);
            informLogin(name);
        } catch(NameTakenException nte) {
            name = nte.findName();
            sbL.player = new Player(sbL.id, name, sbL);
            sbL.getServer().serverLobby.addPlayer(sbL.player);
            informLogin(name);
        }
    }

    /**
     * @param name: Feeds all clients the needed information about the login of Player with 'name'
     */
    private void informLogin(String name) {
        sbL.getPW().println("SETTO§Nickname§" + name);
        sbL.getPW().println("PLAYR§List§" + sbL.getServer().getWholePlayerList(sbL.getPlayer()));
        sendAllExceptOne("PLAYR§Joined§" + name, sbL);
        servLog.info(name + " logged in.");
        sbL.pw.println("PRINT§Terminal§Welcome to Skip-Bo, " + name + "!");
        sendAllExceptOne("PRINT§Terminal§" + name + " joined the room. Say hi!", sbL);
        servLog.debug("Players connected: " + sbL.getServer().getWholePlayerList());
    }

    /**
     * Method for command "CHNGE". This command sets a parameter
     * given as option to the value given as argument.
     */
    public void changeTo() throws NoCommandException {
        String formerName = sbL.player.getName();
        if(input.length < 2) throw new NoCommandException();
        try {
            if (input[1].equals("Nickname")) {
                if(sbL.player.getStatus().equals(Status.INGAME)) {
                    sbL.getPW().println("You cannot change your name during a game.");
                    return;
                }
                String name = input[2];
                if(name.equals(sbL.player.getName())) {
                    sbL.pw.println("PRINT§Terminal§Name is already " + name + ".");
                } else if (!sbL.getServer().serverLobby.nameIsTaken(name) && sbL.getServer().serverLobby.nameIsValid(name)) {
                    sbL.player.changeName(name);
                    sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
                    sbL.pw.println("CHNGE§Nickname§" + name);
                    servLog.info(formerName + " changed name to " + name + ".");
                    sendAllExceptOne("PLAYR§Change§" + formerName + "§" + name, sbL);
                    sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
                } else if (!sbL.getServer().serverLobby.nameIsValid(name)) {
                    sbL.pw.println("PRINT§Terminal§Refused: Invalid name. Try again.");
                } else if (sbL.getServer().serverLobby.nameIsTaken(name)) {
                    servLog.debug(sbL.player.getName() + " tried to change name to a name already in use.");
                    throw new NameTakenException(name, sbL);
                }
            } else if(input[1].equals("Status")) {
                if(sbL.player.getStatus().equals(Status.INGAME)) {
                    sbL.getPW().println("You cannot change your status during a game.");
                    return;
                }
                String status = Status.valueOf(input[2]).toString();
                if(status.equalsIgnoreCase("ingame")) {
                    sbL.getPW().println("PRINT§Terminal§You cannot change your status to 'ingame' yourself.");
                } else if(sbL.player.getStatus().equals(Status.valueOf(status))) {
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
            sbL.pw.println("PRINT§Terminal§Name changed to " + name + ".");
            sbL.pw.println("CHNGE§Nickname§" + name);
            servLog.info(formerName + " changed name to " + name + ".");
            sendAllExceptOne("PLAYR§Change§" + formerName + "§" + name, sbL);
            sendAllExceptOne("PRINT§Terminal§" + formerName + " changed name to " + name + ".", sbL);
        }
    }

    /**
     * Method for command "CHATM". Sends received chat message to all
     * players except the one sending it.
     */
    public void chatMessage() throws NoCommandException {
        if(input.length < 2) throw new NoCommandException();
        try {
            if (input.length < 3) return;
            servLog.debug("Received '" + input[1] + "' chat message from " + sbL.player.getName() + ": " + input[2]);
            if(input[1].equals("Global")) {
                sbL.getPW().println("CHATM§Global§You: " + input[2]);
                sendAllExceptOne("CHATM§Global§" + sbL.player.getName() + ": " + input[2], sbL);
            } else if(input[1].equals("Private")) {
                String[] nameAndMes = input[2].split("§", 2);
                if(sbL.getServer().getLobby().getPlayerByName(nameAndMes[0]) == null) {
                    sbL.getPW().println("PRINT§Terminal§This name does not exist.");
                } else if(nameAndMes[0].equals(sbL.player.getName())) {
                    sbL.getPW().println("PRINT§Terminal§You private messaged yourself, duh...");
                } else {
                    sbL.getPW().println("CHATM§Private§(to " + nameAndMes[0] + "): " + nameAndMes[1]);
                    sbL.getServer().getLobby().getPlayerByName(nameAndMes[0]).getSBL().getPW().
                            println("CHATM§Private§(from " + sbL.player.getName() + "): " + nameAndMes[1]);
                }
            } else if(input[1].equals("Broadcast")) {
                sbL.getPW().println("CHATM§Broadcast§(BC) You: " + input[2]);
                broadcastExceptOne("CHATM§Broadcast§(BC) " + sbL.player.getName() + ": " + input[2], sbL);
            } else throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    /**
     * Method for command "LGOUT". Terminates the SBListener of the player logging out,
     * closes socket and sends message to all other players on which player logged out.
     */
    public void logout() {
        sbL.pw.println("LGOUT");
        sbL.getServer().serverLobby.removePlayer(sbL.player);
        if (sbL.player.getGame() != null) {
            if(sbL.getPlayer().getGame().players.size() == 2) {
                sbL.player.getGame().players.remove(sbL.player);
                sbL.getPlayer().getGame().terminateGame();
            } else {
                sbL.getPlayer().getGame().playerLeaving(sbL.player);
                sbL.player.getGame().players.remove(sbL.player);
            }
        }
        for(Player p : sbL.getServer().serverLobby.getPlayerLobby()) {
            p.getSBL().getPW().println("PLAYR§Left§" + sbL.getPlayer().getName());
        }
        sendAll("PRINT§Terminal§" + sbL.player.getName() + " left the room.", sbL);
        sbL.stopRunning();
        try {
            sbL.pw.close();
            sbL.br.close();
            sbL.sock.close();
        } catch(IOException ioe) {
            servLog.warn("Issues while closing the socket at logout.");
        }
        servLog.info(sbL.player.getName() + " logged out.");
        sbL.getServer().playerCount--;
        try {
            sleep(2000); //Prevents server from shutting down immediately, in case a player is being started right in that moment.
        } catch (InterruptedException e) {
            servLog.warn("Sleeptime of server before shutdown interrupted.");
        }
        if(sbL.getServer().playerCount == 0) {
            System.exit(0);
        }

    }

    /**
     * Method for command "NWGME". Starts a new game with the first 4 players found with PlayerStatus 'READY'.
     */
    public void newGame() throws NoCommandException {
        if(input.length < 2) throw new NoCommandException();
        if(input[1].equals("New")) {
            int n = 2;
            int x = 20;
            if(input.length == 3) {
                String[] nAndX = input[2].split("§");
                n = Integer.parseInt(nAndX[0]);
                x = Integer.parseInt(nAndX[1]);
                if(!(n == 2 || n == 3 || n == 4)) n = 2;
                if(!(x == 3 || x == 10 || x == 20 || x == 30)) x = 20;
            }
            ArrayList<Player> newPlayers = new ArrayList<Player>();
            newPlayers.add(sbL.player);
            int playerCount = 1;
            for (int i = 0; i < sbL.getServer().getLobby().getSize(); i++) {
                if (sbL.getServer().getLobby().getPlayer(i).getStatus().equals(Status.READY)
                                && !sbL.getServer().getLobby().getPlayer(i).equals(sbL.player)) {
                    newPlayers.add(sbL.getServer().getLobby().getPlayer(i));
                    ++playerCount;
                }
                if (playerCount == n) break;
            }
            if(playerCount == n) {
                Game game = new Game(newPlayers, x);
                sbL.getServer().serverLobby.addGame(game);
                String names = "";
                for(Player p : newPlayers) {
                    names += p.getName() + "§";
                }
                servLog.debug("NWGME command with names: " + x + "§" + names);
                for (Player p : newPlayers) {
                    p.changeGame(game);
                    p.getSBL().getPW().println("NWGME§Names§" + x + "§" + names);
                    p.changeStatus(Status.INGAME);
                }
                Thread gameT = new Thread(game); gameT.start();
                servLog.info("Game started.");
                servLog.debug("Size of gameList: " + sbL.getServer().serverLobby.getGames().size());
                return;
            } else {
                sbL.getPW().println("PRINT§Terminal§Not enough people are ready.");
                servLog.debug(sbL.player.getName() + " tried to start game: not enough people were ready.");
            }
        } else throw new NoCommandException(input[0], input[1]);
    }

    /**
     * Method for command "PUTTO". Triggers needed methods of the Game class.
     */
    void putTo() throws NoCommandException {
        try {
            if (input[1].equals("Card")) {
                servLog.debug("Got into putTo method with input: " + input[2] + ".");
                if (!sbL.getGameLobby().get(sbL.player.getGame().getPlayersTurn()).equals(sbL.player)) {
                    sbL.getPW().println("PRINT§Terminal§Wait until it's your turn, you impatient little rascal!");
                    return;
                }
                String[] arguments = input[2].split("§");
                if (arguments.length < 4) {
                    sbL.getPW().println("Missing an argument for command 'play'.");
                    return;
                }
                String pF = arguments[0]; // pile from
                String pT = arguments[2]; // pile to
                int iF = Integer.parseInt(arguments[1]) - 1; // index from
                int iT = Integer.parseInt(arguments[3]) - 1; // index to
                if ((pF.equals("H") && (iF < 0 || iF > 4)) || (pF.equals("S") && iF != 0) ||
                        (pF.equals("D") && (iF < 0 || iF > 3)) || iT < 0 || iT > 3) {
                    sbL.getPW().println("PRINT§Terminal§Invalid indexes in this move.");
                    return;
                }
                switch (pF + pT) {
                    case "HB":
                        if (sbL.player.getGame().playToMiddle(sbL.player, iF, iT)) {
                            sbL.getPW().println("PUTTO§Response§" + input[2] + "§" + sbL.player.getName());
                            this.check("HandCards");
                            sbL.player.getGame().checkHandCards(sbL.player);
                        }
                        break;
                    case "SB":
                        Card stockPileTopCard = sbL.player.getGame().playFromStockToMiddle(sbL.player, iT);
                        if (stockPileTopCard != null && stockPileTopCard.number != -1) {
                            sbL.getPW().println("PUTTO§StockResponse§" + input[2] + "§" + sbL.player.getName()
                                    + "§" + stockPileTopCard.getColString() + "§" + stockPileTopCard.number);
                            servLog.debug("Sending StockResponse command from server with arguments: " + input[2] + "§"
                                                        + sbL.player.getName() + "§" + stockPileTopCard.getColString()
                                                                                    + "§" + stockPileTopCard.number);
                        } else if(stockPileTopCard != null) {
                            sbL.getPW().println("PUTTO§StockResponse§" + input[2] + "§" + sbL.player.getName()
                                        + "§" + stockPileTopCard.getColString() + "§" + stockPileTopCard.number);
                        }
                        break;
                    case "DB":
                        if (sbL.player.getGame().playFromDiscardToMiddle(sbL.player, iF, iT)) {
                            sbL.getPW().println("PUTTO§Response§" + input[2] + "§" + sbL.player.getName());
                        }
                        break;
                    case "HD":
                        if (sbL.player.getGame().playToDiscard(sbL.player, iF, iT)) {
                            sbL.getPW().println("PUTTO§Response§" + input[2] + "§" + sbL.player.getName());
                            this.check("HandCards");
                            if(sbL.getGameLobby().get(sbL.player.getGame().getPlayersTurn()).equals(sbL.player)) {
                                sbL.player.getGame().checkHandCards(sbL.player);
                            }
                        }
                        break;
                    default:
                        sbL.getPW().println("PRINT§Terminal§This move is not allowed.");
                }
            } else if(input[1].equals("Update")) {
                String[] arguments = input[2].split("§");
                if(arguments[0].equals("S") && arguments[4].equals("-1")) {
                    sendAllExceptOne("PUTTO§Update§" + input[2], sbL);
                    Player winner = sbL.getServer().serverLobby.getPlayerByName(arguments[2]);
                    winner.getGame().endGame(winner);
                } else {
                    sendAllExceptOne("PUTTO§Update§" + input[2], sbL);
                }
            } else throw new NoCommandException(input[0], input[1]);
        } finally {}
    }

    public void check(String option) {
        String cards;
        try {
            switch (option) {
                case "HandCards":
                    cards = sbL.player.getGame().getPiles().getHandCardsForProtocol(sbL.player);
                    servLog.debug("Sending CHECK HandCards command with arguments = " + cards);
                    sbL.getPW().println("CHECK§HandCards§" + cards);
                    break;
                case "StockCard":
                    Card stockC = sbL.player.getStockPile().get(sbL.player.getStockPile().size()-1);
                    servLog.debug("Sending CHECK StockCard command with arguments: "
                                                                + stockC.getColString() + "§" + stockC.number);
                    sbL.getPW().println("CHECK§StockCard§" + stockC.getColString() + "§" + stockC.number);
                case "AllCards":
                    cards = sbL.player.getGame().getPiles().getHandCardsForProtocol(sbL.player);
                    servLog.debug("Sending CHECK AllCards command with arguments = " + cards);
                    sbL.getPW().println("CHECK§AllCards§" + cards);
                default: servLog.debug("Not a valid CHECK option.");
            }
        } finally {}
    }

    /**
     * Method for command DISPL. Displays certain elements (players, games,...) to client.
     */
    public void display() throws NoCommandException {
        if(input.length < 2) throw new NoCommandException();
        try {
            switch (input[1]) {
                case "players":
                    sbL.getPW().println("PRINT§Terminal§Players list: " + sbL.getServer().getWholePlayerList());
                    break;
                case "games":
                    String[] gamesList = sbL.getServer().getGamesList();
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

    public void gameEnding(Game game) {
        // Ending game
        for (Player p : game.players) {
            p.changeStatus(Status.WAITING);
            p.clearHandCards();
            p.clearDiscardPiles();
            p.clearStockPile();
            p.changeGame(null);
        }

        // Writing game into highscores file
        File highScoreOld = new File("skipBoLogs/Highscores.txt");
        File tempFile = new File("skipBoLogs/tempFile.txt");
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            servLog.debug("Initializing br and pw.");
            if(!tempFile.createNewFile()) servLog.error("Could not create file.");
            br = new BufferedReader(new FileReader(highScoreOld));
            pw = new PrintWriter(new FileOutputStream(tempFile), true);
        } catch(FileNotFoundException fnfe) {
            servLog.error("File not found.");
        } catch(IOException ioe) {
            servLog.error("Problem with tempFile.");
        }

        try {
            pw.println(br.readLine());
            pw.println(br.readLine());
            String line = br.readLine();
            String[] lineSplit;
            boolean gameAppended = false;
            while (line != null) {
                lineSplit = line.split("SCORE: ");
                servLog.debug("lineSplit[1] = " + lineSplit[1]);
                double scoreInLine = Double.parseDouble(lineSplit[1]);
                servLog.debug("score on this line = " + scoreInLine + ", game.score is " + game.score);
                servLog.debug("gameAppended is: " + gameAppended);
                if(scoreInLine <= game.score || gameAppended) {
                    servLog.debug("Writing from file.");
                    pw.println(line);
                } else {
                    servLog.debug("Writing game.");
                    pw.println(game.toString(true));
                    pw.println(line);
                    gameAppended = true;
                }
                line = br.readLine();
            }
            if (!gameAppended) {
                servLog.debug("Worst score: writing game at the end of file.");
                pw.println(game.toString(true));
            }
        } catch (IOException ioe) {
            servLog.error("Problem with reading from Highscores.txt file.");
        }

        servLog.debug("Deleting old file and renaming tempFile.");
        highScoreOld.delete();
        tempFile.renameTo(new File("skipBoLogs/Highscores.txt"));

        try {
            br.close();
            pw.close();
        } catch (IOException e) {
            servLog.debug("Problem with closing br and pw after writing in highscores.txt");
        }
    }

    /**
     * @param message: String sent to all clients in main lobby or game, according
     *               to where the player is.
     */
    public void sendAll(String message, SBListener sbL) {
        String[] splitMessage = message.split("\n");
        if(sbL.player.getStatus().equals(Status.INGAME)) {
            for(int i = 0; i < sbL.getGameLobby().size(); i++) {
                for(String s : splitMessage) sbL.getGameLobby().get(i).getSBL().pw.println(s);
            }
        } else {
            for(int i = 0; i < sbL.getServer().serverLobby.getSize(); i++) {
                if(!sbL.getServer().serverLobby.getPlayer(i).getStatus().equals(Status.INGAME)) {
                    for(String s : splitMessage) sbL.getServer().serverLobby.getSBL(i).pw.println(s);
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
        String[] splitMessage = message.split("\n");
        if(sbL.player.getStatus().equals(Status.INGAME)) {
            for(int i = 0; i < sbL.getGameLobby().size(); i++) {
                if(!sbL.getGameLobby().get(i).equals(sbL.player)) {
                    for(String s : splitMessage) sbL.getGameLobby().get(i).getSBL().pw.println(s);
                }
            }
        } else {
            for(int i = 0; i < sbL.getServer().serverLobby.getSize(); i++) {
                if(!sbL.getServer().serverLobby.getSBL(i).equals(sbL) &&
                                !sbL.getServer().serverLobby.getPlayer(i).getStatus().equals(Status.INGAME)) {
                    for(String s : splitMessage) sbL.getServer().serverLobby.getSBL(i).pw.println(s);
                }
            }
        }
    }

    /**
     * @param message: String sent to all clients in main lobby AND all games.
     */
    public void broadcast(String message) {
        String[] splitMessage = message.split("\n");
        for(Player p : sbL.getServer().getLobby().getPlayerLobby()) {
            for(String s : splitMessage) p.getSBL().getPW().println(s);
        }
    }

    /**
     * @param message: String sent to all clients in main lobby AND all games...
     * @param sbL: ... except this one.
     */
    public void broadcastExceptOne(String message, SBListener sbL) {
        String[] splitMessage = message.split("\n");
        for(Player p : sbL.getServer().getLobby().getPlayerLobby()) {
            if(!p.equals(sbL.player)) {
                for(String s : splitMessage) p.getSBL().getPW().println(s);
            }
        }
    }
}
