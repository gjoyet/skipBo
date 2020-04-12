package skipbo.client;

import skipbo.game.Game;
import skipbo.game.Player;
import skipbo.server.Protocol;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static skipbo.client.SBClient.clientLog;

/**
 * Will be class for the Game GUI in the future
 */
public class GameGraphic extends JButton implements ActionListener {

    ChatGraphic chatGraphic;
    private JTextArea chat;
    private Player player;
    private Game game;
    private DefaultButtonModel notClickableModel;
    private DefaultButtonModel defaultButtonModel = new DefaultButtonModel();
    JButton button1Pressed = null;
    JButton button2Pressed;
    Border defaultBorder = UIManager.getBorder("Button.border");
    Border clickedBorder = BorderFactory.createLineBorder(Color.BLACK,2);

    GameGraphic(ChatGraphic chatGraphic) {
        this.chatGraphic = chatGraphic;
        setButtonModel();
        //appendDecks();
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
        JButton hand_A = new JButton();
        hand_A.setBounds(620, 570, 78, 120);
        JButton hand_B = new JButton();
        hand_B.setBounds(708, 570, 78, 120);
        JButton hand_C = new JButton();
        hand_C.setBounds(796, 570, 78, 120);
        JButton hand_D = new JButton();
        hand_D.setBounds(884, 570, 78, 120);
        JButton hand_E = new JButton();
        hand_E.setBounds(972, 570, 78, 120);

        chatGraphic.getContentPane().add(hand_A);
        chatGraphic.getContentPane().add(hand_B);
        chatGraphic.getContentPane().add(hand_C);
        chatGraphic.getContentPane().add(hand_D);
        chatGraphic.getContentPane().add(hand_E);
        hand_A.setName(" H 1");
        hand_B.setName(" H 2");
        hand_C.setName(" H 3");
        hand_D.setName(" H 4");
        hand_E.setName(" H 5");
        hand_A.addActionListener(this);
        hand_B.addActionListener(this);
        hand_C.addActionListener(this);
        hand_D.addActionListener(this);
        hand_E.addActionListener(this);

        //stock pile
        JButton stock = new JButton();
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
        JButton build_A = new JButton();
        build_A.setBounds(620, 150, 100, 145);
        JButton build_B = new JButton();
        build_B.setBounds(730, 150, 100, 145);
        JButton build_C = new JButton();
        build_C.setBounds(840, 150, 100, 145);
        JButton build_D = new JButton();
        build_D.setBounds(950, 150, 100, 145);

        chatGraphic.getContentPane().add(build_A);
        chatGraphic.getContentPane().add(build_B);
        chatGraphic.getContentPane().add(build_C);
        chatGraphic.getContentPane().add(build_D);
        build_A.setName(" B 1");
        build_B.setName(" B 2");
        build_C.setName(" B 3");
        build_D.setName(" B 4");
        build_A.addActionListener(this);
        build_B.addActionListener(this);
        build_C.addActionListener(this);
        build_D.addActionListener(this);

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
        JLabel e1 = new JLabel("Opponent 1");
        e1.setBounds(490,30,120,15);
        chatGraphic.getContentPane().add(e1);

        JButton E1_A = new JButton();
        E1_A.setBounds(490, 50, 30, 50);
        JButton E1_B = new JButton();
        E1_B.setBounds(525, 50, 30, 50);
        JButton E1_C = new JButton();
        E1_C.setBounds(560, 50, 30, 50);
        JButton E1_D = new JButton();
        E1_D.setBounds(595, 50, 30, 50);

        chatGraphic.getContentPane().add(E1_A);
        chatGraphic.getContentPane().add(E1_B);
        chatGraphic.getContentPane().add(E1_C);
        chatGraphic.getContentPane().add(E1_D);
        setClickable(E1_A, false);
        setClickable(E1_B, false);
        setClickable(E1_C, false);
        setClickable(E1_D, false);

        // enemy 2
        JLabel e2 = new JLabel("Opponent 2");
        e2.setBounds(650,30,120,15);
        chatGraphic.getContentPane().add(e2);

        JButton E2_A = new JButton();
        E2_A.setBounds(650, 50, 30, 50);
        JButton E2_B = new JButton();
        E2_B.setBounds(685, 50, 30, 50);
        JButton E2_C = new JButton();
        E2_C.setBounds(720, 50, 30, 50);
        JButton E2_D = new JButton();
        E2_D.setBounds(755, 50, 30, 50);

        chatGraphic.getContentPane().add(E2_A);
        chatGraphic.getContentPane().add(E2_B);
        chatGraphic.getContentPane().add(E2_C);
        chatGraphic.getContentPane().add(E2_D);
        setClickable(E2_A, false);
        setClickable(E2_B, false);
        setClickable(E2_C, false);
        setClickable(E2_D, false);

        // enemy 3
        JLabel e3 = new JLabel("Opponent 3");
        e3.setBounds(810,30,120,15);
        chatGraphic.getContentPane().add(e3);

        JButton E3_A = new JButton();
        E3_A.setBounds(810, 50, 30, 50);
        JButton E3_B = new JButton();
        E3_B.setBounds(845, 50, 30, 50);
        JButton E3_C = new JButton();
        E3_C.setBounds(880, 50, 30, 50);
        JButton E3_D = new JButton();
        E3_D.setBounds(915, 50, 30, 50);

        chatGraphic.getContentPane().add(E3_A);
        chatGraphic.getContentPane().add(E3_B);
        chatGraphic.getContentPane().add(E3_C);
        chatGraphic.getContentPane().add(E3_D);
        setClickable(E3_A, false);
        setClickable(E3_B, false);
        setClickable(E3_C, false);
        setClickable(E3_D, false);

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
            button2Pressed = (JButton) actionEvent.getSource();
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
