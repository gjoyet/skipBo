package skipboTest.serverTest;

import org.junit.Test;
import skipbo.server.NoCommandException;
import skipbo.server.ProtocolExecutor;
import skipbo.server.SBListener;
import skipbo.server.SBServer;

import static org.junit.Assert.assertEquals;
import static skipbo.server.SBServer.serverLobby;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.Assert.fail;

/**
 * Tests the execution of the network protocol commands. Therefore, it will test cases for all commands but
 * will not, however, test what happens if the input from the client has no network protocol command at the
 * beginning.
 */
public class ProtocolExecutorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException1() {
        SBListener sbL = null;
        try {
            Socket sock = new Socket(InetAddress.getLocalHost(), 12345);
            sbL = new SBListener(sock, 0);
        } catch(IOException ioe) {
            fail("Error with test framework.");
        }
        ProtocolExecutor pe = new ProtocolExecutor(null, sbL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException2() {
        String[] input = {"Testing", "Program"};
        ProtocolExecutor pe = new ProtocolExecutor(input, null);
    }

    @Test
    public void testSetToNickname() {
        ProtocolExecutor pe = new ProtocolExecutor();
        try {
            pe.setInput(new String[]{"SETTO", "Nickname"});
            pe.setTo();
            pe.setInput(new String[]{"SETTO", "Nickname", "Ma&rc"});
            pe.setTo();
            pe.setInput(new String[]{"SETTO", "Nickname", "John"});
            pe.setTo();
            pe.setTo();
        } catch(NoCommandException nce) { fail("Error with test framework."); }
        assertEquals("SBPlayer", serverLobby.getPlayerLobby().get(0).getName());
        assertEquals("SBPlayer1", serverLobby.getPlayerLobby().get(1).getName());
        assertEquals("John", serverLobby.getPlayerLobby().get(2).getName());
        assertEquals("John1", serverLobby.getPlayerLobby().get(3).getName());
    }

    @Test
    public void testChangeToStatus() {
        ProtocolExecutor pe = new ProtocolExecutor();
        try {
            pe.setInput(new String[]{"SETTO", "Nickname", "Marc"});
            pe.setTo();
            pe.setInput(new String[]{"CHNGE", "Status", "Ready"});
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }

    @Test(expected = NoCommandException.class)
    public void testSetToException1() {
        ProtocolExecutor pe = new ProtocolExecutor();
        pe.setInput(new String[]{"SETTO", ""});
        try {
            pe.setTo();
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }

    @Test(expected = NoCommandException.class)
    public void testSetToException2() {
        ProtocolExecutor pe = new ProtocolExecutor();
        pe.setInput(new String[]{"SETTO", "NotAnOption"});
        try {
            pe.setTo();
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }
}
