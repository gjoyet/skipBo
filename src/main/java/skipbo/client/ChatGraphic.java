package skipbo.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGraphic extends JFrame implements ActionListener {

    private SBClientListener clientListener;
    private JPanel contentPane;
    JTextArea chat;
    private JTextArea inputMes;
    private JScrollPane chatScrollPane;
    private JScrollPane inputScrollPane;
    private JButton sendMes;

    ChatGraphic(SBClientListener clientListener) {
        this.clientListener = clientListener;
        setFrame();
        setName();
        printInfo("Connection successful");
        printCommandInfo();
    }

    void setFrame() {

        setTitle("Skip-Bros CHAT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 520, 485);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //Output textfield
        chat = new JTextArea();
        chat.setBounds(20, 30 ,250, 400);
        chat.setLineWrap(true);
        chat.setWrapStyleWord(true);

        chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setBounds(20, 30 ,250, 400 );
        chatScrollPane.setVisible(true);
        chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(chatScrollPane);

        //Input textfield
        inputMes = new JTextArea();
        inputMes.setBounds(290, 335,200, 50);
        inputMes.setEditable(true);
        inputMes.setColumns(3);
        inputMes.setLineWrap(true);
        inputMes.setWrapStyleWord(true);
        contentPane.add(inputMes);

        inputScrollPane = new JScrollPane(inputMes);
        inputScrollPane.setBounds(290, 335,200, 50);
        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(inputScrollPane);

        //Buttons
        sendMes = new JButton("Send message");
        sendMes.setBounds(290, 405, 200,25);
        contentPane.add(sendMes);
        sendMes.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == sendMes) {
            String input = inputMes.getText();
            inputMes.replaceRange("",0,input.length());
            try {
                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                printInfo("Please enter a valid command"); //TODO copy catch from SBClientListener
                printCommandInfo();
            }
        }

    }

    /**
     * Sends Information about valid commands to the client
     */
    void printCommandInfo() {
        //List of Commands
        String listOfCommands = "Commands:\n/change name [name]\n/change status [ready|waiting]\n" +
                "/msg [name] [message]\n/new game\n/play [PlaceFrom] [n] [PlaceTo] [n] | not yet implemented\n/quit\n";
        printInfo(listOfCommands);
    }

    /**
     *Displays an information to the client
     * @param message An information message
     */
    void printInfo(String message) {
        chat.append("\n[INFO] " + message);
    }

    /**
     * Let's client set their name
     */
    void setName() {
        //TODO
        //JOptionPane optionPane = new JOptionPane(JOptionPane.OK_OPTION);
        String name = JOptionPane.showInputDialog(contentPane,"Please enter your name");
        System.out.println("Name can only contain letters or digits and must have between 3 and 13 characters");
        System.out.println("Your suggested nickname (according to your system username): " + System.getProperty("user.name"));
        System.out.println("Please enter your name: ");
        //String name = scanner.nextLine();
        //pw.println("SETTO§Nickname§" + name);
    }
}
