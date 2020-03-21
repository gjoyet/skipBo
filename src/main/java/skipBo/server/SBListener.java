package skipBo.server;

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

    static String takeName() {
        String name;

        try {
            // Sende Befehl für System.out.println("Please enter nickname:") auf terminal von Client;
            // Suche nach unerlaubten Zeichen im Namen -> throw NoNameException
        } catch(NoNameException) {

        } catch(IOException) {

        }

        return name;
    }
}
