package skipBo.game;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatGraphic extends JFrame {

    private JPanel contentPane;
    private JButton sendMes;
    private JTextArea inputMes;
    private JScrollPane scrollPane;

    // Run the JFrame
    public static void main(String[] args){

        Thread window = new Thread(){
            public void run(){
                try{
                    ChatGraphic frame = new ChatGraphic();
                    frame.setVisible(true);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        };
        window.start();
  }

    // Create JFrame
    public ChatGraphic() {

       setTitle("Skip-Bros CHAT");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setBounds(100, 100, 520, 485);

       contentPane= new JPanel();
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

       inputMes = new JTextArea();
       inputMes.setBounds(290, 320,200, 50);
       inputMes.setEditable(true);
       inputMes.setColumns(3);
       contentPane.add(inputMes);








    }
}