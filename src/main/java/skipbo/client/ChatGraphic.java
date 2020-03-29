package skipbo.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGraphic extends JFrame implements Runnable, ActionListener {

    private JPanel contentPane;
    private JButton sendMes;
    private JTextArea inputMes;
    private JScrollPane scrollPane;
    private SBClientListener clientListener;

    ChatGraphic(SBClientListener clientListener) {
        setFrame();
        this.clientListener = clientListener;
    }


    @Override
    public void run() {
        try{
            ChatGraphicObsolete frame = new ChatGraphicObsolete();
            frame.setVisible(true);
        } catch (Exception e){
            System.out.println(e);
        }
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

        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 30 ,250, 400 );
        scrollPane.setVisible(true);
        contentPane.add(scrollPane);

        sendMes = new JButton("Send message");
        sendMes.setBounds(290, 405, 200,25);
        contentPane.add(sendMes);
        sendMes.addActionListener(this);

        inputMes = new JTextArea();
        inputMes.setBounds(290, 335,200, 50);
        inputMes.setEditable(true);
        inputMes.setColumns(3);
        inputMes.setLineWrap(true);
        contentPane.add(inputMes);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("entered actionPerformed method"); //For testing purpose
        if (actionEvent.getSource() == sendMes) {
            String input = inputMes.getText();
            try {
                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                System.out.println("Please enter a valid command");
                clientListener.printCommandInfo();
            }
        }

    }
}
