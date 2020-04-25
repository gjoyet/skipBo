package skipbo.client;

import skipbo.server.Protocol;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

import static skipbo.client.SBClient.clientLog;

/**
 * Will be class for the Game GUI in the future
 */
public class GameGraphic implements ActionListener {

    private final SBClientListener clientListener;
    private DefaultButtonModel notClickableModel;
    private final DefaultButtonModel defaultButtonModel = new DefaultButtonModel();
    private CardButton button1Pressed = null;
    private final Border defaultBorder = UIManager.getBorder("Button.border");
    private final Border clickedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
    private final String playerName;
    private final JLayeredPane layeredPane;

    // Layout Manager

    private final int HEIGHT_HAND = 120;
    private final int X_HAND = 550;  // change here
    private final int Y_HAND = 620;  // change here
    private final int X_HAND_DISTANCE = 88;
    private final int Y_HAND_LABEL = Y_HAND-20;

    // Layout of discard piles
    private final int WIDTH_DISCARD = 100;
    private final int HEIGHT_DISCARD = 145;
    private final int X_DISCARD = 550;  // change here
    private final int Y_DISCARD = 330;  // change here
    private final int X_DISCARD_DISTANCE = 120;
    private final int Y_DISCARD_LABEL = Y_DISCARD-20;

    // Layout of stock pile
    private final int WIDTH_STOCK = 100;
    private final int HEIGHT_STOCK = 145;
    private final int X_STOCK = 400;    // change here
    private final int Y_STOCK = 330;    // change here
    private final int Y_STOCK_LABEL = Y_STOCK-20;

    // Layout of build piles
    private final int WIDTH_BUILD = 100;
    private final int HEIGHT_BUILD = 145;
    private final int X_BUILD = 550;  // change here
    private final int Y_BUILD = 120;  // change here
    private final int X_BUILD_DISTANCE = 120;
    private final int Y_BUILD_LABEL = Y_BUILD-20;

    // Layout of draw pile
    private final int WIDTH_DRAW = 100;
    private final int HEIGHT_DRAW = 145;
    private final int X_DRAW = 400;    // change here
    private final int Y_DRAW = 120;    // change here
    private final int Y_DRAW_LABEL = Y_DRAW-20;

    // Layout of opponents
    private final int WIDTH_OP1 = 40; //30
    private final int HEIGHT_OP1 = 58; //50
    private final int X_OP1 = 1050;  // change here
    private final int Y_OP1 = 120;  // change here
    private final int Y_OP2 = Y_OP1+140; //120  // change here
    private final int Y_OP3 = Y_OP2+140;  // change here
    private final int X_OP1_DISTANCE = 45; //35

    //Opponents
    private JLabel e1;
    private JLabel e2;
    private JLabel e3;

    private JLabel[] oppArray;
    private int playerIndex = 0;

    //Own piles
    private final CardButton[] hand = new CardButton[5];
    private CardButton stock;
    //private CardButton[] discard = new CardButton[4];
    private final ArrayList<CardButton>[] discard = new ArrayList[4];

    //Game piles
    private final CardButton[] build = new CardButton[4];

    //Opponent discard piles
    private final ArrayList<CardButton>[] e1_discard = new ArrayList[4];
    private final ArrayList<CardButton>[] e2_discard = new ArrayList[4];
    private final ArrayList<CardButton>[] e3_discard = new ArrayList[4];
/*    private CardButton[] e1_discard = new CardButton[4];
    private CardButton[] e2_discard = new CardButton[4];
    private CardButton[] e3_discard = new CardButton[4];*/

    private final int DISTDISCARD = 30;
    private final int DISTOPPDISCARD = 13;

    //Opponent stock piles
    private CardButton e1_stock;
    private CardButton e2_stock;
    private CardButton e3_stock;

    //Number of cards left on stock piles
    private JLabel numOfStockCards;
    private JLabel[] oppNumStockCards = new JLabel[3];

