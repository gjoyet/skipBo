package skipBo.server;

import skipBo.enums.Protocol;
import skipBo.userExceptions.NoNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Thread waiting for any action from client.
 */
public class SBListener implements Runnable {
    private PrintWriter pw;
    private BufferedReader br;

    public SBListener(PrintWriter pw, BufferedReader br) {
        this.pw = pw;
        this.br = br;
    }

    public void run() {
        String name = takeName();

    }

    String takeName() {
        String name;

        try {
            this.pw.println("PRINT§Terminal§Please enter nickname:");
            String[] input = this.br.readLine().split("§");

            if(input[0].equals("NICKN"))



        } catch(NoNameException nne) {

        } catch(IOException ioe) {
            System.out.println(ioe);
        }

        return name;
    }

}
