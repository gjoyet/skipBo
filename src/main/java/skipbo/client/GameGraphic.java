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
public class GameGraphic implements KeyListener{ //ActionListener

    ChatGraphic chatGraphic;
    private JTextArea chat;
    private Player player;
    private Game game;

    GameGraphic(ChatGraphic chatGraphic) {
        this.chatGraphic = chatGraphic;
    }

    void setGameGraphic() {

        chatGraphic.setTitle("Skip-Bros GAME");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        chatGraphic.setBounds(100, 100, 1100, 750);
        chatGraphic.setLocationRelativeTo(null);
        Icon icon = new ImageIcon("logo.png");
        JLabel logo = new JLabel(icon);
        chatGraphic.getContentPane().add(logo);

        this.player = player;
        this.game = game;

        /*
        Cards of the Player
        */
            // Discard Piles
            JTextField discard_A = new JTextField();
            discard_A.setBounds(620,400, 100,145);
            JTextField discard_B = new JTextField();
            discard_B.setBounds(730,400, 100,145);
            JTextField discard_C = new JTextField();
            discard_C.setBounds(840,400, 100,145);
            JTextField discard_D = new JTextField();
            discard_D.setBounds(950,400, 100,145);

            chatGraphic.getContentPane().add(discard_A);
            chatGraphic.getContentPane().add(discard_B);
            chatGraphic.getContentPane().add(discard_C);
            chatGraphic.getContentPane().add(discard_A);
            chatGraphic.getContentPane().add(discard_D);
            discard_A.setVisible(true);
            clientLog.debug("Added discard_A");

            // hand piles
            JTextField hand_A = new JTextField();
            hand_A.setBounds(620,570, 78,120);
            JTextField hand_B = new JTextField();
            hand_B.setBounds(708,570, 78,120);
            JTextField hand_C = new JTextField();
            hand_C.setBounds(796,570, 78,120);
            JTextField hand_D = new JTextField();
            hand_D.setBounds(884,570, 78,120);
            JTextField hand_E = new JTextField();
            hand_E.setBounds(972,570, 78,120);
            chatGraphic.getContentPane().add(hand_A);
            chatGraphic.getContentPane().add(hand_B);
            chatGraphic.getContentPane().add(hand_C);
            chatGraphic.getContentPane().add(hand_D);
            chatGraphic.getContentPane().add(hand_E);
            hand_A.setVisible(true);
            //stock pile
            JTextField stock = new JTextField();
            stock.setBounds(490,400, 100,145);
            chatGraphic.getContentPane().add(stock);

        /*
        Cards of the game
        */
            // Build piles
            JTextField build_A = new JTextField();
            build_A.setBounds(620,150, 100,145);
            JTextField build_B = new JTextField();
            build_B.setBounds(730,150, 100,145);
            JTextField build_C = new JTextField();
            build_C.setBounds(840,150, 100,145);
            JTextField build_D = new JTextField();
            build_D.setBounds(950,150, 100,145);
            chatGraphic.getContentPane().add(build_A);
            chatGraphic.getContentPane().add(build_B);
            chatGraphic.getContentPane().add(build_C);
            chatGraphic.getContentPane().add(build_D);
            // draw piles
            JTextField draw = new JTextField();
            draw.setBounds(490,150, 100,145);
            chatGraphic.getContentPane().add(draw);


        /*
        display cards of enemies
        */

            // enemy 1
            JTextField E1_A = new JTextField();
            E1_A.setBounds(490,50, 30,50);
            JTextField E1_B = new JTextField();
            E1_B.setBounds(525,50, 30,50);
            JTextField E1_C = new JTextField();
            E1_C.setBounds(560,50, 30,50);
            JTextField E1_D = new JTextField();
            E1_D.setBounds(595,50, 30,50);
            chatGraphic.getContentPane().add(E1_A);
            chatGraphic.getContentPane().add(E1_B);
            chatGraphic.getContentPane().add(E1_C);
            chatGraphic.getContentPane().add(E1_D);

            // enemy 1
            JTextField E2_A = new JTextField();
            E2_A.setBounds(650,50, 30,50);
            JTextField E2_B = new JTextField();
            E2_B.setBounds(685,50, 30,50);
            JTextField E2_C = new JTextField();
            E2_C.setBounds(720,50, 30,50);
            JTextField E2_D = new JTextField();
            E2_D.setBounds(755,50, 30,50);
            chatGraphic.getContentPane().add(E2_A);
            chatGraphic.getContentPane().add(E2_B);
            chatGraphic.getContentPane().add(E2_C);
            chatGraphic.getContentPane().add(E2_D);

            // enemy 1
            JTextField E3_A = new JTextField();
            E3_A.setBounds(810,50, 30,50);
            JTextField E3_B = new JTextField();
            E3_B.setBounds(845,50, 30,50);
            JTextField E3_C = new JTextField();
            E3_C.setBounds(880,50, 30,50);
            JTextField E3_D = new JTextField();
            E3_D.setBounds(915,50, 30,50);
            chatGraphic.getContentPane().add(E3_A);
            chatGraphic.getContentPane().add(E3_B);
            chatGraphic.getContentPane().add(E3_C);
            chatGraphic.getContentPane().add(E3_D);

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
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
