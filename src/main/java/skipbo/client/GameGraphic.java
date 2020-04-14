package skipbo.client;

import skipbo.game.Game;
import skipbo.game.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Will be class for the Game GUI in the future
 */
public class GameGraphic extends JButton implements ActionListener {

    private ChatGraphic chatGraphic;
    private Player player;
    private Game game;
    private DefaultButtonModel notClickableModel;
    private DefaultButtonModel defaultButtonModel = new DefaultButtonModel();
    private CardButton button1Pressed = null;
    private Border defaultBorder = UIManager.getBorder("Button.border");
    private Border clickedBorder = BorderFactory.createLineBorder(Color.BLACK,2);

    //Opponents
    private JLabel e1;
    private JLabel e2;
    private JLabel e3;

    //Own piles
    private CardButton[] hand = new CardButton[5];
    private CardButton stock;
    private CardButton[] discard = new CardButton[4];

    //Game piles
    private CardButton[] build = new CardButton[4];

    //Opponent discard piles
    private CardButton[] e1_discard = new CardButton[4];
    private CardButton[] e2_discard = new CardButton[4];
    private CardButton[] e3_discard = new CardButton[4];

    //Opponent stock piles
    private CardButton e1_stock;
    private CardButton e2_stock;
    private CardButton e3_stock;

    private CardIcons cardIcons = new CardIcons( 30, 50, 78, 120);

    GameGraphic(ChatGraphic chatGraphic) {
        this.chatGraphic = chatGraphic;
        setButtonModel();
    }

    void setGameGraphic() {

        chatGraphic.setTitle("Skip-Bros GAME");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        chatGraphic.setBounds(100, 100, 1150, 800);
        chatGraphic.setLocationRelativeTo(null);
        appendDecks();

        chatGraphic.setVisible(true);
        chatGraphic.repaint();
/*
        this.player = player;
        this.game = game;
        */
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
        chatGraphic.getContentPane().add(dp);
        chatGraphic.getContentPane().add(hp);
        chatGraphic.getContentPane().add(sp);
        chatGraphic.getContentPane().add(bp);

        // Discard and build Piles

        for (int i = 0, j = 1; i < discard.length; i++, j++) {
            discard[i] = new CardButton();
            build[i] = new CardButton();
            chatGraphic.getContentPane().add(discard[i]);
            chatGraphic.getContentPane().add(build[i]);
            discard[i].addActionListener(this);
            build[i].addActionListener(this);
            discard[i].setName(" D " + j);
            build[i].setName(" B " + j);
        }
        discard[0].setBounds(620, 400, 100, 145);
        discard[1].setBounds(730, 400, 100, 145);
        discard[2].setBounds(840, 400, 100, 145);
        discard[3].setBounds(950, 400, 100, 145);
        build[0].setBounds(620, 150, 100, 145);
        build[1].setBounds(730, 150, 100, 145);
        build[2].setBounds(840, 150, 100, 145);
        build[3].setBounds(950, 150, 100, 145);


        // hand piles
        for (int i = 0; i < hand.length;) {
            hand[i] = new CardButton();
            chatGraphic.getContentPane().add(hand[i]);
            hand[i].addActionListener(this);
            hand[i].setName(" H " + ++i);
        }
        hand[0].setBounds(620, 570, 78, 120);
        hand[1].setBounds(708, 570, 78, 120);
        hand[2].setBounds(796, 570, 78, 120);
        hand[3].setBounds(884, 570, 78, 120);
        hand[4].setBounds(972, 570, 78, 120);


        //stock pile
        stock = new CardButton();
        stock.setBounds(490, 400, 100, 145);
        chatGraphic.getContentPane().add(stock);
        stock.setName(" S 1");
        stock.addActionListener(this);

        /*
        Cards of the game (draw pile)
        */

        JLabel dpg = new JLabel("Draw pile");
        dpg.setBounds(490,130,120,15);
        chatGraphic.getContentPane().add(dpg);

        // draw pile
        JButton draw = new JButton();
        draw.setBounds(490, 150, 100, 145);
        chatGraphic.getContentPane().add(draw);
        ImageIcon back = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Back.png")));
        draw.setIcon(back);
        setClickable(draw, false);

        /*
        display cards of enemies
        */

        //Labels of enemies

        e1 = new JLabel("Opponent 1");
        e1.setBounds(490,30,120,15);
        chatGraphic.getContentPane().add(e1);

        e2 = new JLabel("Opponent 2");
        e2.setBounds(700,30,120,15);
        chatGraphic.getContentPane().add(e2);

        e3 = new JLabel("Opponent 3");
        e3.setBounds(910,30,120,15);
        chatGraphic.getContentPane().add(e3);

        //Stock piles of enemies
        e1_stock = new CardButton();
        e1_stock.setBounds(490, 50, 30, 50);
        chatGraphic.getContentPane().add(e1_stock);
        setClickable(e1_stock, false);

        e2_stock = new CardButton();
        e2_stock.setBounds(700, 50, 30, 50);
        chatGraphic.getContentPane().add(e2_stock);
        setClickable(e2_stock, false);

        e3_stock = new CardButton();
        e3_stock.setBounds(910, 50, 30, 50);
        chatGraphic.getContentPane().add(e3_stock);
        setClickable(e3_stock, false);


        //Discard piles of enemies

        for (int i = 0; i < e1_discard.length; i++) {
            e1_discard[i] = new CardButton();
            e2_discard[i] = new CardButton();
            e3_discard[i] = new CardButton();
            chatGraphic.getContentPane().add(e1_discard[i]);
            chatGraphic.getContentPane().add(e2_discard[i]);
            chatGraphic.getContentPane().add(e3_discard[i]);
            setClickable(e1_discard[i], false);
            setClickable(e2_discard[i], false);
            setClickable(e3_discard[i], false);
        }

        e1_discard[0].setBounds(535, 50, 30, 50);
        e1_discard[1].setBounds(570, 50, 30, 50);
        e1_discard[2].setBounds(605, 50, 30, 50);
        e1_discard[3].setBounds(640, 50, 30, 50);

        e2_discard[0].setBounds(745, 50, 30, 50);
        e2_discard[1].setBounds(780, 50, 30, 50);
        e2_discard[2].setBounds(815, 50, 30, 50);
        e2_discard[3].setBounds(850, 50, 30, 50);

        e3_discard[0].setBounds(955, 50, 30, 50);
        e3_discard[1].setBounds(990, 50, 30, 50);
        e3_discard[2].setBounds(1025, 50, 30, 50);
        e3_discard[3].setBounds(1060, 50, 30, 50);

    }

