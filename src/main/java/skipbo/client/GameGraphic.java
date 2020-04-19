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

    private SBClientListener clientListener;
    private DefaultButtonModel notClickableModel;
    private DefaultButtonModel defaultButtonModel = new DefaultButtonModel();
    private CardButton button1Pressed = null;
    private Border defaultBorder = UIManager.getBorder("Button.border");
    private Border clickedBorder = BorderFactory.createLineBorder(Color.BLACK,2);
    private String playerName;
    private JLayeredPane layeredPane;

    //Opponents
    private JLabel e1;
    private JLabel e2;
    private JLabel e3;

    private JLabel[] oppArray;
    private int playerIndex = 0;

    //Own piles
    private CardButton[] hand = new CardButton[5];
    private CardButton stock;
    //private CardButton[] discard = new CardButton[4];
    private ArrayList<CardButton>[] discard = new ArrayList[4];

    //Game piles
    private CardButton[] build = new CardButton[4];

    //Opponent discard piles
    private ArrayList<CardButton>[] e1_discard = new ArrayList[4];
    private ArrayList<CardButton>[] e2_discard = new ArrayList[4];
    private ArrayList<CardButton>[] e3_discard = new ArrayList[4];
/*    private CardButton[] e1_discard = new CardButton[4];
    private CardButton[] e2_discard = new CardButton[4];
    private CardButton[] e3_discard = new CardButton[4];*/

    //Opponent stock piles
    private CardButton e1_stock;
    private CardButton e2_stock;
    private CardButton e3_stock;

    private CardIcons cardIcons = new CardIcons( 30, 50, 78, 120);

    private JTextPane chat;


    GameGraphic(SBClientListener clientListener, String name, JTextPane chat) {
        this.clientListener = clientListener;
        playerName = name;
        layeredPane = new JLayeredPane();
        this.chat = chat;
        layeredPane.setBounds(0, 0, 1150, 800);
        setButtonModel();
        appendDecks();
    }


    void appendDecks() {
        /*
        Cards of the Player and build piles
        */

        JLabel dp = new JLabel("Your Discard Piles");
        dp.setBounds(620,380,120,15);
        JLabel hp = new JLabel("Your hand cards");
        hp.setBounds(620,550,120,15);
        JLabel sp = new JLabel("Your stock pile");
        sp.setBounds(490, 380,120,15);
        JLabel bp = new JLabel("Build piles");
        bp.setBounds(620,130,120,15);
        layeredPane.add(dp);
        layeredPane.add(hp);
        layeredPane.add(sp);
        layeredPane.add(bp);

        // Discard and build Piles

        for (int i = 0, j = 1; i < discard.length; i++, j++) {
            //discard[i] = new CardButton(CardButton.DISCARD);
            build[i] = new CardButton(CardButton.BUILD);
            //layeredPane.add(discard[i]);
            layeredPane.add(build[i]);
            //discard[i].addActionListener(this);
            //build[i].addActionListener(this);
            //discard[i].setName(" D " + j);
            build[i].setName(" B " + j);
            discard[i] = new ArrayList<>();
            CardButton b = new CardButton(CardButton.DISCARD);
            b.setBounds(620+i*110, 400, 100, 145);
            b.addActionListener(this);
            b.setName(" D " + j);
            layeredPane.add(b);
            discard[i].add(b);

        }
/*        discard[0].setBounds(620, 400, 100, 145);
        discard[1].setBounds(730, 400, 100, 145);
        discard[2].setBounds(840, 400, 100, 145);
        discard[3].setBounds(950, 400, 100, 145);*/
        build[0].setBounds(620, 150, 100, 145);
        build[1].setBounds(730, 150, 100, 145);
        build[2].setBounds(840, 150, 100, 145);
        build[3].setBounds(950, 150, 100, 145);


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
        for (int i = 0; i < hand.length;) {
            hand[i] = new CardButton(CardButton.HAND);
            layeredPane.add(hand[i]);
            hand[i].addActionListener(this);
            hand[i].setName(" H " + ++i);
        }
        hand[0].setBounds(620, 570, 78, 120);
        hand[1].setBounds(708, 570, 78, 120);
        hand[2].setBounds(796, 570, 78, 120);
        hand[3].setBounds(884, 570, 78, 120);
        hand[4].setBounds(972, 570, 78, 120);


        //stock pile
        stock = new CardButton(CardButton.STOCK);
        stock.setBounds(490, 400, 100, 145);
        layeredPane.add(stock);
        stock.setName(" S 1");
        stock.addActionListener(this);

        /*
        Cards of the game (draw pile)
        */

        JLabel dpg = new JLabel("Draw pile");
        dpg.setBounds(490,130,120,15);
        layeredPane.add(dpg);

        // draw pile
        JButton draw = new JButton();
        draw.setBounds(490, 150, 100, 145);
        layeredPane.add(draw);
        ImageIcon back = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Back.png")));
        draw.setIcon(back);
        setClickable(draw, false);

        /*
        display cards of enemies
        */

        //Labels of enemies

        e1 = new JLabel("Opponent 1");
        e1.setBounds(490,30,120,15);
        layeredPane.add(e1);

        e2 = new JLabel("Opponent 2");
        e2.setBounds(700,30,120,15);
        layeredPane.add(e2);

        e3 = new JLabel("Opponent 3");
        e3.setBounds(910,30,120,15);
        layeredPane.add(e3);

        //Stock piles of enemies
        e1_stock = new CardButton();
        e1_stock.setBounds(490, 50, 30, 50);
        layeredPane.add(e1_stock);
        setClickable(e1_stock, false);

        e2_stock = new CardButton();
        e2_stock.setBounds(700, 50, 30, 50);
        layeredPane.add(e2_stock);
        setClickable(e2_stock, false);

        e3_stock = new CardButton();
        e3_stock.setBounds(910, 50, 30, 50);
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
            b1.setBounds(535+i*35, 50, 30, 50);
            b2.setBounds(745+i*35, 50, 30, 50);
            b3.setBounds(955+i*35, 50, 30, 50);
            setClickable(b1, false);
            setClickable(b2, false);
            setClickable(b3, false);
            layeredPane.add(b1);
            layeredPane.add(b2);
            layeredPane.add(b3);
            e1_discard[i].add(b1);
            e2_discard[i].add(b2);
            e3_discard[i].add(b3);

/*            e1_discard[i] = new CardButton();
            e2_discard[i] = new CardButton();
            e3_discard[i] = new CardButton();
            layeredPane.add(e1_discard[i]);
            layeredPane.add(e2_discard[i]);
            layeredPane.add(e3_discard[i]);
            setClickable(e1_discard[i], false);
            setClickable(e2_discard[i], false);
            setClickable(e3_discard[i], false);*/
        }

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
        i++;
        if (names.length > 2) {
            if (names[i].equals(playerName)) {
                i++;
            }
            e2.setText(names[i]);
            oppArray[i] = e2;
            i++;
            if (names.length > 3) {
                if (names[i].equals(playerName)) {
                    i++;
                }
                e3.setText(names[i]);
                oppArray[i] = e3;
            }
        }
    }

    void setInitialCards(String[] colAndNum) {
        for (int i = 0, j = 0; i < hand.length; i++) {
            hand[i].setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j+1]), CardIcons.MEDIUM));
            hand[i].addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
        }
        for (int i = 0, j = 10; i < (colAndNum.length - 10)/3; i++) {
            CardButton stockCard = getEnemyButton(colAndNum[j]);
            j++;
            if (stockCard == null) {
                stock.setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j+1]), CardIcons.LARGE));
                stock.addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
            } else {
                stockCard.setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j+1]), CardIcons.SMALL));
                stockCard.addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
            }
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
        notClickableModel =  new DefaultButtonModel() {
            public boolean isArmed() {
                return false;
            }
            public boolean isPressed() {
                return false;
            }
        };
    }


    // Play a hand card to the discard pile
    void handToDiscard(int i, int j, String name, String colour, int number) {

        ArrayList<CardButton> al;
        CardButton newDisCard;
        if (name.equals(playerName)) {
            CardButton handCard = hand[i-1];
            //CardButton discardCard = discard[j-1];
            String col = handCard.removeColour();
            int num = handCard.removeNumber();
            //discardCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            //discardCard.addCard(col, num);
            handCard.setIcon(null);
            clientListener.pw.println(Protocol.PUTTO + "§Update§D§" + i + "§" + j + "§" + name + "§" +
                    col + "§" + num);

            al = discard[j-1];
            CardButton oldDisCard = al.get(al.size()-1);
            newDisCard = new CardButton(CardButton.DISCARD);
            newDisCard.addActionListener(this);
            oldDisCard.removeActionListener(this);
            newDisCard.setName(" D " + j);
            newDisCard.setBounds(al.get(0).getX(), al.get(0).getY()+(al.size()-1)*15,
                    al.get(0).getWidth(), al.get(0).getHeight());
            newDisCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            newDisCard.addCard(col, num);
            layeredPane.add(newDisCard, new Integer(al.size()));
            al.add(newDisCard);

            playerIndex = (playerIndex+1)%oppArray.length;
            oppArray[playerIndex].setForeground(ChatGraphic.DARKGREEN);

        } else {
            al = getEnemyArray(name, j);
            newDisCard = new CardButton();
            setClickable(newDisCard, false);
            newDisCard.setBounds(al.get(0).getX(), al.get(0).getY()+(al.size()-1)*10, al.get(0).getWidth(),
                    al.get(0).getHeight());
            newDisCard.setIcon(cardIcons.getIcon(colour, number, CardIcons.SMALL));
            newDisCard.addCard(colour, number);
            layeredPane.add(newDisCard, new Integer(al.size()));
            al.add(newDisCard);
/*            CardButton discard =  getEnemyArray(name, j);
            discard.addCard(colour, number);
            discard.setIcon(cardIcons.getIcon(colour, number, CardIcons.SMALL));*/

            oppArray[playerIndex].setForeground(Color.BLACK);
            playerIndex = (playerIndex+1)%oppArray.length;
            if (oppArray[playerIndex] != null) { //if this is null, it means that it's this players turn
                oppArray[playerIndex].setForeground(ChatGraphic.DARKGREEN);
            }
        }
    }

    // Play a hand card to build pile
    void handToBuild(int i, int j, String name, String colour, int number) {
        if (name.equals(playerName)) {
            CardButton handCard = hand[i-1];
            CardButton buildCard = build[j-1];
            String col = handCard.removeColour();
            int num = handCard.removeNumber();
            if (num == 13) {
                num = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(col, num);
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            handCard.setIcon(null);
            clientListener.pw.println(Protocol.PUTTO + "§Update§B§" + i + "§" + j + "§" + name + "§" +
                    col + "§" + num);
        } else {
            clientLog.debug("set build for everyone");
            CardButton buildCard = build[j-1];
            if (number == 13) {
                number = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(colour, number);
            buildCard.setIcon(cardIcons.getIcon(colour, number, CardIcons.LARGE));
        }
    }

    // Play the stock card to a build pile. card1 = new stock card, card2 = new build card
    void stockToBuild(int j, String name, String colour1, int number1, String colour2, int number2) {
        //clientLog.debug("(GameGraphic) entered stock to build method");
        if (name.equals(playerName)) {
            CardButton buildCard = build[j-1];
            String col = stock.removeColour();
            number2 = stock.removeNumber();
            if (number2 == 13) {
                number2 = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(col, number2);
            buildCard.setIcon(cardIcons.getIcon(col, number2, CardIcons.LARGE));
            stock.setIcon(cardIcons.getIcon(colour1, number1, CardIcons.LARGE));
            stock.addCard(colour1, number1);
            clientListener.pw.println(Protocol.PUTTO + "§Update§S§" + j + "§" + name + "§" +
                    colour1 + "§" + number1 + "§" + col + "§" + number2);
        } else {
            clientLog.debug("is updating build & stock from enemy");
            CardButton stockCard = getEnemyButton(name);
            CardButton buildCard = build[j-1];
            if (number2 == 13) {
                number2 = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(colour2, number2);
            buildCard.setIcon(cardIcons.getIcon(colour2, number2, CardIcons.LARGE));
            stockCard.addCard(colour1, number1);
            stockCard.setIcon(cardIcons.getIcon(colour1, number1, CardIcons.SMALL));
        }
        if (number2 == 12) {
            resetBuildPile(j-1);
        }
    }

    // Play from discard pile to a build pile
    void discardToBuild(int i, int j, String name) {
        CardButton buildCard = build[j-1];
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
            clientListener.pw.println(Protocol.PUTTO +"§Update§" + i + "§" + j + "§" + name);

            al = discard[i-1];
            oldDisCard = al.remove(al.size()-1);
            layeredPane.remove(oldDisCard);
            col = oldDisCard.removeColour();
            num = oldDisCard.removeNumber();
            if (num == 13) {
                num = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(col, num);
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            al.get(al.size()-1).addActionListener(this);
            layeredPane.repaint();
        } else {
            al = getEnemyArray(name, i);
            oldDisCard = al.remove(al.size()-1);
            layeredPane.remove(oldDisCard);
            col = oldDisCard.removeColour();
            num = oldDisCard.removeNumber();
            if (num == 13) {
                num = buildCard.getTopNumber()+1;
            }
            buildCard.addCard(col, num);
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            layeredPane.repaint();
/*            discardCard = getEnemyArray(name, i);
            String col = discardCard.removeColour();
            num = discardCard.removeNumber();
            buildCard.setIcon(cardIcons.getIcon(col, num, CardIcons.LARGE));
            discardCard.setIcon(cardIcons.getIcon(discardCard.getTopColour(), discardCard.getTopNumber(), CardIcons.SMALL));*/
        }
        if (num == 12) {
            resetBuildPile(j-1);
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
            clientLog.debug("updated hand card " + i);
        }
        for (int i = 4; i >= colours.length; i--) {
            hand[i].resetCards();
            hand[i].setIcon(null);
        }
    }

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
            array = e1_discard[index-1];
        } else if (e2.getText().equals(name)) {
            array = e2_discard[index-1];
        } else if (e3.getText().equals(name)) {
            array = e3_discard[index-1];
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

    //if true HandCards, StockCards and Discard are enabled; BuildCards are disabled
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



    JLayeredPane getGameComponent() {
        return layeredPane;
    }

}
