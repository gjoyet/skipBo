package skipboTest.serverTest;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import skipbo.server.*;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static skipbo.server.SBServer.getSblList;
import static skipbo.server.SBServer.serverLobby;

import static org.junit.Assert.fail;

/**
 * Tests the execution of the network protocol commands. Therefore, it will test cases for all commands but
 * will not, however, test what happens if the input from the client has no network protocol command at the
 * beginning.
 */
public class ProtocolExecutorTest {

    @BeforeClass
    public static void initialize() {
        Main server = new Main();
        server.main(new String[]{"server", "12345"});
        Main client0 = new Main(); Main client1 = new Main();
        client0.main(new String[]{"client", "localhost:12345", "Janni"});
        client1.main(new String[]{"client", "localhost:12345", "Manuela"});
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSetToNickname() {
        Main client2 = new Main(); Main client3 = new Main(); Main client4 = new Main();
        Main client5 = new Main(); Main client6 = new Main(); Main client7 = new Main();
        client2.main(new String[]{"client", "localhost:12345"});
        client3.main(new String[]{"client", "localhost:12345"});
        client4.main(new String[]{"client", "localhost:12345"});
        client5.main(new String[]{"client", "localhost:12345"});
        client6.main(new String[]{"client", "localhost:12345"});
        client7.main(new String[]{"client", "localhost:12345"});
        ProtocolExecutor pe2 = new ProtocolExecutor(new String[]{"SETTO", "Nickname"}, getSblList().get(2));
        ProtocolExecutor pe3 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "Ma&rc"}, getSblList().get(5));
        ProtocolExecutor pe4 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "John"}, getSblList().get(6));
        ProtocolExecutor pe5 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "John"}, getSblList().get(7));
        ProtocolExecutor pe6 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "Al"}, getSblList().get(8));
        ProtocolExecutor pe7 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "WaaayTooLongName"}, getSblList().get(9));
        try {
            pe2.setTo();
            pe3.setTo();
            pe4.setTo();
            pe5.setTo();
            pe6.setTo();
            pe7.setTo();
        } catch (NoCommandException e) {
            e.printStackTrace();
        }
        assertEquals("SBPlayer", pe2.getSBL().getPlayer().getName());
        /*
        assertEquals("SBPlayer1", serverLobby.getPlayerLobby().get(5).getName());
        assertEquals("John", serverLobby.getPlayerLobby().get(6).getName());
        assertEquals("John1", serverLobby.getPlayerLobby().get(7).getName());
        assertEquals("SBPlayer2", serverLobby.getPlayerLobby().get(8).getName());
        assertEquals("SBPlayer3", serverLobby.getPlayerLobby().get(9).getName());
        */
    }

    @Ignore
    @Test
    public void testChangeToStatus() {
        ProtocolExecutor pe = new ProtocolExecutor();
        try {
            pe.setInput(new String[]{"SETTO", "Nickname", "Marc"});
            pe.setTo();
            pe.setInput(new String[]{"CHNGE", "Status", "Ready"});
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }

    @Ignore
    @Test(expected = NoCommandException.class)
    public void testSetToException1() {
        ProtocolExecutor pe = new ProtocolExecutor();
        pe.setInput(new String[]{"SETTO", ""});
        try {
            pe.setTo();
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }

    @Ignore
    @Test(expected = NoCommandException.class)
    public void testSetToException2() {
        ProtocolExecutor pe = new ProtocolExecutor();
        pe.setInput(new String[]{"SETTO", "NotAnOption"});
        try {
            pe.setTo();
        } catch(NoCommandException nce) { fail("Error with test framework."); }
    }
}