    void setOpponentNames(String[] names) {
        int i = 0;
        if (names[i].equals(chatGraphic.playerName)) {
            i++;
        }
        e1.setText(names[i]);
        if (names.length > 2) {
            if (names[i].equals(chatGraphic.playerName)) {
                i++;
            }
            e2.setText(names[i]);
            if (names.length > 3) {
                if (names[i].equals(chatGraphic.playerName)) {
                    i++;
                }
                e3.setText(names[i]);
            }
        }
    }

    void setInitialCards(String[] colAndNum) {

        for (int i = 0, j = 0; i < hand.length; i++) {
            hand[i].setIcon(cardIcons.getIcon(colAndNum[j], Integer.parseInt(colAndNum[j+1]), "M"));
            hand[i].addCard(colAndNum[j++], Integer.parseInt(colAndNum[j++]));
        }
        stock.setIcon(cardIcons.getIcon(colAndNum[10], Integer.parseInt(colAndNum[11]), "L"));
        stock.addCard(colAndNum[10], Integer.parseInt(colAndNum[11]));
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

    void displayTestCard() {
        //Icon card = new ImageIcon("")
    }

    //TODO: update cards when an enemy  plays a card

    // Play a hand card to the discard pile
    void handToDiscard(int i, int j, String name, String colour, int number) {
        if (name.equals(chatGraphic.playerName)) {
            CardButton handCard = hand[i - 1];
            CardButton discardCard = discard[j - 1];

            String col = handCard.removeColour();
            int num = handCard.removeNumber();
            discardCard.setIcon(cardIcons.getIcon(col, num, "L"));
            discardCard.addCard(col, num);
            handCard.setIcon(null);
        }
    }

    // Play a hand card to build pile
    void handToBuild(int i, int j, String name, String colour, int number) {
        if (name.equals(chatGraphic.playerName)) {
            CardButton handCard = hand[i-1];
            CardButton buildCard = build[j-1];

            buildCard.setIcon(cardIcons.getIcon(handCard.removeColour(), handCard.removeNumber(), "L"));
            handCard.setIcon(null);
        }
    }

    // Play the stock card to a build pile
    void stockToBuild(int i, int j, String name, String colour1, int number1, String colour2, int number2) {
        if (name.equals(chatGraphic.playerName)) {
            CardButton buildCard = build[j-1];
            buildCard.setIcon(cardIcons.getIcon(stock.removeColour(), stock.removeNumber(), "L"));
            stock.setIcon(cardIcons.getIcon(colour1, number1, "L"));
            stock.addCard(colour1, number1);
        }
    }
    // Play from discard pile to a build pile
    void discardToBuild(int i, int j, String name, String colour, int number) {
        if (name.equals(chatGraphic.playerName)) {
            CardButton discardCard = discard[i-1];
            CardButton buildCard = build[j-1];
            String col = discardCard.removeColour();
            int num = discardCard.removeNumber();
            buildCard.setIcon(cardIcons.getIcon(col, num, "L"));
            discardCard.setIcon(cardIcons.getIcon(discardCard.getTopColour(), discardCard.getTopNumber(), "L"));
        }
    }

    void updateHandCards(String[] colours, int[] numbers) {
        for (int i = 0; i < 5; i++) {
            hand[i].setIcon(cardIcons.getIcon(colours[i], numbers[i], "M"));
            hand[i].removeColour();
            hand[i].removeNumber();
            hand[i].addCard(colours[i], numbers[i]);
        }
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (button1Pressed == null) {
            button1Pressed = (CardButton) actionEvent.getSource();
            button1Pressed.setBorder(clickedBorder);
        } else if (button1Pressed == actionEvent.getSource()) {
            button1Pressed.setBorder(defaultBorder);
            button1Pressed = null;
        } else {
            JButton button2Pressed = (JButton) actionEvent.getSource();
            String input = "/play" + button1Pressed.getName() + button2Pressed.getName();
            try {
                chatGraphic.getClientListener().forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                chatGraphic.printErrorMessage(e.getMessage());
            }
            button1Pressed.setBorder(defaultBorder);
            button1Pressed = null;
        }
    }

    //returns discard pile button of enemy
    CardButton getEnemyButton(String name, int index) {
        CardButton button;
        if (e1.getName().equals(name)) {
            button = e1_discard[index-1];
        } else if (e2.getName().equals(name)) {
            button = e2_discard[index-1];
        } else {
            button = e3_discard[index-1];
        }
        return button;
    }

    //returns stock pile button of enemy
    CardButton getEnemyButton(String name) {
        CardButton button;
        if (e1.getName().equals(name)) {
            button = e1_stock;
        } else if (e2.getName().equals(name)) {
            button = e2_stock;
        } else {
            button = e3_stock;
        }
        return button;
    }

}
