package skipBo.server;

import skipBo.enums.Protocol;
import skipBo.userExceptions.NoNameException;
import skipBo.userExceptions.NoProtocolException;

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

    public void analyze(String[] input) {

    }



}
