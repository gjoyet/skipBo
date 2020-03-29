package skipbo.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ChatGraphic extends JFrame implements Runnable, ActionListener {




    private JPanel contentPane;
    private JButton sendMes;
    private JTextArea inputMes;
    private JTextField inputMesPrint;
    private SBClientListener clientListener;


    public static void main(String[] args) throws IOException {
        ChatGraphicObsolete frame = new ChatGraphicObsolete();
        frame.setVisible(true);

    }



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
        setBounds(20, 30, 240, 400);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);



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

        inputMesPrint = new JTextField();
        inputMesPrint.setBounds(290, 335,200, 50);
        inputMesPrint.setText("fefe");
        inputMesPrint.setFont(new Font("Arial",Font.BOLD, 13));
        inputMesPrint.setVisible(true);
        contentPane.add(inputMesPrint);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("entered actionPerformed method"); //For testing purpose
        inputMesPrint.setText("Dis is önli för testing pörpöses");
        if (actionEvent.getSource() == sendMes) {
            String input = inputMes.getText();
            inputMesPrint.setText(input);
            try {

                clientListener.forward(input);
            } catch (IndexOutOfBoundsException | NotACommandException e) {
                System.out.println("Please enter a valid command"); //TODO copy catch from SBClientListener
                clientListener.printCommandInfo();
            }
        }

    }
}
