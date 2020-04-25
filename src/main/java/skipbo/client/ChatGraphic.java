package skipbo.client;

import skipbo.server.Protocol;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static skipbo.client.SBClient.clientLog;
import static skipbo.server.Protocol.NWGME;

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
    private JButton manualB;
    private JButton changeNameB;
    private JButton readyB;
    private JButton startB;
    private JButton infoB;
    private JButton gamesB;
    private JButton whosOnB;
    private JButton leaveB;
    private GameGraphic gameGraphic;
    String playerName = "";
    private JComboBox<String> listChat;
    private String[] playersChat = {"all","global","geiom","theLegend27","ManuWelan","MrDickson","RohanZohan","GreekLegend","Borat","HaikhoMisori"};

    static final Color DARKGREEN = new Color(0x0AB222);

    private final int X_FRAME = 400;
    private final int Y_FRAME = 20;
    private final int WIDTH_FRAME = 420;
    private final int HEIGHT_FRAME = 830;

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
        setBounds(X_FRAME, Y_FRAME, WIDTH_FRAME, HEIGHT_FRAME);

        contentPane = getContentPane();
        contentPane.setBackground(Color.orange);
        contentPane.setLayout(null);

        // Logo on top
        ImageIcon logoI = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png")));
        Image image = logoI.getImage().getScaledInstance(210, 160, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        JTextPane logoJ = new JTextPane();
        logoJ.setBorder(null);
        logoJ.setEditable(false);
        logoJ.setBounds(100, 30, scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
        logoJ.setPreferredSize(new Dimension(scaledIcon.getIconWidth(), scaledIcon.getIconHeight()));
        logoJ.insertIcon(scaledIcon);
        contentPane.add(logoJ);

        // Layout Manager for menu buttons
        final int WIDTH_MENU_B = 120;
        final int HEIGHT_MENU_B = 22;
        final int X_MENU_B_R1 = 80;
        final int X_MENU_B_R2 = 210;
        final int Y_MENU_B = 195;
        final int Y_DISTANCE_MENU_B = 30;

        manualB = new JButton("Manual");
        manualB.setBounds(X_MENU_B_R1, Y_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(manualB);
        manualB.addActionListener(this);

        changeNameB = new JButton("Change name");
        changeNameB.setBounds(X_MENU_B_R2, Y_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(changeNameB);
        changeNameB.addActionListener(this);

        infoB = new JButton("Info");
        infoB.setBounds(X_MENU_B_R1, Y_MENU_B+ 1*Y_DISTANCE_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(infoB);
        infoB.addActionListener(this);

        gamesB = new JButton("Ranking");
        gamesB.setBounds(X_MENU_B_R2, Y_MENU_B+ 1*Y_DISTANCE_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(gamesB);
        gamesB.addActionListener(this);

        whosOnB = new JButton("Who's on?");
        whosOnB.setBounds(X_MENU_B_R1, Y_MENU_B+ 2*Y_DISTANCE_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(whosOnB);
        whosOnB.addActionListener(this);

        leaveB = new JButton("Leave");
        leaveB.setBounds(X_MENU_B_R2, Y_MENU_B+ 2*Y_DISTANCE_MENU_B, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(leaveB);
        leaveB.addActionListener(this);

        Color gameButtonColor = new Color (153,255,153);

        readyB = new JButton("Ready");
        readyB.setBackground(gameButtonColor);
        readyB.setBounds(X_MENU_B_R1, Y_MENU_B+ 3*Y_DISTANCE_MENU_B+8, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(readyB);
        readyB.addActionListener(this);

        startB = new JButton("Start Game");
        startB.setBackground(gameButtonColor);
        startB.setBounds(X_MENU_B_R2, Y_MENU_B+ 3*Y_DISTANCE_MENU_B+8, WIDTH_MENU_B, HEIGHT_MENU_B);
        contentPane.add(startB);
        startB.addActionListener(this);


        //Output textfield
        chat = new JTextPane();
        chat.setBounds(80, 330, 250, 340);
        chat.setEditable(false);

        chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setBounds(80, 330, 250, 340);
        chatScrollPane.setVisible(true);
        chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(chatScrollPane);

        // Dropdown menu for global or private chat
        JLabel privateChatL = new JLabel("Chat with:");
        add(privateChatL);
        privateChatL.setBounds(80,675,90,20);
        //playersChat = {"all","global","geiom","theLegend27","ManuWelan","MrDickson","RohanZohan","GreekLegend","Borat","HaikhoMisori"};
        listChat = new JComboBox<>(playersChat);
        listChat.setVisible(true);
        listChat.setBounds(150,675,180,20);
        add(listChat);



        //Input textfield
        inputMes = new JTextArea();
        inputMes.setBounds(80, 700, 250, 60);
        inputMes.setEditable(true);
        inputMes.setColumns(3);
        inputMes.setLineWrap(true);
        inputMes.setWrapStyleWord(true);
        inputMes.addKeyListener(this);
        contentPane.add(inputMes);

        inputScrollPane = new JScrollPane(inputMes);
        inputScrollPane.setBounds(80, 700, 250, 60);
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(inputScrollPane);

        DefaultCaret caret = (DefaultCaret) chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }


    /**
     * Sends Information about valid commands to the client
     */
    void printCommandList() {
        String listOfCommands = "\n***********\nCommands:\n/change name [name]\n/change status ready|waiting\n" +
                "/msg [name] [message]\n/broadcast\n/new game\n/play [PlaceFrom] [n] [PlaceTo] [n]\n" +
                "/list games|players\n/help\n/quit\n***********";
        printInfoMessage(listOfCommands);

/*        JScrollBar vertical = chatScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());*/

/*        chatScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if ((chatScrollPane.getVerticalScrollBar().getMaximum() - e.getAdjustable().getMaximum()) == 0)
                return;
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });*/

/*        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = chatScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });*/
    }

    /**
     * Displays an information to the client
     *
     * @param message An information message
     */
    void printInfoMessage(String message) {
        appendToChat("\n[Info] ", DARKGREEN);
        appendToChat(message, Color.BLACK);
    }

    /**
     * Displays an error message to the client
     *
     * @param message An error message
     */
    void printErrorMessage(String message) {
        appendToChat("\n[Error] ", Color.RED);
        appendToChat(message, Color.BLACK);
    }

    /**
     * Displays a chat message in the chat
     *
     * @param message A chat message
     */
    void printChatMessage(String message) {
        appendToChat("\n" + message, Color.BLACK);
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
        changeNameB.setEnabled(false);
        readyB.setText("Ready");
    }

    /**
     * Ends the game. Removes the game graphic from the frame and displays the winners name. Sends player back to main lobby.
     *
     * @param name Name of the winner of the game.
     */
    void endGame(String name) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png")));
        JOptionPane optionPane = new JOptionPane("The winner is: " + name + "!", JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, icon);
        JDialog dialog = optionPane.createDialog(contentPane, "Game is finished.");

        int width = gameGraphic.getGameComponent().getWidth();
        int height = gameGraphic.getGameComponent().getHeight();
        int iconWidth = icon.getIconWidth()+270;
        int iconHeight = icon.getIconHeight()+100;
        dialog.setBounds(width/2-iconWidth/2, height/2-iconHeight/2, iconWidth, iconHeight);
        dialog.setVisible(true);
        /*JOptionPane.showMessageDialog(contentPane, "The winner is: " + name + "!", "Game is finished.",
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png"))));*/
        contentPane.remove(gameGraphic.getGameComponent());
        setBounds(X_FRAME, Y_FRAME, WIDTH_FRAME, HEIGHT_FRAME);
        setTitle("Skip-Bros CHAT");
        startB.setEnabled(true);
        readyB.setEnabled(true);
        changeNameB.setEnabled(true);
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
            if (!input.startsWith("/")) {
                String s = (String) listChat.getSelectedItem();
                assert s != null;
                if (s.equals("global")) {
                    input = "/broadcast " + input;
                } else if (!s.equals("all")) {
                    input = "/msg " + s + " " + s;
                }
            }
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
            //TODO change to JComboBox
            String[] numberOfPlayers = new String[]{"2", "3", "4"};
            String[] numberOfStock = new String[]{"3", "10", "20", "30"};
            String selectedPlayer = (String) JOptionPane.showInputDialog(contentPane, "Select number of players",
                    "Starting new game", JOptionPane.PLAIN_MESSAGE, null, numberOfPlayers, numberOfPlayers[0]);
            if (selectedPlayer == null) {return;}
            String selectedStock = (String) JOptionPane.showInputDialog(contentPane, "Select number of stock cards",
                    "Starting new game", JOptionPane.PLAIN_MESSAGE, null, numberOfStock, numberOfStock[0]);
            if (selectedStock == null) {return;}
            clientListener.pw.println(NWGME + "§New§" + selectedPlayer + "§" + selectedStock);

        } else if (buttonPressed == manualB) {
            if (Desktop.isDesktopSupported()) {
                try {
                    File manual = new File("build/resources/main/Instruction_manual_v2.pdf");
                    Desktop.getDesktop().open(manual);
                } catch (NullPointerException | IOException npe) {
                    clientLog.warn("Cannot open manual PDF");
                }
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
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        } else if (buttonPressed == changeNameB) {
            String name = (String) JOptionPane.showInputDialog(contentPane, "Enter your new name:",
                    "Change your name", JOptionPane.PLAIN_MESSAGE, null, null, playerName);
            if (name != null) {
                clientListener.pw.println(Protocol.CHNGE + "§Nickname§" + name);
            }
        }
    }

    /**
     * Prints a string to the chat.
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
        /*SwingUtilities.invokeLater(() -> {
            try {
                JScrollBar bar = chatScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            } catch (Exception e) {
                clientLog.warn("Exception while trying to adjust the scrollbar");
            }
        });*/

    }

    GameGraphic getGameGraphic() {
        return gameGraphic;
    }

    public void changePlayerName(String name) {
        playerName = name;
    }

    SBClientListener getClientListener() {
        return clientListener;
    }
}