    private int initialNumStockCards;

    private final CardIcons cardIcons = new CardIcons(WIDTH_OP1, HEIGHT_OP1, 78, 120);


    GameGraphic(SBClientListener clientListener, String name, JTextPane chat) {
        this.clientListener = clientListener;
        playerName = name;
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1550, 870);

        setButtonModel();
        appendDecks();
    }

    /**
     * Creates all necessary components for the game.
     */
    void appendDecks() {
        /*
        Cards of the Player and build piles
        */

        JLabel dp = new JLabel("Your Discard Piles");
        dp.setBounds(X_DISCARD, Y_DISCARD_LABEL, 120, 15);
        JLabel hp = new JLabel("Your hand cards");
        hp.setBounds(X_HAND, Y_HAND_LABEL, 120, 15);
        JLabel sp = new JLabel("Your stock pile");
        sp.setBounds(X_STOCK, Y_STOCK_LABEL, 120, 15);
        JLabel bp = new JLabel("Build piles");
        bp.setBounds(X_BUILD, Y_BUILD_LABEL, 120, 15);
        JLabel dpg = new JLabel("Draw pile");
        dpg.setBounds(X_DRAW, Y_DRAW_LABEL, 120, 15);
        layeredPane.add(dpg);
        layeredPane.add(dp);
        layeredPane.add(hp);
        layeredPane.add(sp);
        layeredPane.add(bp);

        // Discard and build Piles

        for (int i = 0, j = 1; i < discard.length; i++, j++) {
            //discard[i] = new CardButton(CardButton.DISCARD);
            build[i] = new CardButton(CardButton.BUILD);
            build[i].setBounds(X_BUILD + i * X_BUILD_DISTANCE, Y_BUILD, WIDTH_BUILD, HEIGHT_BUILD);
            //layeredPane.add(discard[i]);
            layeredPane.add(build[i]);
            //discard[i].addActionListener(this);
            //build[i].addActionListener(this);
            //discard[i].setName(" D " + j);
            build[i].setName(" B " + j);
            discard[i] = new ArrayList<>();
            CardButton b = new CardButton(CardButton.DISCARD);
            b.setBounds(X_DISCARD + i * X_DISCARD_DISTANCE, Y_DISCARD, WIDTH_DISCARD, HEIGHT_DISCARD);
            b.addActionListener(this);
            b.setName(" D " + j);
            layeredPane.add(b);
            discard[i].add(b);

        }
/*        discard[0].setBounds(620, 400, 100, 145);
        discard[1].setBounds(730, 400, 100, 145);
        discard[2].setBounds(840, 400, 100, 145);
        discard[3].setBounds(950, 400, 100, 145);*/


        /*
         * Test/Example for multiple buttons on top of each other
         */
/*
        dis[0] = new ArrayList<>();
        dis[0].add(new CardButton());
        dis[0].get(dis[0].size()-1).setText("Test1");
        //dis[0].get(dis[0].size()-1).setLocation(discard[0].getLocation());
        dis[0].get(dis[0].size()-1).setBounds(620, 400+20*dis[0].size(), 100, 145);
        layeredPane.add(dis[0].get(dis[0].size()-1), new Integer(dis[0].size()));
        dis[0].get(dis[0].size()-1).setVisible(true);

        dis[0].add(new CardButton());
        dis[0].get(dis[0].size()-1).setText("Test2");
        dis[0].get(dis[0].size()-1).setBounds(620, 400+20*dis[0].size(), 100, 145);
        layeredPane.add(dis[0].get(dis[0].size()-1), new Integer(dis[0].size()));
        dis[0].get(dis[0].size()-1).setVisible(true);
        */

        // hand piles
        for (int i = 0; i < hand.length; ) {
            hand[i] = new CardButton(CardButton.HAND);
            layeredPane.add(hand[i]);
            hand[i].addActionListener(this);
            hand[i].setName(" H " + ++i);
        }
        // Layout of Hand piles
        int WIDTH_HAND = 78;
        hand[0].setBounds(X_HAND, Y_HAND, WIDTH_HAND, HEIGHT_HAND);
        hand[1].setBounds(X_HAND + X_HAND_DISTANCE, Y_HAND, WIDTH_HAND, HEIGHT_HAND);
        hand[2].setBounds(X_HAND + 2*X_HAND_DISTANCE, Y_HAND, WIDTH_HAND, HEIGHT_HAND);
        hand[3].setBounds(X_HAND + 3*X_HAND_DISTANCE, Y_HAND, WIDTH_HAND, HEIGHT_HAND);
        hand[4].setBounds(X_HAND + 4*X_HAND_DISTANCE, Y_HAND, WIDTH_HAND, HEIGHT_HAND);


        //stock pile
        stock = new CardButton(CardButton.STOCK);
        stock.setBounds(X_STOCK, Y_STOCK, WIDTH_STOCK, HEIGHT_STOCK);
        layeredPane.add(stock);
        stock.setName(" S 1");
        stock.addActionListener(this);

        /*
        Cards of the game (draw pile)
        */


        // draw pile
        JButton draw = new JButton();
        draw.setBounds(X_DRAW, Y_DRAW, WIDTH_DRAW, HEIGHT_DRAW);
        layeredPane.add(draw);
        ImageIcon back = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Back.png")));
        draw.setIcon(back);
        setClickable(draw, false);

        /*
        display cards of enemies
        */

        //Labels of enemies

        e1 = new JLabel("Opponent 1");
        e1.setBounds(X_OP1, Y_OP1-20, 120, 15);
        layeredPane.add(e1);

        e2 = new JLabel("Opponent 2");
        e2.setBounds(X_OP1, Y_OP2-20, 120, 15);
        layeredPane.add(e2);

        e3 = new JLabel("Opponent 3");
        e3.setBounds(X_OP1, Y_OP3-20, 120, 15);
        layeredPane.add(e3);

        //Stock piles of enemies
        e1_stock = new CardButton();
        e1_stock.setBounds(X_OP1, Y_OP1, WIDTH_OP1, HEIGHT_OP1);
        layeredPane.add(e1_stock);
        setClickable(e1_stock, false);

        e2_stock = new CardButton();
        e2_stock.setBounds(X_OP1, Y_OP2, WIDTH_OP1, HEIGHT_OP1);
        layeredPane.add(e2_stock);
        setClickable(e2_stock, false);

        e3_stock = new CardButton();
        e3_stock.setBounds(X_OP1, Y_OP3, WIDTH_OP1, HEIGHT_OP1);
        layeredPane.add(e3_stock);
        setClickable(e3_stock, false);


        //Discard piles of enemies

        for (int i = 0; i < e1_discard.length; i++) {
            e1_discard[i] = new ArrayList<>();
            e2_discard[i] = new ArrayList<>();
            e3_discard[i] = new ArrayList<>();
            CardButton b1 = new CardButton();
            CardButton b2 = new CardButton();
            CardButton b3 = new CardButton();
            b1.setBounds(X_OP1 + i * X_OP1_DISTANCE + 55, Y_OP1, WIDTH_OP1, HEIGHT_OP1); //+45
            b2.setBounds(X_OP1 + i * X_OP1_DISTANCE + 55, Y_OP2, WIDTH_OP1, HEIGHT_OP1);
            b3.setBounds(X_OP1 + i * X_OP1_DISTANCE + 55, Y_OP3, WIDTH_OP1, HEIGHT_OP1);
            setClickable(b1, false);
            setClickable(b2, false);
            setClickable(b3, false);
            layeredPane.add(b1);
            layeredPane.add(b2);
            layeredPane.add(b3);
            e1_discard[i].add(b1);
            e2_discard[i].add(b2);
            e3_discard[i].add(b3);
        }

        //Displays number of own stock cards left. Name of label corresponds to the number of stock cards left.
        numOfStockCards = new JLabel();
        //numOfStockCards.setName(String.valueOf(initialNumStockCards));
        numOfStockCards.setBounds(X_STOCK, Y_STOCK+HEIGHT_STOCK+5, 100, 15);
        layeredPane.add(numOfStockCards);

        //creates JLabels for number of stock cards of opponents and sets their bounds. Further adjustments are done in
        //method setOpponentNames
        for (int i = 0; i < oppNumStockCards.length; i++) {
            oppNumStockCards[i] = new JLabel();
            oppNumStockCards[i].setBounds(X_OP1+(WIDTH_OP1/2-5), Y_OP1+HEIGHT_OP1+5 +i*140, 35, 15);
        }
/*            e1_discard[i] = new CardButton();
            e2_discard[i] = new CardButton();
            e3_discard[i] = new CardButton();
            layeredPane.add(e1_discard[i]);
            layeredPane.add(e2_discard[i]);
            layeredPane.add(e3_discard[i]);
            setClickable(e1_discard[i], false);
            setClickable(e2_discard[i], false);
            setClickable(e3_discard[i], false);*/


/*        e1_discard[0].setBounds(535, 50, 30, 50);
        e1_discard[1].setBounds(570, 50, 30, 50);
        e1_discard[2].setBounds(605, 50, 30, 50);
        e1_discard[3].setBounds(640, 50, 30, 50);*/

/*        e2_discard[0].setBounds(745, 50, 30, 50);
        e2_discard[1].setBounds(780, 50, 30, 50);
        e2_discard[2].setBounds(815, 50, 30, 50);
        e2_discard[3].setBounds(850, 50, 30, 50);

        e3_discard[0].setBounds(955, 50, 30, 50);
        e3_discard[1].setBounds(990, 50, 30, 50);
        e3_discard[2].setBounds(1025, 50, 30, 50);
        e3_discard[3].setBounds(1060, 50, 30, 50);*/

    }

    /**
     * Sets the opponents names and puts them in an array in the correct order (order of turns). Adds labels of number
     * of stock cards for opponents to layeredPane.
     *
     * @param names Array with names of opponents and own player name.
     */
    void setOpponentNames(String[] names) {
        oppArray = new JLabel[names.length];
        int i = 0;
        if (names[i].equals(playerName)) {
            i++;
        } else {
            e1.setForeground(ChatGraphic.DARKGREEN);
        }
        oppArray[i] = e1;
        e1.setText(names[i]);
        layeredPane.add(oppNumStockCards[0]);
        oppNumStockCards[0].setName(names[i]);
        i++;
        if (names.length > 2) {
            if (names[i].equals(playerName)) {
                i++;
            }
            e2.setText(names[i]);
            oppArray[i] = e2;
            layeredPane.add(oppNumStockCards[1]);
            oppNumStockCards[1].setName(names[i]);
            i++;
            if (names.length > 3) {
                if (names[i].equals(playerName)) {
                    i++;
                }
                e3.setText(names[i]);
                oppArray[i] = e3;
                layeredPane.add(oppNumStockCards[2]);
                oppNumStockCards[2].setName(names[i]);
            }
        }
    }

    /**
     * Sets own hand cards and stock card and sets the number of stock cards.
     *
     * @param colAndNum Array of colours and numbers of hand cards (5x alternating) and colour and number of stock card.
     */
    void setInitialCards(String[] colAndNum) {
        for (int i = 0, j = 0; i < hand.length; i++) {
            hand[i].setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j + 1]), CardIcons.MEDIUM));
            hand[i].addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
        }
        for (int i = 0, j = 10; i < (colAndNum.length - 10) / 3; i++) {
            CardButton stockCard = getEnemyButton(colAndNum[j]);
            j++;
            if (stockCard == null) {
                stock.setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j + 1]), CardIcons.LARGE));
                stock.addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
            } else {
                stockCard.setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j + 1]), CardIcons.SMALL));
                stockCard.addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
            }
        }

        //Displays number of own stock cards left. Name of label corresponds to the number of stock cards left.
        numOfStockCards.setText(initialNumStockCards + " cards left");
        numOfStockCards.setName(String.valueOf(initialNumStockCards));

        //creates JLabels for number of stock cards of opponents and sets their bounds. Further adjustments are done in
        //method setOpponentNames
        for (JLabel oppNumStockCard : oppNumStockCards) {
            oppNumStockCard.setText(String.valueOf(initialNumStockCards));
        }
    }

    private void setClickable(JButton button, boolean b) {
        button.setFocusPainted(b);
        if (b) {
            button.setModel(defaultButtonModel);
        } else {
            button.setModel(notClickableModel);
        }
    }

    void setButtonModel() {
        notClickableModel = new DefaultButtonModel() {
            public boolean isArmed() {
                return false;
            }

            public boolean isPressed() {
                return false;
            }
        };
    }

    /**
     * Plays a hand card to a discard pile
     *
     * @param i      Index of hand card
     * @param j      Index of discard pile
     * @param name   Player name of player who makes the move
     * @param colour Colour of hand card being moved
     * @param number Number of hand card being moved
     */
    void handToDiscard(int i, int j, String name, String colour, int number) {

        ArrayList<CardButton> al;
        CardButton newDisCard;
        if (name.equals(playerName)) {
            CardButton handCard = hand[i - 1];
            //CardButton discardCard = discard[j-1];
            String col = handCard.removeColour();
            int num = handCard.removeNumber();
            //discardCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            //discardCard.addCard(col, num);
            handCard.setIcon(null);
            clientListener.pw.println(Protocol.PUTTO + "§Update§D§" + i + "§" + j + "§" + name + "§" +
                    col + "§" + num);

            al = discard[j - 1];
            CardButton oldDisCard = al.get(al.size() - 1);
            newDisCard = new CardButton(CardButton.DISCARD);
            newDisCard.addActionListener(this);
            oldDisCard.removeActionListener(this);
            newDisCard.setName(" D " + j);
            setBoundsOfDiscard(newDisCard, al, DISTDISCARD);
/*            newDisCard.setBounds(al.get(0).getX(), al.get(0).getY() + (al.size() - 1) * 30,
                    al.get(0).getWidth(), al.get(0).getHeight());*/
            newDisCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            newDisCard.addCard(col, num);
            layeredPane.add(newDisCard, Integer.valueOf(al.size()));
            al.add(newDisCard);

            playerIndex = (playerIndex + 1) % oppArray.length;
            oppArray[playerIndex].setForeground(ChatGraphic.DARKGREEN);

        } else {
            al = getEnemyArray(name, j);
            newDisCard = new CardButton();
            setClickable(newDisCard, false);
            setBoundsOfDiscard(newDisCard, al, DISTOPPDISCARD);
            /*newDisCard.setBounds(al.get(0).getX(), al.get(0).getY() + (al.size() - 1) * 13, al.get(0).getWidth(),
                    al.get(0).getHeight());*/
            newDisCard.setIcon(cardIcons.getIcon(colour, number, CardIcons.SMALL));
            newDisCard.addCard(colour, number);
            layeredPane.add(newDisCard, Integer.valueOf(al.size()));
            al.add(newDisCard);
/*            CardButton discard =  getEnemyArray(name, j);
            discard.addCard(colour, number);
            discard.setIcon(cardIcons.getIcon(colour, number, CardIcons.SMALL));*/

            oppArray[playerIndex].setForeground(Color.BLACK);
            playerIndex = (playerIndex + 1) % oppArray.length;
            if (oppArray[playerIndex] != null) { //if this is null, it means that it's this players turn
                oppArray[playerIndex].setForeground(ChatGraphic.DARKGREEN);
            }
        }
    }

    private void setBoundsOfDiscard(CardButton newDisCard, ArrayList<CardButton> al, int distance) {
        if (newDisCard != null) { //Card will be added
            if (al.size() >= 6) {
                for (int i = al.size() - 4; i < al.size(); i++) {
                    al.get(i).setLocation(al.get(0).getX(), al.get(i).getY() - distance);
                }
            }
            if (al.size() == 1) {
                distance = 0;
            }
            newDisCard.setBounds(al.get(0).getX(), al.get(al.size() - 1).getY() + distance,
                    al.get(0).getWidth(), al.get(0).getHeight());
        } else { //Card will be removed
            if (al.size() >= 6) {
                for (int i = al.size()-4; i < al.size(); i++) {
                    al.get(i).setLocation(al.get(0).getX(), al.get(i).getY() + distance);
                }
            }
        }
    }

    /**
     * Plays a hand card to a build pile
     *
     * @param i      Index of hand card
     * @param j      Index of build pile
     * @param name   Player name of player who makes the move
     * @param colour Colour of hand card being moved
     * @param number Number of hand card being moved
     */
    void handToBuild(int i, int j, String name, String colour, int number) {
        if (name.equals(playerName)) {
            CardButton handCard = hand[i - 1];
            CardButton buildCard = build[j - 1];
            String col = handCard.removeColour();
            number = handCard.removeNumber();
            if (number == 13) {
                number = buildCard.getTopNumber() + 1;
            }
            buildCard.addCard(col, number);
            buildCard.setIcon(cardIcons.getIcon(col, number, CardIcons.LARGE));
            handCard.setIcon(null);
            clientListener.pw.println(Protocol.PUTTO + "§Update§B§" + i + "§" + j + "§" + name + "§" +
                    col + "§" + number);
        } else {
            clientLog.debug("set build for everyone");
            CardButton buildCard = build[j - 1];
/*            if (number == 13) {
                number = buildCard.getTopNumber()+1;
            }*/
            buildCard.addCard(colour, number);
            buildCard.setIcon(cardIcons.getIcon(colour, number, CardIcons.LARGE));
        }
        if (number == 12) {
            resetBuildPile(j - 1);
        }
    }

    // Play the stock card to a build pile. card1 = new stock card, card2 = new build card

    /**
     * Plays a stock card to a build pile
     *
     * @param j       Index of build pile
     * @param name    Player name of player who makes the move
     * @param colour1 Colour of the new stock card
     * @param number1 Number of the new stock card
     * @param colour2 Colour of the stock card being moved
     * @param number2 Number of the stock card being moved
     */
    void stockToBuild(int j, String name, String colour1, int number1, String colour2, int number2) {
        //clientLog.debug("(GameGraphic) entered stock to build method");
        if (name.equals(playerName)) {
            CardButton buildCard = build[j - 1];
            String col = stock.removeColour();
            number2 = stock.removeNumber();
            if (number2 == 13) {
                number2 = buildCard.getTopNumber() + 1;
            }
            buildCard.addCard(col, number2);
            buildCard.setIcon(cardIcons.getIcon(col, number2, CardIcons.LARGE));
            stock.setIcon(cardIcons.getIcon(colour1, number1, CardIcons.LARGE));
            stock.addCard(colour1, number1);

            numOfStockCards.setName(String.valueOf(Integer.parseInt(numOfStockCards.getName())-1));
            if (numOfStockCards.getName().equals("1")) {
                numOfStockCards.setText(1 + " card left");
            } else {
                numOfStockCards.setText(Integer.parseInt(numOfStockCards.getName()) + " cards left");
            }

            clientListener.pw.println(Protocol.PUTTO + "§Update§S§" + j + "§" + name + "§" +
                    colour1 + "§" + number1 + "§" + col + "§" + number2);
        } else {
            clientLog.debug("is updating build & stock from enemy");
            CardButton stockCard = getEnemyButton(name);
            CardButton buildCard = build[j - 1];
/*            if (number2 == 13) {
                number2 = buildCard.getTopNumber()+1;
            }*/
            buildCard.addCard(colour2, number2);
            buildCard.setIcon(cardIcons.getIcon(colour2, number2, CardIcons.LARGE));
            stockCard.addCard(colour1, number1);
            stockCard.setIcon(cardIcons.getIcon(colour1, number1, CardIcons.SMALL));
            JLabel l = getNumOfStockCardsLabel(name);
            l.setText(String.valueOf(Integer.parseInt(l.getText())-1));
        }
        if (number2 == 12) {
            resetBuildPile(j - 1);
        }
    }

    // Play from discard pile to a build pile

    /**
     * Plays a card from a discard pile to a build pile
     *
     * @param i    Index of discard pile
     * @param j    Index of build pile
     * @param name Player name of player who made the move
     */
    void discardToBuild(int i, int j, String name) {
        CardButton buildCard = build[j - 1];
        CardButton oldDisCard;
        ArrayList<CardButton> al;
        String col;
        int num;
        if (name.equals(playerName)) {
/*            discardCard = discard[i-1];
            String col = discardCard.removeColour();
            num = discardCard.removeNumber();
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            discardCard.setIcon(cardIcons.getIcon(discardCard.getTopColour(), discardCard.getTopNumber(), CardIcons.LARGE));*/
            clientListener.pw.println(Protocol.PUTTO + "§Update§" + i + "§" + j + "§" + name);

            al = discard[i - 1];
            oldDisCard = al.remove(al.size() - 1);
            layeredPane.remove(oldDisCard);
            col = oldDisCard.removeColour();
            num = oldDisCard.removeNumber();
            if (num == 13) {
                num = buildCard.getTopNumber() + 1;
            }
            buildCard.addCard(col, num);
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            al.get(al.size() - 1).addActionListener(this);
            setBoundsOfDiscard(null, al, DISTDISCARD);
            layeredPane.repaint();
        } else {
            al = getEnemyArray(name, i);
            oldDisCard = al.remove(al.size() - 1);
            layeredPane.remove(oldDisCard);
            col = oldDisCard.removeColour();
            num = oldDisCard.removeNumber();
            if (num == 13) {
                num = buildCard.getTopNumber() + 1;
            }
            buildCard.addCard(col, num);
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            setBoundsOfDiscard(null, al, DISTOPPDISCARD);
            layeredPane.repaint();
/*            discardCard = getEnemyArray(name, i);
            String col = discardCard.removeColour();
            num = discardCard.removeNumber();
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            discardCard.setIcon(cardIcons.getIcon(discardCard.getTopColour(), discardCard.getTopNumber(), CardIcons.SMALL));*/
        }
        if (num == 12) {
            resetBuildPile(j - 1);
        }
    }

    /*
     * dis -> build: 2x index, name = 3
     * stock -> build: 1x index, name, 2x Card = 6
     * hand -> build: 1x Pile, 2x index, name, 1x Card = 6
     * hand -> dis: 1x Pile, 2x index, name, 1x Card = 6
     * */

    void updateHandCards(String[] colours, int[] numbers) {
/*        clientLog.debug("(Graphic) length of colours = " + colours.length);
        clientLog.debug("(Graphic) length of numbers = " + numbers.length);*/
        for (int i = 0; i < colours.length; i++) {
            hand[i].setIcon(cardIcons.getIcon(colours[i], numbers[i], CardIcons.MEDIUM));
            hand[i].resetCards();
            hand[i].addCard(colours[i], numbers[i]);
            //clientLog.debug("updated hand card " + i);
        }
        for (int i = 4; i >= colours.length; i--) {
            hand[i].resetCards();
            hand[i].setIcon(null);
        }
    }

    /**
     * Removes all cards from a build pile
     *
     * @param i Index of build pile
     */
    void resetBuildPile(int i) {
        build[i].resetCards();
        build[i].setIcon(null);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (button1Pressed == null) {
            button1Pressed = (CardButton) actionEvent.getSource();
            if (button1Pressed.getIcon() == null) {
                button1Pressed = null;
                return;
            }
            button1Pressed.setBorder(clickedBorder);
            changeButtonStates(button1Pressed, false);
        } else if (button1Pressed == actionEvent.getSource()) {
            button1Pressed.setBorder(defaultBorder);
            changeButtonStates(button1Pressed, true);
            button1Pressed = null;
        } else {
            JButton button2Pressed = (JButton) actionEvent.getSource();
            String input = "/play" + button1Pressed.getName() + button2Pressed.getName();
            try {
                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                clientLog.warn("Error with /play command");
            }
            button1Pressed.setBorder(defaultBorder);
            changeButtonStates(button1Pressed, true);
            button1Pressed = null;
        }
    }

    //returns discard pile array of enemy
    ArrayList<CardButton> getEnemyArray(String name, int index) {
        ArrayList<CardButton> array = null;
        if (e1.getText().equals(name)) {
            array = e1_discard[index - 1];
        } else if (e2.getText().equals(name)) {
            array = e2_discard[index - 1];
        } else if (e3.getText().equals(name)) {
            array = e3_discard[index - 1];
        }
        return array;
    }

    //returns stock pile button of enemy
    CardButton getEnemyButton(String name) {
        CardButton button = null;
        if (e1.getText().equals(name)) {
            button = e1_stock;
        } else if (e2.getText().equals(name)) {
            button = e2_stock;
        } else if (e3.getText().equals(name)) {
            button = e3_stock;
        }
        return button;
    }

    JLabel getNumOfStockCardsLabel(String name) {
        for (JLabel oppNumStockCard : oppNumStockCards) {
            if (oppNumStockCard.getName().equals(name)) {
                return oppNumStockCard;
            }
        }
        return null;
    }

    //if true HandCards, StockCards and Discard are enabled; BuildCards are disabled

    /**
     * Enables or disables buttons of hand cards, stock cards and build cards
     *
     * @param button A button which is not getting disabled
     * @param b      If true, hand cards, stock cards and discard piles are enabled while the build cards are disabled. If
     *               false it is the other way round.
     */
    void changeButtonStates(CardButton button, boolean b) {
        if (button.getType() == CardButton.HAND) {
            for (CardButton cardButton : hand) {
                if (button != cardButton) {
                    cardButton.setEnabled(b);
                }
            }
            stock.setEnabled(b);
            for (CardButton cardButton : build) {
                if (b) {
                    cardButton.removeActionListener(this);
                } else {
                    cardButton.addActionListener(this);
                }
            }
        } else if (button.getType() == CardButton.DISCARD) {
            for (int i = 0; i < discard.length; i++) {
                for (CardButton cardButton : discard[i]) {
                    if (cardButton != button) {
                        cardButton.setEnabled(b);
                    }
                }
                if (b) {
                    build[i].removeActionListener(this);
                } else {
                    build[i].addActionListener(this);
                }
            }
            for (CardButton cardButton : hand) {
                cardButton.setEnabled(b);
            }
            stock.setEnabled(b);
        } else { //button is stock button
            for (CardButton cardButton : hand) {
                cardButton.setEnabled(b);
            }
            for (int i = 0; i < build.length; i++) {
                if (b) {
                    build[i].removeActionListener(this);
                } else {
                    build[i].addActionListener(this);
                }
                for (CardButton cardButton : discard[i]) {
                    cardButton.setEnabled(b);
                }
            }
        }
    }

    void setStockSize(int stockSize) {
        initialNumStockCards = stockSize;
    }

    JLayeredPane getGameComponent() {
        return layeredPane;
    }

}
