package skipbo.game;

import skipbo.server.ProtocolExecutor;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static skipbo.server.SBServer.servLog;

public class Game implements Runnable {

    public ArrayList<Player> players;
    Pile piles;
    int sizeOfStockPile; //3 for DEMO purposes, actually 20
    int playersTurn = 0;
    private Player winner;
    private boolean gameRunning, turnFinished;
    public int turnCounter = 0;
    public double score = -1.0;

    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game(ArrayList<Player> players) {

        this.players = players;
        this.piles = new Pile();
        this.sizeOfStockPile = 3;

    }


    /**
     * Returns the drawPile of the main Game
     */
    public ArrayList<Card> getDrawPile() {
        return this.piles.drawPile;
    }

    public int getPlayersTurn() {
        return this.playersTurn;
    }

    public Pile getPiles() {
        return this.piles;
    }

    public boolean gameIsRunning() {
        return this.gameRunning;
    }

    public void terminateGame() {
        this.gameRunning = false;
    }

    /**
     * Gets a String containing all the players in the game (one per line).
     */
    public String getPlayerList() {
        StringBuilder playerString = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            playerString.append(players.get(i).getName());
            if (!(i == players.size() - 1)) playerString.append("\n");
        }
        return playerString.toString();
    }

    /**
     * Gets a game with its players and status (all in one line).
     */
    public String toString(boolean withDateAndTime) {
        StringBuilder gToString = new StringBuilder();

        if(withDateAndTime) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            gToString.append(dtf.format(now) + " || ");
        }
        gToString.append("Participants: ");

        for (int i = 0; i < players.size(); i++) {
            gToString.append(players.get(i).getName());
            if (!(i == players.size() - 1)) gToString.append(", ");
        }
        if (gameRunning) {
            gToString.append(" || RUNNING.");
        } else {
            if(winner == null) {
                gToString.append(" || TERMINATED || No winner.");
            } else {
                gToString.append(" || FINISHED || WINNER: ").append(this.winner.getName()).append(", ");
                gToString.append("SCORE: " + String.format("%.2f", score));
            }
        }

        return gToString.toString();
    }

    /**
     * This run method creates all card Decks and hands out random cards
     * from the main deck to all players in the game, and starts the first player's turn.
     * Game and Player Objects have an Object of type Pile, which contain
     * all the different pile-types which are specifically needed.
     */
    public void run() {
        servLog.debug("Game thread starting.");
        gameRunning = true;

        this.piles.gamePiles();   // Game gets complete set of cards
        Player firstPlayer = players.get(0);
        firstPlayer.getHandCards().add(0, new Card(1, Color.green));
        firstPlayer.getHandCards().add(1, new Card(3, Color.red));
        firstPlayer.getHandCards().add(2, new Card(4, Color.red));
        firstPlayer.getHandCards().add(3, new Card(8, Color.green));
        firstPlayer.getHandCards().add(4, new Card(Color.cyan));

        for (int k = 5; k >= 0; k--) {   //FOR TESTING AND DEMO PURPOSE
            Card c = new Card(k + 6, Color.red);
            firstPlayer.getStockPile().add(c);
        }

        for (int i = 1; i < players.size(); i++) {     // Players getting their cards
            Player tempPlayer = players.get(i);
            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Game is starting...");
            Random random = new Random();
            for (int j = 0; j < 4; j++) {   //ONLY FOR DEMO PURPOSES
                Card c = new Card(j + 1, Color.green);
                tempPlayer.getHandCards().add(c);
            }
            Card skip = new Card(Color.cyan);
            tempPlayer.getHandCards().add(4, skip);
            /*
            for (int j = 0; j < 5; j++) {    // Draw hand-cards for each player (Actual hand card loop)
                Card c = getDrawPile().get(random.nextInt(getDrawPile().size()));
                tempPlayer.getHandCards().add(c);
                piles.drawPile.remove(c);
            }
            */
            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Your Hand cards are: " + piles.handCardPrint(tempPlayer));

            for (int k = 2; k >= 0; k--) {   //FOR TESTING AND DEMO PURPOSE
                Card c = new Card(k + 6, Color.red);
                tempPlayer.getStockPile().add(c);
            }

            /*for (int j = 0; j < sizeOfStockPile; j++) {    // Draw Stock-Pile cards for each player (REAL METHOD)
                Card c = getDrawPile().get(random.nextInt(getDrawPile().size()));
                piles.drawPile.remove(c);
                tempPlayer.getStockPile().add(c);
            }*/
            Card topCard = tempPlayer.getStockPile().get(tempPlayer.getStockPile().size() - 1);
            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Your Stock card is: " + topCard.number);
        }

        for (Player p : players) {
            String cards = piles.getAllCardsForProtocol(p, p.getGame());
            servLog.debug("NWGME command with cards: " + cards);
            p.getSBL().getPW().println("NWGME§Cards§" + cards);
        }

        startFirstTurn(playersTurn);
        servLog.debug("Game thread finished.");
    }

    void startFirstTurn(int playersTurn) {
        servLog.debug("Entered first turn.");
        turnFinished = false;
        Player player = players.get(playersTurn);
        player.getSBL().getPW().println("PRINT§Terminal§It's your first turn! Your first set of hand cards are: " + piles.handCardPrint(player));
        turnCounter++;
    }

    /**
     * Method to be executed at the start of each player's turn
     * and to fill their hand cards.
     */

    public void startTurn(int playersTurn) {
        Player ply = players.get(playersTurn);
        servLog.debug("Entered startTurn: it's " + ply.getName() + "'s turn.");
        checkDrawPile();
        fillHandCards(ply);
        turnCounter++;

        turnFinished = false;

        ply.getSBL().getPW().println("PRINT§Terminal§It's your turn!");
        ply.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                ply.getStockPile().get(ply.getStockPile().size() - 1).number);

        displayDiscard(ply);

        String[] bPiles = piles.buildPilesPrint();
        for (String str : bPiles) {
            ply.getSBL().getPW().println("PRINT§Terminal§" + str);
        }

        ply.getSBL().getPW().println("PRINT§Terminal§Your hand cards are: " + piles.handCardPrint(ply));

        new ProtocolExecutor().sendAllExceptOne("PRINT§Terminal§It's " + ply.getName()
                + "'s turn!", ply.getSBL());
    }

    /**
     * Checks if player's hand cards are empty. If yes, fills the player's hand again.
     *
     * @param player The player's that currently playing
     */
    public void checkHandCards(Player player) {
        if (player.getHandCards().isEmpty()) {
            fillHandCards(player);
        }
    }

    /**
     * Method playToMiddle processes which player, which Card index they wish to play
     * and which buildPile they wish to play to and carries out the command if valid.
     * Furthermore, removes the specified card from their hand cards.
     *
     * @param currentPlayer  (The player that's playing right now)
     * @param handCardIndex  (The index of the hand card, from 0-4)
     * @param buildDeckIndex (The index of the build pile the player wishes to play to, from 0-3)
     */

    public boolean playToMiddle(Player currentPlayer, int handCardIndex, int buildDeckIndex) {

        if (handCardIndex < 0 || handCardIndex >= currentPlayer.getHandCards().size()) {
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Hand Card Index is invalid!");
            servLog.debug("Invalid index");
            return false;
        }
        if (buildDeckIndex < 0 || buildDeckIndex > 3) {       //if bp index is a false index that cannot be true
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Build Deck Index is invalid!");
            servLog.debug("Invalid build deck index");
            return false;
        }

        servLog.debug("Entered playToMiddle.");
        Card card = currentPlayer.getHandCards().get(handCardIndex);   // returns card at specified index in the hand card arraylist

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        ArrayList<Card> specBuildPile = buildPiles.get(buildDeckIndex);
        Card stockCard = currentPlayer.getStockPile().get(currentPlayer.getStockPile().size() - 1);

        if (specBuildPile.isEmpty()) {      //if build pile is empty
            if (1 == card.number) {
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                        stockCard.number);
                return true;
            } else if (card.col == Color.cyan) {
                card.number = 1;
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                        stockCard.number);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);
                return true;
            } else {
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "To play to an empty pile, the card number has to be 1.");
                return false;
            }
        } else {        // if Build Pile isn't empty
            Card topCard = specBuildPile.get(specBuildPile.size() - 1);
            if (topCard.number == (card.number - 1)) {
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);  //check if buildPile is full and print build pile

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hands cards are now: "
                        + piles.handCardPrint(currentPlayer));

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                        stockCard.number);

                return true;
            } else if (card.col == Color.cyan) {      //if Joker card
                card.number = topCard.number + 1;
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer); //check if buildPile is full and should be emptied.

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hands cards are now: "
                        + piles.handCardPrint(currentPlayer));

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " + stockCard.number);
                return true;
            } else {    // not Joker card and not a number higher than the top card on build pile
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Invalid move! The card you play to build deck " +
                        "has to be one number higher than the card on the build deck.");
                return false;
            }
        }
    }

    /**
     * Checks if build pile top card is 12, if yes, removes cards from that build pile and puts it into and empty pile
     * and prints. If not, prints normally.
     *
     * @param card      card that's played
     * @param buildPile build pile that's played to
     * @param player    Player whose turn it is
     */

    public void checkBuildPileAndPrint(Card card, ArrayList<Card> buildPile, Player player) {
        String[] buildPiles = piles.buildPilesPrint();
        if (card.number == 12) {
            for (String str : buildPiles) {
                new ProtocolExecutor().sendAll("PRINT§Terminal§" + str, player.getSBL());
            }
            piles.emptyPile.addAll(buildPile); //TODO: does add all remove bp cards already? ask

            for (Iterator<Card> bp = buildPile.iterator(); bp.hasNext(); ) { //Iterator to remove all cards from current BP
                bp.next();
                bp.remove();
            }

            new ProtocolExecutor().sendAll("PRINT§Terminal§Your empty pile is: " + piles.emptyPilePrint()
                    ,player.getSBL());

            new ProtocolExecutor().sendAll("PRINT§Terminal§The maximum number has been reached; " +
                    "the deck has been reset to: ", player.getSBL());
            for (String s : buildPiles) {
                new ProtocolExecutor().sendAll("PRINT§Terminal§" + s, player.getSBL());
            }
        } else {
            new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: ", player.getSBL());
            for (String s : buildPiles) {
                new ProtocolExecutor().sendAll("PRINT§Terminal§" + s, player.getSBL());
            }
        }
    }

    /**
     * This method plays a hand card into a discard pile of the player's choice
     * Parameter handCardIndex to know which hand card should be selected to be played
     * Parameter id to know whose turn it is. Furthermore, removes the specified card
     * from Player's hand.
     * Parameter discardPileIndex to know which Discard pile to play to.
     *
     * @param currentPlayer    (The player that's playing right now)
     * @param handCardIndex    (The index of the hand card, from 0-4)
     * @param discardPileIndex (The index of the discard pile that the player wishes to play to.)
     */

    public boolean playToDiscard(Player currentPlayer, int handCardIndex, int discardPileIndex) {

        servLog.debug("Entered playToDiscard.");

        if (handCardIndex < 0 || handCardIndex >= currentPlayer.getHandCards().size()) {
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Hand Card Index is invalid!");
            servLog.debug("Invalid index");
            return false;
        }
        if (discardPileIndex < 0 || discardPileIndex > 3) {
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Discard deck index is invalid!");
            servLog.debug("Invalid discard deck index");
            return false;
        }

        ArrayList<ArrayList<Card>> discardPiles = currentPlayer.getDiscardPile();
        ArrayList<Card> specDiscard = discardPiles.get(discardPileIndex);

        Card card = currentPlayer.getHandCards().get(handCardIndex);

        specDiscard.add(card);
        currentPlayer.getHandCards().remove(card);

        displayDiscard(currentPlayer);
        endTurn();

        return true;

    }

    /**
     * Method to display all Discard piles
     */

    public void displayDiscard(Player player) {
        String[] discardPiles = piles.discardPilesPrint(player);
        for (String s : discardPiles) {
            player.getSBL().getPW().println("PRINT§Terminal§ " + s);
        }
    }

    /**
     * This method is to play the top card from the Stock Pile to a build pile
     * of the player's choosing.
     *
     * @param currentPlayer  (The player that's playing right now)
     * @param buildPileIndex (The index of the pile that the player wishes to play to)
     */


    public Card playFromStockToMiddle(Player currentPlayer, int buildPileIndex) {

        if (buildPileIndex < 0 || buildPileIndex > 3) {      //if bp index is a false index that cannot be true
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Build Deck Index is invalid!");
            servLog.debug("Invalid build deck index");
            return null;
        }
        servLog.debug("Entered playFromStockToMiddle.");
        ArrayList<Card> stockPile = currentPlayer.getStockPile();
        Card stockCard = currentPlayer.getStockPile().get(stockPile.size() - 1);

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        ArrayList<Card> specBuildPile = buildPiles.get(buildPileIndex);

        if (specBuildPile.isEmpty()) {
            if (stockCard.number == 1) {        // if card number is 1, add to new pile
                specBuildPile.add(stockCard);
                currentPlayer.getHandCards().remove(stockCard);
                if (currentPlayer.getStockPile().size() == 0) {  //if stock pile is empty
                    return new Card(-1, Color.cyan);
                }

                checkBuildPileAndPrint(stockCard, specBuildPile, currentPlayer);
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
                currentPlayer.getSBL().getPW().print("PRINT§Terminal§Your stock card is: [" +
                        stockCard.number);
                if (currentPlayer.getStockPile().size() == 0) {  //if stock pile is empty
                    return new Card(-1, Color.cyan);
                }
                return stockPile.get(stockPile.size() - 1);
            } else if (stockCard.col == Color.cyan) { //if stock card is Joker card
                stockCard.number = 1;
                specBuildPile.add(stockCard);
                currentPlayer.getStockPile().remove(stockCard);
                if (currentPlayer.getStockPile().size() == 0) {  //if stock pile is empty
                    return new Card(-1, Color.cyan);
                }

                checkBuildPileAndPrint(stockCard, specBuildPile, currentPlayer);
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                        currentPlayer.getStockPile().get(currentPlayer.getStockPile().size() - 1).number);

                return stockPile.get(stockPile.size() - 1);
            } else {      //if invalid move
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "To play to an empty pile, the card number has to be 1.");
                return null;
            }
        } else {         //case build pile isn't empty
            Card topCard = specBuildPile.get(specBuildPile.size() - 1);
            if (topCard.number == (stockCard.number - 1)) {
                specBuildPile.add(stockCard);
                currentPlayer.getStockPile().remove(stockCard);
                if (currentPlayer.getStockPile().size() == 0) {  //if stock pile is empty
                    return new Card(-1, Color.cyan);
                }
                checkBuildPileAndPrint(stockCard, specBuildPile, currentPlayer);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hands cards are now: "
                        + piles.handCardPrint(currentPlayer));

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your stock card is: " +
                        currentPlayer.getStockPile().get(currentPlayer.getStockPile().size() - 1).number);

                return stockPile.get(stockPile.size() - 1);
            } else if (stockCard.col == Color.cyan) {      // if Skip Bo card
                stockCard.number = 1;
                specBuildPile.add(stockCard);
                currentPlayer.getHandCards().remove(stockCard);
                if (currentPlayer.getStockPile().size() == 0) {  //if stock pile is empty
                    return new Card(-1, Color.cyan);
                }

                checkBuildPileAndPrint(stockCard, specBuildPile, currentPlayer);
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));

                return stockPile.get(stockPile.size() - 1);
            } else {         // If card number isn't 1 and isn't a Skip Bo card
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "Card number has to be one higher than top card on build pile.");
                return null;
            }
        }
    }

    /*
     * Checks if stock pile of the player is empty, if yes, the player has won.
     * @param player Check this player's stock pile
    public boolean checkStockPile(Player player) {
        servLog.info("Entered checkStockPile()");
        if (player.getStockPile().isEmpty()) {
            endGame(player);
            return true;
        }
        return false;
    }
    */

    /**
     * Method to play a card from the discard pile to the build pile
     * with index of build and discard piles to choose what card to play.
     * Also checks validity of the move and replaces card at the
     *
     * @param currentPlayer    (The player that's playing right now)
     * @param discardPileIndex (The index of the hand card, from 0-4)
     * @param buildPileIndex   (The index of the pile that the player wishes to play to)
     */

    public boolean playFromDiscardToMiddle(Player currentPlayer, int discardPileIndex, int buildPileIndex) {
        servLog.debug("Entered playFromDiscardToMiddle.");
        ArrayList<Card> discardPile = currentPlayer.getDiscardPile().get(discardPileIndex);

        if (buildPileIndex < 0 || buildPileIndex > 3) {       //if bp index is a false index that cannot be true
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Build Deck Index is invalid!");
            servLog.debug("Invalid build deck index");
            return false;
        }
        if (discardPileIndex < 0 || discardPileIndex > 3) {   //If Dp index is false
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§Discard deck index is invalid!");
            servLog.debug("Invalid discard deck index");
            return false;
        }

        if (discardPile.isEmpty()) {      //if dp is empty and player tries to play from it
            currentPlayer.getSBL().getPW().println("PRINT§Terminal§That discard pile is empty! Choose another.");
            servLog.debug("Played from an empty discard deck");
            return false;
        }
        ArrayList<Card> specBuildPile = piles.buildPiles.get(buildPileIndex);

        Card card = discardPile.get(discardPile.size() - 1);

        if (specBuildPile.isEmpty()) {
            if (card.number == 1) {                 // if card number is 1, new pile
                specBuildPile.add(card);
                discardPile.remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);
                displayDiscard(currentPlayer);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));

                return true;
            } else if (card.col == Color.cyan) {       //if Joker card, then make it 1 and add
                card.number = 1;
                specBuildPile.add(card);
                discardPile.remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);
                displayDiscard(currentPlayer);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));

                return true;
            } else {            //invalid move
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "To play to an empty pile, the card number has to be 1.");
                return false;
            }
        } else {
            // DONE: joker case added
            Card topCard = specBuildPile.get(specBuildPile.size() - 1);
            if (topCard.number == (card.number - 1)) {      // checks if move is valid, if number is correct
                specBuildPile.add(card);
                discardPile.remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);
                displayDiscard(currentPlayer);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hands cards are now: "
                        + piles.handCardPrint(currentPlayer));
                return true;
            } else if (card.col == Color.cyan) {       //if Joker card
                card.number = (topCard.number + 1);
                specBuildPile.add(card);
                discardPile.remove(card);

                checkBuildPileAndPrint(card, specBuildPile, currentPlayer);

                displayDiscard(currentPlayer);

                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
                return true;
            } else {          //invalid move
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "Card number has to be one higher than top card on build pile.");
                return false;
            }
        }
    }

    /**
     * Method to fill the hand cards up to 5 of the player who's turn it is to play
     * Adds cards from top of draw pile.
     *
     * @param player (Player whose hand you wish to fill)
     */
    public void fillHandCards(Player player) {
        servLog.debug("Entered fillHandCards.");
        ArrayList<Card> drawPile = piles.drawPile;
        int toFill = 5 - player.getHandCards().size();
        if (toFill != 0) {
            for (int i = 0; i < toFill; i++) {
                Card drawCard = drawPile.get(drawPile.size()-1);
                player.getHandCards().add(drawCard);
                drawPile.remove(drawCard);
            }
        }
        new ProtocolExecutor(null, player.getSBL()).check("HandCards");
    }

    /**
     * Checks if draw pile is empty, if yes, adds cards from the empty pile into the draw pile.
     */
    public void checkDrawPile() {
        if (this.getDrawPile().size() <= 5) {
            Collections.shuffle(piles.getEmptyPile());  //shuffles the empty pile
            getDrawPile().addAll(piles.getEmptyPile()); //adds empty pile cards to the draw pile
        }
    }

    /**
     * Method to be run at the end of a player's turn, which
     * then changes turn from one player to the next.
     */
    public void endTurn() {
        servLog.debug("Entered endTurn.");
        if (!(playersTurn == players.size() - 1)) {     //if not the last player in the array, go up by one
            playersTurn++;
        } else {
            playersTurn = 0;        //otherwise start over from first player
            turnCounter++;
        }
        startTurn(playersTurn);     //starts next turn
        turnFinished = true;
    }

    /**
     * A method to run the End Game network protocol, and let the
     * player know that some player has won the game!
     *
     * @param winner (The player that has won the game)
     */
    public void endGame(Player winner) {
        servLog.info("Game ending.");
        gameRunning = false;
        this.winner = winner;

        score = (double) turnCounter / sizeOfStockPile;
        if (winner != null) {
            new ProtocolExecutor().sendAll("ENDGM§Winner§" + winner.getName(), winner.getSBL());
        } else {
            // TODO: option for when game got interrupted without having a winner. SOLVED (see below)
            // Solved: If game is ended w/o a winner, use method game.terminateGame().
        }
        new ProtocolExecutor().gameEnding(this);
    }
}
