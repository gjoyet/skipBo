package skipbo.client;

import skipbo.server.Protocol;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import static skipbo.client.SBClient.clientLog;

/**
 * GUI for Skip-Bo lobby
 */
public class ChatGraphic extends JFrame implements KeyListener, ActionListener {

    private final SBClientListener clientListener;
    private Container contentPane;
    //private JTextArea chat;
    private JTextPane chat;
    private JTextArea inputMes;
    JScrollPane chatScrollPane;
    private JScrollPane inputScrollPane;
    private JButton readyB;
    private JButton startB;
    private JButton infoB;
    private JButton gamesB;
    private JButton whosOnB;
    private JButton leaveB;
    private GameGraphic gameGraphic;
    String playerName = "";

    static final Color DARKGREEN = new Color(0x0AB222);

/*
    //test method
    public static void main(String[] args) {
        ChatGraphic testChatGraphic = new ChatGraphic();
        GameGraphic gameGraphic = new GameGraphic(testChatGraphic);
        gameGraphic.setGameGraphic();
    }

    //Test constructor
    ChatGraphic() {
        setFrame();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    */

    /**
     * Constructor for ChatGraphic without player name. Lets client choose their name.
     *
     * @param clientListener A SBClientListener
     */
    ChatGraphic(SBClientListener clientListener) {
        this.clientListener = clientListener;
        setFrame();
        setName();
        printInfoMessage("Connection successful");
        printCommandList();
    }

    /**
     * Constructor for ChatGraphic with player name
     *
     * @param clientListener A SBClientListener
     * @param name           The player name that was initially chosen when starting the program
     */
    ChatGraphic(SBClientListener clientListener, String name) {
        this.clientListener = clientListener;
        setFrame();
        clientListener.pw.println("SETTO§Nickname§" + name);
        printInfoMessage("Connection successful");
        printCommandList();
    }

    /**
     * Creates the chat window with buttons
     */
    void setFrame() {

        setTitle("Skip-Bros CHAT");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 420, 830);

        contentPane = getContentPane();
        contentPane.setBackground(Color.orange);
        contentPane.setLayout(null);

