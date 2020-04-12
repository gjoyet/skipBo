package skipbo.client;

import skipbo.game.Card;
import skipbo.game.Game;
import skipbo.game.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import static skipbo.client.SBClient.clientLog;

/**
 * Will be class for the Game GUI in the future
 */
public class GameGraphic extends JButton implements KeyListener { //ActionListener

    ChatGraphic chatGraphic;
    private JTextArea chat;
    private Player player;
    private Game game;

    GameGraphic(ChatGraphic chatGraphic) {
        this.chatGraphic = chatGraphic;
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
            chatGraphic.getContentPane().add(discard_A);
            chatGraphic.getContentPane().add(discard_D);
            clientLog.debug("Added discard_A");

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

            //stock pile
            JButton stock = new JButton();
            stock.setBounds(490, 400, 100, 145);
            chatGraphic.getContentPane().add(stock);

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

            // draw piles
            JButton draw = new JButton();
            draw.setBounds(490, 150, 100, 145);
            chatGraphic.getContentPane().add(draw);

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

        }
 /*       setTitle("Skip-Bros CHAT");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 520, 485);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //Output textfield
        chat = new JTextArea(); //TODO: change to JEditorPane or JTextPane to print in colour
        chat.setBounds(20, 30 ,250, 400);
        chat.setLineWrap(true);
        chat.setWrapStyleWord(true);
        chat.setEditable(false);

        chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setBounds(20, 30 ,250, 400 );
        chatScrollPane.setVisible(true);
        chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(chatScrollPane);

        //Input textfield
        inputMes = new JTextArea();
        inputMes.setBounds(290, 350,200, 80);
        inputMes.setEditable(true);
        inputMes.setColumns(3);
        inputMes.setLineWrap(true);
        inputMes.setWrapStyleWord(true);
        inputMes.addKeyListener(this);
        contentPane.add(inputMes);

        inputScrollPane = new JScrollPane(inputMes);
        inputScrollPane.setBounds(290, 350,200, 80);
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(inputScrollPane);*/


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
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
