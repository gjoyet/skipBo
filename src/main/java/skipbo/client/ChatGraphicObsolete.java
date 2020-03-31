package skipbo.client;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatGraphicObsolete extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JButton sendMes;
    private JTextArea inputMes;
    private JScrollPane scrollPane;

    // Run the JFrame
    public static void main(String[] args){

        Thread window = new Thread(){
            public void run(){
                try{
                    ChatGraphicObsolete frame = new ChatGraphicObsolete();
                    frame.setVisible(true);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        };
        window.start();
  }

    // Create JFrame
    public ChatGraphicObsolete() {

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
       contentPane.add(inputMes);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("entered actionPerformed");
    }
}