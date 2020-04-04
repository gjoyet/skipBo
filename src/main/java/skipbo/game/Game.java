package skipbo.game;

import skipbo.server.ProtocolExecutor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Game {

    public ArrayList<Player> players;
    public Pile piles;
    private Player winner, whosTurn;
    private boolean gameRunning, turnFinished;
    int sizeOfStockPile = 20;
    int playersTurn = 0;


    /**
     * Constructor for Object Game, where the main Game and Game rules
     * will be implemented.
     */
    public Game(ArrayList<Player> players) {

        this.players = players;
        this.whosTurn = whosTurn;
        this.winner = winner;
        this.piles = new Pile();
        this.gameRunning = gameRunning;
        this.turnFinished = turnFinished;
        this.sizeOfStockPile = 20;

        dealCards();
        startTurn(playersTurn);


        gameRunning = true;
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

    public boolean gameIsRunning() {
        return this.gameRunning;
    }

    public String toString() {
        StringBuilder gToString = new StringBuilder("Participants: ");
        for (int i = 0; i < players.size(); i++) {
            gToString.append(players.get(i).getName());
            if (!(i == players.size() - 1)) gToString.append(", ");
        }
        if (gameRunning) gToString.append("; RUNNING.");
        else gToString.append("; FINISHED.");

        return gToString.toString();
    }

    /**
     * Method start() runs at the beginning of the Game, and waits until
     * boolean value turnFinished
     */

    public void start() {
        while (gameRunning) {
            startTurn(playersTurn);
            while (!turnFinished) {
                try {
                    sleep(100);
                } finally {
                }
            }
            try {
                sleep(100);
            } finally {
            }
        }
    }


    /**
     * setUpGame creates all card Decks and hands out random cards
     * from the main deck to all players in the game.
     * Game and Player Objects have an Object of type Pile, which contain
     * all the different pile-types, which are specifically needed.
     */
    public void dealCards() {

        this.piles.gamePiles();   // Game gets complete set of cards

        for (int i = 0; i < players.size(); i++) {     // Players getting their cards

            Player tempPlayer = players.get(i);
            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Game is starting...");
            Random random = new Random();   // Object random for card distribution by chance

            for (int j = 0; j < 5; j++) {    // Draw hand-cards for each player
                Card c = getDrawPile().get(random.nextInt(getDrawPile().size()));
                tempPlayer.getHandCards().add(c);
                piles.drawPile.remove(c);
            }

            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Your Hand cards are: " + piles.handCardPrint(tempPlayer));

            for (int j = 0; j < sizeOfStockPile; j++) {    // Draw Stock-Pile cards for each player
                Card c = getDrawPile().get(random.nextInt(getDrawPile().size()));
                piles.drawPile.remove(c);
                tempPlayer.getStockPile().add(c);
            }
            Card topCard = tempPlayer.getStockPile().get(tempPlayer.getStockPile().size() - 1);
            tempPlayer.getSBL().getPW().println("PRINT§Terminal§Your Stock card is: " + topCard.number);
        }
    }

    /**
     * Method to be executed at the start of each player's turn
     * and to fill their hand cards.
     */

    public void startTurn(int playersTurn) {
        turnFinished = false;
        Player ply = players.get(playersTurn);
        ply.getSBL().getPW().println("PRINT§Terminal§It's your turn! Your hand cards are now: "
                + piles.handCardPrint(ply));

        ply.fillHandCards();
        new ProtocolExecutor().sendAllExceptOne("PRINT§Terminal§Gave " + ply.getName()
                + " their missing cards.", ply.getSBL());
    }

    /**
     * Method playToMiddle processes which player, which Card index they wish to play
     * and which buildPile they wish to play to and carries out the command if valid.
     * Furthermore, removes the specified card from their hand cards.
     *
     * @param currentPlayer
     * @param handCardIndex
     * @param buildDeckIndex
     */

    public void playToMiddle(Player currentPlayer, int handCardIndex, int buildDeckIndex) {
        Card card = currentPlayer.getHandCards().get(handCardIndex);   // returns card at specified index in the hand card arraylist

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        ArrayList<Card> specBuildPile = buildPiles.get(buildDeckIndex);

        //Card topCard = specBuildPile.get(specBuildPile.size() - 1);

        if (specBuildPile.isEmpty()) {
            if (card.number == 1) {
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);
                new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                        + piles.buildPilesPrint(), currentPlayer.getSBL());
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
            } else if (card.number != 1 && card.col != Color.CYAN) {
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! " +
                        "To play to an empty pile, the card number has to be 1.");
            }
            if (card.col == Color.CYAN) {
                card.number = 1;
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hand cards are now: "
                        + piles.handCardPrint(currentPlayer));
                for (Player player : players) {
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                            + piles.buildPilesPrint(), currentPlayer.getSBL());
                }
            }
        } else {
            Card topCard = specBuildPile.get(specBuildPile.size() - 1);
            if (topCard.number == (card.number - 1)) {
                specBuildPile.add(card);
                currentPlayer.getHandCards().remove(card);
                for (Player p : players) {
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                            + piles.buildPilesPrint(), currentPlayer.getSBL());
                }
                currentPlayer.getSBL().getPW().println("PRINT§Terminal§Your hands cards are now: "
                        + piles.handCardPrint(currentPlayer));
            }
            if (card.number == 12) {
                for (int i = 0; i < 12; i++) {    // remove all cards from the buildPile if the top card is 12
                    specBuildPile.remove(i);
                }
                for (Player player : players) {
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The maximum number has been reached; the deck has been reset to: "
                            + piles.buildPilesPrint(), player.getSBL());
                }
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
     * @param currentPlayer
     * @param handCardIndex
     * @param discardPileIndex
     */

    public void playToDiscard(Player currentPlayer, int handCardIndex, int discardPileIndex) {
        currentPlayer.getSBL().getPW().println("PRINT§Terminal§You are playing to discard now!");
        ArrayList<ArrayList<Card>> discardPiles = currentPlayer.getDiscardPile();
        ArrayList<Card> specDiscard = discardPiles.get(discardPileIndex);

        Card card = currentPlayer.getHandCards().get(handCardIndex);

        specDiscard.add(card);
        currentPlayer.getHandCards().remove(card);
        //DONE: Execute: update Discard pile and hand cards
        displayDiscard();

        endTurn();

    }

    public void displayDiscard() {
        for (Player player : players) {
            new ProtocolExecutor().sendAll("PRINT§Terminal§ " +
                    piles.discardPilesPrint(player), player.getSBL());
        }
    }

    /**
     * This method is to play the top card from the Stock Pile to a build pile
     * of the player's choosing.
     * Param id for player, Param id to know which Build pile to play to.
     *
     * @param currentPlayer
     * @param buildPileIndex
     */

    public void playFromStockToMiddle(Player currentPlayer, int buildPileIndex) {
        ArrayList<Card> stockPile = currentPlayer.getStockPile();
        Card stockCard = stockPile.get(stockPile.size() - 1);

        ArrayList<ArrayList<Card>> buildPiles = piles.buildPiles;
        var specBuildPile = buildPiles.get(buildPileIndex);

        Card topCard = specBuildPile.get(specBuildPile.size() - 1);

        if (stockCard.col == Color.CYAN) {
            int num = specBuildPile.get(specBuildPile.size()).number;
            stockCard.number = num + 1;
            specBuildPile.add(stockCard);
            new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                    + buildPiles.toString(), currentPlayer.getSBL());
        } else {
            if (!(specBuildPile.isEmpty())) {
                if (topCard.number == (stockCard.number - 1)) {
                    specBuildPile.add(stockCard);
                    currentPlayer.getStockPile().remove(stockCard);
                    topCard = stockCard;     // could be redundant
                    // Execute: card op valid - update board and stock pile card.
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                            + buildPiles.toString(), currentPlayer.getSBL());
                    piles.handCardPrint(currentPlayer);

                    if (stockCard.number == 12) {
                        //Execute: make that BuildPile go away
                        for (int i = 0; i < 12; i++) {    // remove all cards from the buildPile if the top card is 12
                            specBuildPile.remove(i);
                        }
                        new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                                + buildPiles.toString(), currentPlayer.getSBL());

                    }
                } else {
                    //EXECUTE: the move is invalid!
                    currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid!");
                }
            } else {
                if (stockCard.number == 1) {
                    specBuildPile.add(stockCard);
                    //Execute: update build pile
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                            + buildPiles.toString(), currentPlayer.getSBL());
                    piles.handCardPrint(currentPlayer);
                } else if (stockCard.number != 1) {
                    //Execute: invalid move! Card 1 has to be first card on build pile.
                    currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! The first card has to have " +
                            "the number 1.");
                }
            }
        }
        if (currentPlayer.getStockPile().isEmpty()) {
            endGame(currentPlayer);
        }
    }

    /**
     * Method to play a card from the discard pile to the build pile
     * with index of build and discard piles to choose what card to play.
     * Also checks validity of the move and replaces card at the
     *
     * @param currentPlayer
     * @param discardPileIndex
     * @param buildPileIndex
     */

    public void playFromDiscardToMiddle(Player currentPlayer, int discardPileIndex, int buildPileIndex) {
        // TODO: Rohan, I let you implement this method since you know how you handled the other cases.
        ArrayList<Card> discardPile = currentPlayer.getDiscardPile().get(discardPileIndex);
        ArrayList<Card> specBuildPile = piles.buildPiles.get(buildPileIndex);

        Card card = discardPile.get(discardPile.size());

        if (card.col == Color.CYAN) {
            int num = specBuildPile.get(specBuildPile.size()).number;
            card.number = num + 1;
            specBuildPile.add(card);
            new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                    + discardPile.toString(), currentPlayer.getSBL());
        } else {
            if (!specBuildPile.isEmpty()) {
                Card topCard = specBuildPile.get(specBuildPile.size());
                if (topCard.number == (card.number - 1)) {
                    specBuildPile.add(card);
                    discardPile.remove(card);
                    new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                            + piles.buildPiles.toString(), currentPlayer.getSBL());

                    if (card.number == 12) {
                        for (int i = 0; i < 12; i++) {    // remove all cards from the buildPile if the top card is 12
                            specBuildPile.remove(i);
                        }
                        new ProtocolExecutor().sendAll("PRINT§Terminal§The build decks are now: "
                                + piles.buildPiles.toString(), currentPlayer.getSBL());
                    }
                } else {
                    //move invalid
                    currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid!");
                }
            } else {
                if (card.number == 1) {
                    specBuildPile.add(card);
                } else if (card.number != 1) {
                    currentPlayer.getSBL().getPW().println("PRINT§Terminal§This move is invalid! The first card has to have " +
                            "the number 1.");
                }
            }
        }
    }

    //TODO: reshuffle()!

    /**
     * Method to be run at the end of a player's turn, which
     * then changes turn from one player to the next.
     */
    public void endTurn() {
        if (!(playersTurn == players.size() - 1)) {
            playersTurn++;
        } else {
            playersTurn = 0;
        }
        startTurn(playersTurn);
        turnFinished = true;
    }


    /**
     * A method to run the End Game network protocol, and let the
     * player know that some player has won the game!
     *
     * @param winner
     */
    public void endGame(Player winner) {
        gameRunning = false;
        //EXECUTE: EndGame Protocol (sysout: Player X won game!) server shut, reshuffle etc.
        new ProtocolExecutor().sendAll(winner.getName() + " has won the game!", winner.getSBL());
    }

    /**
     * A sleep method with long parameter to sleep the given amount of time.
     *
     * @param ms
     */
    private void sleep(long ms) {
        try {
            sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
