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
    private CardButton hand_a;
    private CardButton hand_b;
    private CardButton hand_c;
    private CardButton hand_d;
    private CardButton hand_e;
    private CardButton stock;
    private CardButton discard_a;
    private CardButton discard_b;
    private CardButton discard_c;
    private CardButton discard_d;

    //Game piles
    private CardButton build_a;
    private CardButton build_b;
    private CardButton build_c;
    private CardButton build_d;

    //Opponent discard piles
    private CardButton e1_a;
    private CardButton e1_b;
    private CardButton e1_c;
    private CardButton e1_d;
    private CardButton e2_a;
    private CardButton e2_b;
    private CardButton e2_c;
    private CardButton e2_d;
    private CardButton e3_a;
    private CardButton e3_b;
    private CardButton e3_c;
    private CardButton e3_d;

    //Opponent stock piles
    private CardButton e1_stock;
    private CardButton e2_stock;
    private CardButton e3_stock;
    private CardButton e4_stock;

    private CardIcons cardIcons = new CardIcons( 30, 50, 78, 120);

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

        discard_a = new CardButton();
        JButton discard_A = discard_a;
        discard_A.setBounds(620, 400, 100, 145);
        discard_b = new CardButton();
        JButton discard_B = discard_b;
        discard_B.setBounds(730, 400, 100, 145);
        discard_c = new CardButton();
        JButton discard_C = discard_c;
        discard_C.setBounds(840, 400, 100, 145);
        discard_d = new CardButton();
        JButton discard_D = discard_d;
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
        hand_a = new CardButton();
        hand_a.setBounds(620, 570, 78, 120);
        hand_b = new CardButton();
        hand_b.setBounds(708, 570, 78, 120);
        hand_c = new CardButton();
        hand_c.setBounds(796, 570, 78, 120);
        hand_d = new CardButton();
        hand_d.setBounds(884, 570, 78, 120);
        hand_e = new CardButton();
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
        stock = new CardButton();
        stock.setBounds(490, 400, 100, 145);
        chatGraphic.getContentPane().add(stock);
        stock.setName(" S 1");
        stock.addActionListener(this);

        /*
        Cards of the game
        */

        JLabel bp = new JLabel("Build piles");
        bp.setBounds(620,130,120,15);
        JLabel dpg = new JLabel("Draw pile");
        dpg.setBounds(490,130,120,15);
        chatGraphic.getContentPane().add(bp);
        chatGraphic.getContentPane().add(dpg);

        // Build piles
        build_a = new CardButton();
        build_a.setBounds(620, 150, 100, 145);
        build_b = new CardButton();
        build_b.setBounds(730, 150, 100, 145);
        build_c = new CardButton();
        build_c.setBounds(840, 150, 100, 145);
        build_d = new CardButton();
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
        ImageIcon back = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Back.png")));
        draw.setIcon(back);
        setClickable(draw, false);

        /*
        display cards of enemies
        */

        // enemy 1
        e1 = new JLabel("Opponent 1");
        e1.setBounds(490,30,120,15);
        chatGraphic.getContentPane().add(e1);

        e1_a = new CardButton();
        e1_a.setBounds(490, 50, 30, 50);
        e1_b = new CardButton();
        e1_b.setBounds(525, 50, 30, 50);
        e1_c = new CardButton();
        e1_c.setBounds(560, 50, 30, 50);
        e1_d = new CardButton();
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

        e2_a = new CardButton();
        e2_a.setBounds(650, 50, 30, 50);
        e2_b = new CardButton();
        e2_b.setBounds(685, 50, 30, 50);
        e2_c = new CardButton();
        e2_c.setBounds(720, 50, 30, 50);
        e2_d = new CardButton();
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

        e3_a = new CardButton();
        e3_a.setBounds(810, 50, 30, 50);
        e3_b = new CardButton();
        e3_b.setBounds(845, 50, 30, 50);
        e3_c = new CardButton();
        e3_c.setBounds(880, 50, 30, 50);
        e3_d = new CardButton();
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
        hand_a.setIcon(cardIcons.getIcon(colAndNum[0], Integer.parseInt(colAndNum[1]), "M"));
        hand_b.setIcon(cardIcons.getIcon(colAndNum[2], Integer.parseInt(colAndNum[3]), "M"));
        hand_c.setIcon(cardIcons.getIcon(colAndNum[4], Integer.parseInt(colAndNum[5]), "M"));
        hand_d.setIcon(cardIcons.getIcon(colAndNum[6], Integer.parseInt(colAndNum[7]), "M"));
        hand_e.setIcon(cardIcons.getIcon(colAndNum[8], Integer.parseInt(colAndNum[9]), "M"));
        stock.setIcon(cardIcons.getIcon(colAndNum[10], Integer.parseInt(colAndNum[11]), "L"));
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
    // Play the stock card to a build pile
    public void stockToBuild(){

    }
    // Play from discard pile to a build pile
    public void discardToBuild(){

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (button1Pressed == null) {
            button1Pressed = (CardButton) actionEvent.getSource();
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
