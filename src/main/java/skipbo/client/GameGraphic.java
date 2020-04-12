package skipbo.client;

import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.server.Protocol;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static skipbo.client.SBClient.clientLog;

/**
 * Will be class for the Game GUI in the future
 */
public class GameGraphic extends JButton implements ActionListener {

    private ChatGraphic chatGraphic;
    private JTextArea chat;
    private Player player;
    private Game game;
    private DefaultButtonModel notClickableModel;
    private DefaultButtonModel defaultButtonModel = new DefaultButtonModel();
    private JButton button1Pressed = null;
    private Border defaultBorder = UIManager.getBorder("Button.border");
    private Border clickedBorder = BorderFactory.createLineBorder(Color.BLACK,2);

    private JLabel e1;
    private JLabel e2;
    private JLabel e3;

    private JButton hand_a;
    private JButton hand_b;
    private JButton hand_c;
    private JButton hand_d;
    private JButton hand_e;
    private JButton stock;
    private JButton build_a;
    private JButton build_b;
    private JButton build_c;
    private JButton build_d;
    private JButton e1_a;
    private JButton e1_b;
    private JButton e1_c;
    private JButton e1_d;
    private JButton e2_a;
    private JButton e2_b;
    private JButton e2_c;
    private JButton e2_d;
    private JButton e3_a;
    private JButton e3_b;
    private JButton e3_c;
    private JButton e3_d;

    GameGraphic(ChatGraphic chatGraphic) {
        this.chatGraphic = chatGraphic;
        setButtonModel();
    }