        ImageIcon logoI = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png")));
        Image image = logoI.getImage().getScaledInstance(250, 190, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        JTextPane logoJ = new JTextPane();
        logoJ.setBorder(null);
        logoJ.setEditable(false);
        logoJ.setBounds(80, 30, scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
        logoJ.setPreferredSize(new Dimension(scaledIcon.getIconWidth(), scaledIcon.getIconHeight()));
        logoJ.insertIcon(scaledIcon);
        contentPane.add(logoJ);

        readyB = new JButton("Ready");
        readyB.setBounds(80, 230, 120, 20);
        contentPane.add(readyB);
        readyB.addActionListener(this);

        startB = new JButton("Start Game");
        startB.setBounds(210, 230, 120, 20);
        contentPane.add(startB);
        startB.addActionListener(this);

        infoB = new JButton("Info");
        infoB.setBounds(80, 260, 120, 20);
        contentPane.add(infoB);
        infoB.addActionListener(this);

        gamesB = new JButton("Ranking");
        gamesB.setBounds(210, 260, 120, 20);
        contentPane.add(gamesB);
        gamesB.addActionListener(this);

        whosOnB = new JButton("Who's on?");
        whosOnB.setBounds(80, 290, 120, 20);
        contentPane.add(whosOnB);
        whosOnB.addActionListener(this);

        leaveB = new JButton("Leave");
        leaveB.setBounds(210, 290, 120, 20);
        contentPane.add(leaveB);
        leaveB.addActionListener(this);


        //Output textfield
        chat = new JTextPane();
        chat.setBounds(80, 320, 250, 340);
        chat.setEditable(false);

        chat = new JTextPane(); //TODO: change to JEditorPane or JTextPane to print in colour
        chat.setBounds(80, 320, 250, 340);
/*        chat.setLineWrap(true);
        chat.setWrapStyleWord(true);*/
        chat.setEditable(false);

        chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setBounds(80, 320, 250, 340);
        chatScrollPane.setVisible(true);
        chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(chatScrollPane);

        /*DefaultCaret caret = (DefaultCaret) chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);*/

        //Input textfield
        inputMes = new JTextArea();
        inputMes.setBounds(80, 665, 250, 60);
        inputMes.setEditable(true);
        inputMes.setColumns(3);
        inputMes.setLineWrap(true);
        inputMes.setWrapStyleWord(true);
        inputMes.addKeyListener(this);
        contentPane.add(inputMes);

        inputScrollPane = new JScrollPane(inputMes);
        inputScrollPane.setBounds(80, 665, 250, 60);
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(inputScrollPane);

    }


    /**
     * Sends Information about valid commands to the client
     */
    void printCommandList() {
        String listOfCommands = "\n***********\nCommands:\n/change name [name]\n/change status ready|waiting\n" +
                "/msg [name] [message]\n/broadcast\n/new game\n/play [PlaceFrom] [n] [PlaceTo] [n]\n" +
                "/list games|players\n/help\n/quit\n***********";
        printInfoMessage(listOfCommands);
    }

    /**
     * Displays an information to the client
     *
     * @param message An information message
     */
    void printInfoMessage(String message) {
        appendToChat("\n[Info] ", DARKGREEN);
        appendToChat(message, Color.BLACK);
        //chat.append("[Info] " + message + "\n");
        //chat.setCaretPosition(chat.getDocument().getLength());
        //chat.setCaretPosition(chat.getDocument().getLength());
    }

    /**
     * Displays an error message to the client
     *
     * @param message An error message
     */
    void printErrorMessage(String message) {
        appendToChat("\n[Error] ", Color.RED);
        appendToChat(message, Color.BLACK);
        //chat.append("[Error] " + message + "\n");
        //chat.setCaretPosition(chat.getDocument().getLength()-1);
    }

    /**
     * Displays a chat message in the chat
     *
     * @param message A chat message
     */
    void printChatMessage(String message) {
        appendToChat("\n" + message, Color.BLACK);
/*        chat.append(message + "\n" );
        chat.setCaretPosition(chat.getDocument().getLength()-1);*/
    }

    /**
     * Creates the game GUI
     */
    void setGameGraphic() {
        gameGraphic = new GameGraphic(clientListener, playerName, chat);
        contentPane.add(gameGraphic.getGameComponent());
        setTitle("Skip-Bros GAME");
        setBounds(100, 100, 1150, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        startB.setEnabled(false);
        readyB.setEnabled(false);
        readyB.setText("Ready");
    }

    /**
     * Ends the game. Removes the game graphic from the frame and displays the winners name. Sends player back to main lobby.
     *
     * @param name Name of the winner of the game.
     */
    void endGame(String name) {
        JOptionPane.showMessageDialog(contentPane, "The winner is: " + name + "!", "Game is finished.",
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png"))));
        contentPane.remove(gameGraphic.getGameComponent());
        setBounds(100, 100, 420, 780);
        setTitle("Skip-Bros CHAT");
        startB.setEnabled(true);
        readyB.setEnabled(true);
    }

    /**
     * Lets client set their name
     */
    void setName() {
        String message = "Please enter your name\n\nName can only contain letters or digits\n" +
                "and must have between 3 and 13 characters";
        String title = "Skip-Bo";
        String nameSuggestion = System.getProperty("user.name");

        String name = (String) JOptionPane.showInputDialog(contentPane, message, title, JOptionPane.QUESTION_MESSAGE,
                null, null, nameSuggestion);

        if (name == null) {
            name = "";
        }
        clientListener.pw.println("SETTO§Nickname§" + name);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
            String input = inputMes.getText().replaceAll("\n", " ");
            inputMes.replaceRange("", 0, input.length());
            input = input.substring(0, input.length() - 1);
            try {
                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                printErrorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    //Handles button events
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton buttonPressed = (JButton) actionEvent.getSource();
        if (buttonPressed == readyB) {
            try {
                if (readyB.getText().equals("Ready")) {
                    clientListener.forward("/change status ready");
                    readyB.setText("Waiting");
                } else {
                    clientListener.forward("/change status waiting");
                    readyB.setText("Ready");
                }

            } catch (NotACommandException e) {
                clientLog.warn("Error with /change status command");
            }

        } else if (buttonPressed == startB) {
            try {
                clientListener.forward("/new game");
            } catch (NotACommandException e) {
                clientLog.warn("Error with /new game command");
            }

        } else if (buttonPressed == infoB) {
            printCommandList();

        } else if (buttonPressed == gamesB) {
            try {
                clientListener.forward("/list games");
            } catch (NotACommandException e) {
                clientLog.warn("Error with /list command");
            }

        } else if (buttonPressed == whosOnB) {
            try {
                clientListener.forward("/list players");
            } catch (NotACommandException e) {
                clientLog.warn("Error with /list command");
            }

        } else if (buttonPressed == leaveB) {
            try {
                clientListener.forward("/quit");
            } catch (NotACommandException e) {
                clientLog.warn("Error with /quit command");
            }
        } else {
            String name = (String) JOptionPane.showInputDialog(contentPane, "Enter your new name:",
                    "Change your name", JOptionPane.PLAIN_MESSAGE, null, null, playerName);
            if (name != null) {
                clientListener.pw.println(Protocol.CHNGE + "§Nickname§" + name);
            }
        }
    }

    /**
     * Prints a string to the chat.
     *
     * @param s     The string to be appended to the chat.
     * @param color The color in which the string is displayed.
     */
    private void appendToChat(String s, Color color) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        attributeSet.addAttribute(StyleConstants.ColorConstants.Foreground, color);
        Document doc = chat.getDocument();
        try {
            doc.insertString(doc.getLength(), s, attributeSet);
//            doc = chat.getDocument();
//            chat.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            clientLog.warn("Error with appending text to chat");
        }

    }

    GameGraphic getGameGraphic() {
        return gameGraphic;
    }

    public void changePlayerName(String name) {
        playerName = name;
    }
}
