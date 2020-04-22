package skipboTest.serverTest;

import org.junit.Test;
import skipbo.server.ProtocolExecutor;
import skipbo.server.SBListener;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.fail;

public class ProtocolExecutorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException1() {
        SBListener sbL = null;
        try {
            Socket sock = new Socket();
            sbL = new SBListener(sock, 0)
        } catch(IOException ioe) {
            fail("Error setting framework for test.");
        }
        ProtocolExecutor pe = new ProtocolExecutor(null, sbL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException2() {
        String[] input = {"Testing", "Program"};
        ProtocolExecutor pe = new ProtocolExecutor(input, null);
    }

    @Test
    public void testSetTo() {

    }
}