    void setGameGraphic() {

        // Logo upper left corner
        ImageIcon logoI = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
        Image image = logoI.getImage().getScaledInstance(250, 70, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        JTextPane logoJ = new JTextPane();
        logoJ.setBorder(null);
        logoJ.setEditable(false);
        logoJ.setBounds(80,30, scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
        logoJ.setPreferredSize(new Dimension(scaledIcon.getIconWidth(), scaledIcon.getIconHeight()));
        logoJ.insertIcon(scaledIcon);
        chatGraphic.getContentPane().add(logoJ);

        chatGraphic.setTitle("Skip-Bros GAME");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        chatGraphic.setBounds(100, 100, 1150, 800);
        chatGraphic.setLocationRelativeTo(null);
        appendDecks();

        chatGraphic.setVisible(true);
        chatGraphic.repaint();


        this.player = player;
        this.game = game;

    }


    void appendDecks() {
        /*
        Cards of the Player
        */

        JLabel dp = new JLabel("Your Discard Piles");
        dp.setBounds(620,380,120,15);
        JLabel hp = new JLabel("Your hand cards");
        hp.setBounds(620,550,120,15);
        JLabel sp = new JLabel("Your stock pile");
        sp.setBounds(490, 380,120,15);
        chatGraphic.getContentPane().add(dp);
        chatGraphic.getContentPane().add(hp);
        chatGraphic.getContentPane().add(sp);

        // Discard Piles
        ImageIcon card = new ImageIcon(getClass().getClassLoader().getResource("R1.png"));
        Image cardSc = card.getImage().getScaledInstance(100, 145, Image.SCALE_DEFAULT);
        ImageIcon scaledCard = new ImageIcon(cardSc);

        JButton discard_A = new JButton(scaledCard);
        discard_A.setBounds(620, 400, 100, 145);
        JButton discard_B = new JButton();
        discard_B.setBounds(730, 400, 100, 145);
        JButton discard_C = new JButton();
        discard_C.setBounds(840, 400, 100, 145);
        JButton discard_D = new JButton();
        discard_D.setBounds(950, 400, 100, 145);
        chatGraphic.getContentPane().add(discard_A);
        chatGraphic.getContentPane().add(discard_B);
        chatGraphic.getContentPane().add(discard_C);
        chatGraphic.getContentPane().add(discard_D);
        discard_A.setName(" D 1");
        discard_B.setName(" D 2");
        discard_C.setName(" D 3");
        discard_D.setName(" D 4");
        discard_A.addActionListener(this);
        discard_B.addActionListener(this);
        discard_C.addActionListener(this);
        discard_D.addActionListener(this);

        // hand piles
        hand_a = new JButton();
        hand_a.setBounds(620, 570, 78, 120);
        hand_b = new JButton();
        hand_b.setBounds(708, 570, 78, 120);
        hand_c = new JButton();
        hand_c.setBounds(796, 570, 78, 120);
        hand_d = new JButton();
        hand_d.setBounds(884, 570, 78, 120);
        hand_e = new JButton();
        hand_e.setBounds(972, 570, 78, 120);

        chatGraphic.getContentPane().add(hand_a);
        chatGraphic.getContentPane().add(hand_b);
        chatGraphic.getContentPane().add(hand_c);
        chatGraphic.getContentPane().add(hand_d);
        chatGraphic.getContentPane().add(hand_e);
        hand_a.setName(" H 1");
        hand_b.setName(" H 2");
        hand_c.setName(" H 3");
        hand_d.setName(" H 4");
        hand_e.setName(" H 5");
        hand_a.addActionListener(this);
        hand_b.addActionListener(this);
        hand_c.addActionListener(this);
        hand_d.addActionListener(this);
        hand_e.addActionListener(this);

        //stock pile
        stock = new JButton();
        stock.setBounds(490, 400, 100, 145);
        chatGraphic.getContentPane().add(stock);
        stock.setName(" S 1");
        stock.addActionListener(this);

        /*
        Cards of the game
        */

        JLabel bp = new JLabel("Build piles");
        bp.setBounds(620,130,120,15);
        JLabel dpg = new JLabel("Main stock pile");
        dpg.setBounds(490,130,120,15);
        chatGraphic.getContentPane().add(bp);
        chatGraphic.getContentPane().add(dpg);

        // Build piles
        build_a = new JButton();
        build_a.setBounds(620, 150, 100, 145);
        build_b = new JButton();
        build_b.setBounds(730, 150, 100, 145);
        build_c = new JButton();
        build_c.setBounds(840, 150, 100, 145);
        build_d = new JButton();
        build_d.setBounds(950, 150, 100, 145);

        chatGraphic.getContentPane().add(build_a);
        chatGraphic.getContentPane().add(build_b);
        chatGraphic.getContentPane().add(build_c);
        chatGraphic.getContentPane().add(build_d);
        build_a.setName(" B 1");
        build_b.setName(" B 2");
        build_c.setName(" B 3");
        build_d.setName(" B 4");
        build_a.addActionListener(this);
        build_b.addActionListener(this);
        build_c.addActionListener(this);
        build_d.addActionListener(this);

        // draw piles
        JButton draw = new JButton();
        draw.setBounds(490, 150, 100, 145);
        chatGraphic.getContentPane().add(draw);
        draw.setIcon(scaledCard);
        setClickable(draw, false);

        /*
        display cards of enemies
        */

        // enemy 1
        e1 = new JLabel("Opponent 1");
        e1.setBounds(490,30,120,15);
        chatGraphic.getContentPane().add(e1);

        e1_a = new JButton();
        e1_a.setBounds(490, 50, 30, 50);
        e1_b = new JButton();
        e1_b.setBounds(525, 50, 30, 50);
        e1_c = new JButton();
        e1_c.setBounds(560, 50, 30, 50);
        e1_d = new JButton();
        e1_d.setBounds(595, 50, 30, 50);

        chatGraphic.getContentPane().add(e1_a);
        chatGraphic.getContentPane().add(e1_b);
        chatGraphic.getContentPane().add(e1_c);
        chatGraphic.getContentPane().add(e1_d);
        setClickable(e1_a, false);
        setClickable(e1_b, false);
        setClickable(e1_c, false);
        setClickable(e1_d, false);

        // enemy 2
        e2 = new JLabel("Opponent 2");
        e2.setBounds(650,30,120,15);
        chatGraphic.getContentPane().add(e2);

        e2_a = new JButton();
        e2_a.setBounds(650, 50, 30, 50);
        e2_b = new JButton();
        e2_b.setBounds(685, 50, 30, 50);
        e2_c = new JButton();
        e2_c.setBounds(720, 50, 30, 50);
        e2_d = new JButton();
        e2_d.setBounds(755, 50, 30, 50);

        chatGraphic.getContentPane().add(e2_a);
        chatGraphic.getContentPane().add(e2_b);
        chatGraphic.getContentPane().add(e2_c);
        chatGraphic.getContentPane().add(e2_d);
        setClickable(e2_a, false);
        setClickable(e2_b, false);
        setClickable(e2_c, false);
        setClickable(e2_d, false);

        // enemy 3
        e3 = new JLabel("Opponent 3");
        e3.setBounds(810,30,120,15);
        chatGraphic.getContentPane().add(e3);

        e3_a = new JButton();
        e3_a.setBounds(810, 50, 30, 50);
        e3_b = new JButton();
        e3_b.setBounds(845, 50, 30, 50);
        e3_c = new JButton();
        e3_c.setBounds(880, 50, 30, 50);
        e3_d = new JButton();
        e3_d.setBounds(915, 50, 30, 50);

        chatGraphic.getContentPane().add(e3_a);
        chatGraphic.getContentPane().add(e3_b);
        chatGraphic.getContentPane().add(e3_c);
        chatGraphic.getContentPane().add(e3_d);
        setClickable(e3_a, false);
        setClickable(e3_b, false);
        setClickable(e3_c, false);
        setClickable(e3_d, false);

    }

    void setOpponentNames(String[] names) {
        e1.setText(names[0]);
        e2.setText(names[1]);
        if (names.length > 2) {
            e3.setText(names[3]);
        }
    }

    void setInitialCards(String[] colAndNum) {
        JButton[] handCards = new JButton[] {hand_a, hand_b, hand_c, hand_d, hand_e};
        ImageIcon card;
        Image image;
        int i = 0;
        for (int cardIndex = 0; cardIndex < handCards.length; cardIndex++) {
            card = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(colAndNum[i] + colAndNum[i] + 1)));
            image = card.getImage().getScaledInstance(78, 120, Image.SCALE_DEFAULT);
            ImageIcon scaledCard = new ImageIcon(image);
            handCards[cardIndex].setIcon(scaledCard);
            i = i + 2;
        }
        card = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(colAndNum[i] + colAndNum[i] + 1)));
        image = card.getImage().getScaledInstance(100, 145, Image.SCALE_DEFAULT);
        ImageIcon scaledCard = new ImageIcon(image);
        stock.setIcon(scaledCard);
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

    // Play a hand card to build pile
    public void handToBuild(){

    }
    // Play a hand card to the discard pile
    public void handToDiscard() {

    }
    // draw a card from the stock pile
    public void drawFromStock(){

    }
    // draw a card from the draw pile
    public void drawFromDraw(){

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (button1Pressed == null) {
            button1Pressed = (JButton) actionEvent.getSource();
            button1Pressed.setBorder(clickedBorder);
        } else if (button1Pressed == actionEvent.getSource()) {
            setClickable(button1Pressed, true);
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
            setClickable(button1Pressed, true);
            button1Pressed.setBorder(defaultBorder);
            button1Pressed = null;
        }
    }

}
