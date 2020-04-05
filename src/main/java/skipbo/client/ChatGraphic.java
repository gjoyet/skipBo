package skipbo.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatGraphic extends JFrame implements KeyListener { //ActionListener

    private SBClientListener clientListener;
    private JPanel contentPane;
    private JTextArea chat;
    private JTextArea inputMes;
    JScrollPane chatScrollPane;
    private JScrollPane inputScrollPane;

    ChatGraphic(SBClientListener clientListener) {
        this.clientListener = clientListener;
        setFrame();
        setName();
        printInfoMessage("Connection successful");
        printCommandList();
    }

    ChatGraphic(SBClientListener clientListener, String name) {
        this.clientListener = clientListener;
        setFrame();
        clientListener.pw.println("SETTO§Nickname§" + name);
        printInfoMessage("Connection successful");
        printCommandList();
    }

    void setFrame() {

        setTitle("Skip-Bros CHAT");
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
        contentPane.add(inputScrollPane);

    }


    /**
     * Sends Information about valid commands to the client
     */
    void printCommandList() {
        //List of Commands
        String listOfCommands = "\n***********\nCommands:\n/change name [name]\n/change status ready|waiting\n" +
                "/msg [name] [message]\n/broadcast\n/new game\n/play [PlaceFrom] [n] [PlaceTo] [n]\n" +
                "/list games|players|commands\n/quit\n***********";
        printInfoMessage(listOfCommands);
    }

    /**
     *Displays an information to the client
     * @param message An information message
     */
    void printInfoMessage(String message) {
        chat.append("[Info] " + message + "\n");
        chat.setCaretPosition(chat.getDocument().getLength());
    }

    /**
     * Displays an error message to the client
     * @param message An error message
     */
    void printErrorMessage(String message) {
        chat.append("[Error] " + message + "\n");
        chat.setCaretPosition(chat.getDocument().getLength()-1);
    }

    /**
     * Displays a chat message in the chat
     * @param message A chat message
     */
    void printChatMessage(String message) {
        chat.append(message + "\n" );
        chat.setCaretPosition(chat.getDocument().getLength()-1);
    }

    /**
     * Creates the game GUI
     */
    void setGameGraphic() {
        GameGraphic gameGraphic = new GameGraphic(this);
        gameGraphic.setGameGraphic();
    }

    /**
     * Lets client set their name
     */
    void setName() {
        String message = "Please enter your name\n\nName can only contain letters or digits\n" +
                "and must have between 3 and 13 characters";
        String title = "Skip-Bo";
        String nameSuggestion = System.getProperty("user.name");

        String name = (String)JOptionPane.showInputDialog(contentPane, message, title, JOptionPane.QUESTION_MESSAGE,
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
            inputMes.replaceRange("",0,input.length());
            input = input.substring(0,input.length() - 1);
            try {
                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                printErrorMessage(e.getMessage());
                printCommandList(); //delete when /list commands is implemented
                //printInfo("Use '/list commands' to display all possible commands");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
