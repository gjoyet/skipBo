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
        chatGraphic.setBounds(100, 100, dim.width, dim.height);
        chatGraphic.setLocationRelativeTo(null);
        this.player = player;
        this.game = game;

        /*
        Cards of the Player
        */
            // Discard Piles
            JTextField discard_A = new JTextField();
            discard_A.setBounds(50,100, 120,220);
            JTextField discard_B = new JTextField();
            discard_B.setBounds(50,100, 120,220);
            JTextField discard_C = new JTextField();
            discard_C.setBounds(50,100, 120,220);
            JTextField discard_D = new JTextField();
            discard_D.setBounds(50,100, 120,220);
            // hand piles
            JTextField hand_A = new JTextField();
            hand_A.setBounds(50,100, 120,220);
            JTextField hand_B = new JTextField();
            hand_B.setBounds(50,100, 120,220);
            JTextField hand_C = new JTextField();
            hand_C.setBounds(50,100, 120,220);
            JTextField hand_D = new JTextField();
            hand_D.setBounds(50,100, 120,220);
            //stock piles
            JTextField stock = new JTextField();
            stock.setBounds(50,100, 120,220);

        /*
        Cards of the game
        */
            // Build piles
            JTextField build_A = new JTextField();
            build_A.setBounds(50,100, 120,220);
            JTextField build_B = new JTextField();
            build_B.setBounds(50,100, 120,220);
            JTextField build_C = new JTextField();
            build_C.setBounds(50,100, 120,220);
            JTextField build_D = new JTextField();
            build_D.setBounds(50,100, 120,220);
            // draw piles
            JTextField draw = new JTextField();
            draw.setBounds(50,100, 120,220);

        /*
        display cards of enemies
        */

            // enemy 1
            JTextField E1_A = new JTextField();
            E1_A.setBounds(50,100, 120,220);
            JTextField E1_B = new JTextField();
            E1_B.setBounds(50,100, 120,220);
            JTextField E1_C = new JTextField();
            E1_C.setBounds(50,100, 120,220);
            JTextField E1_D = new JTextField();
            E1_D.setBounds(50,100, 120,220);

            // enemy 1
            JTextField E2_A = new JTextField();
            E2_A.setBounds(50,100, 120,220);
            E2_A.setVisible(false);
            JTextField E2_B = new JTextField();
            E2_B.setBounds(50,100, 120,220);
            E2_B.setVisible(false);
            JTextField E2_C = new JTextField();
            E2_C.setBounds(50,100, 120,220);
            E2_C.setVisible(false);
            JTextField E2_D = new JTextField();
            E2_D.setBounds(50,100, 120,220);
            E2_D.setVisible(false);

            // enemy 1
            JTextField E3_A = new JTextField();
            E3_A.setBounds(50,100, 120,220);
            E3_A.setVisible(false);
            JTextField E3_B = new JTextField();
            E3_B.setBounds(50,100, 120,220);
            E3_B.setVisible(false);
            JTextField E3_C = new JTextField();
            E3_C.setBounds(50,100, 120,220);
            E3_C.setVisible(false);
            JTextField E3_D = new JTextField();
            E3_D.setBounds(50,100, 120,220);
            E3_D.setVisible(false);

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
