import java.io.BufferedReader;
import java.io.IOException;

public class ChatServerReader implements Runnable {
    BufferedReader chatBR = null;
    String name = null;

    public ChatServerReader(BufferedReader br, String name) {
        this.chatBR = br;
        this.name = name;
    }
    
    public void run() {
        String line = new String("");
        
         try {   
            while(true) {
                line = this.chatBR.readLine();
                System.out.println(this.name + ": " + line);
                
                if(line.equalsIgnoreCase("quit")) break;
            }

        } catch(IOException ioe) {
            System.out.println(ioe.toString());
        }
    }
}