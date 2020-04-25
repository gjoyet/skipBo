package skipboTest.serverTest;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import skipbo.game.Status;
import skipbo.server.*;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import static skipbo.server.SBServer.*;

/**
 * Tests the execution of the network protocol commands. Therefore, it will test cases for all commands but
 * will not, however, test what happens if the input from the client has no network protocol command at the
 * beginning.
 */
public class ProtocolExecutorTest {

    static Main server;
    static Main client0;
    static Main client1;
    static Main client2;
    static Main client3;

    /**
     * This initialization method starts up a server and four clients connected to that server.
     */
    @BeforeClass
    public static void initialize() {
        server = new Main(); server.main(new String[]{"server", "12345"});
        client0 = new Main(); client1 = new Main(); client2 = new Main(); client3 = new Main();
        try {
            client0.main(new String[]{"testClient", "localhost:12345", "Janni"});
            sleep(500);
            client1.main(new String[]{"testClient", "localhost:12345", "Manuela"});
            sleep(500);
            client2.main(new String[]{"testClient", "localhost:12345", "Rohan"});
            sleep(500);
            client3.main(new String[]{"testClient", "localhost:12345", "Guillaume"});
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the implementation of setTo. Details about what is tested are found next to the assertEquals methods.
     */
    @Test
    public void testSetToNickname() {
        Main client4 = new Main(); Main client5 = new Main(); Main client6 = new Main();
        Main client7 = new Main(); Main client8 = new Main(); Main client9 = new Main();
        client4.main(new String[]{"testClient", "localhost:12345"});
        client5.main(new String[]{"testClient", "localhost:12345"});
        client6.main(new String[]{"testClient", "localhost:12345"});
        client7.main(new String[]{"testClient", "localhost:12345"});
        client8.main(new String[]{"testClient", "localhost:12345"});
        client9.main(new String[]{"testClient", "localhost:12345"});
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProtocolExecutor pe4 = new ProtocolExecutor(new String[]{"SETTO", "Nickname"}, getSblList().get(4));
        ProtocolExecutor pe5 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "Ma&rc"}, getSblList().get(5));
        ProtocolExecutor pe6 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "John"}, getSblList().get(6));
        ProtocolExecutor pe7 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "John"}, getSblList().get(7));
        ProtocolExecutor pe8 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "Al"}, getSblList().get(8));
        ProtocolExecutor pe9 = new ProtocolExecutor(new String[]{"SETTO", "Nickname", "WaaayTooLongName"},
                                                                                            getSblList().get(9));
        try {
            pe4.setTo();
            pe5.setTo();
            pe6.setTo();
            pe7.setTo();
            pe8.setTo();
            pe9.setTo();
            sleep(1000);
        } catch (NoCommandException e) {
            System.out.println("Error with testing framework");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("SBPlayer", pe4.getSBL().getPlayer().getName()); // Test: setTo command with null argument
        assertEquals("SBPlayer1", pe5.getSBL().getPlayer().getName());
        // Test: setTo invalid name + default name is already taken
        assertEquals("John", pe6.getSBL().getPlayer().getName()); // Test: regular name setting
        assertEquals("John1", pe7.getSBL().getPlayer().getName()); // Test: setTo name already in use
        assertEquals("SBPlayer2", pe8.getSBL().getPlayer().getName()); // Test: name too short
        assertEquals("SBPlayer3", pe9.getSBL().getPlayer().getName()); // Test: name too long
        servLog.debug("END of testSetToNickname, size of sbListenerList: " + getSblList().size());
    }

    /**
     * Tests the case where the option given to the SETTO command is not valid.
     */
    @Test(expected = NoCommandException.class)
    public void testSetToException1() throws NoCommandException {
        new ProtocolExecutor(new String[]{"SETTO", "NotAnOption"}, getSblList().get(0)).setTo();
    }

    /**
     * Tests the case where the option given to the SETTO command equals null.
     */
    @Test(expected = NoCommandException.class)
    public void testSetToException2() throws NoCommandException {
        new ProtocolExecutor(new String[]{"SETTO"}, getSblList().get(0)).setTo();
    }

    /**
     * Tests the implementation of changeTo with option 'Nickname'. Details about what is tested are
     * found next to the assertEquals methods.
     */
    @Test
    public void testChangeToName() {
        ProtocolExecutor pe0 = new ProtocolExecutor(new String[]{"CHNGE", "Nickname", "Guillaume"}, getSblList().get(0));
        ProtocolExecutor pe1 = new ProtocolExecutor(new String[]{"CHNGE", "Nickname", "Ma&rc"}, getSblList().get(1));
        ProtocolExecutor pe2 = new ProtocolExecutor(new String[]{"CHNGE", "Nickname", "Marc"}, getSblList().get(2));
        ProtocolExecutor pe3 = new ProtocolExecutor(new String[]{"CHNGE", "Nickname", "Guillaume"}, getSblList().get(3));
        try {
           pe0.changeTo();
           pe1.changeTo();
           pe2.changeTo();
           pe3.changeTo();
           sleep(1000);
        } catch(NoCommandException nce) {
            System.out.println("Error with testing framework.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Guillaume1", pe0.getSBL().getPlayer().getName()); // Test: changeTo name already in use
        assertEquals("Manuela", pe1.getSBL().getPlayer().getName()); // Test: changeTo invalid name
        assertEquals("Marc", pe2.getSBL().getPlayer().getName()); // Test: regular name changing
        assertEquals("Guillaume", pe3.getSBL().getPlayer().getName());
        // Test: changeTo own name (should not change anything)

        pe0.getSBL().getPlayer().changeStatus(Status.INGAME);
        pe0.setInput(new String[]{"CHNGE", "Nickname", "Whatever"});
        assertEquals("Guillaume1", pe0.getSBL().getPlayer().getName());
        // Test: changTo while ingame (not allowed)
        pe0.getSBL().getPlayer().changeStatus(Status.WAITING);
        servLog.debug("END of testChangeToName, size of sbListenerList: " + getSblList().size());
    }

    /**
     * Tests the implementation of changeTo with option 'Status'. Details about what is tested are
     * found next to the assertEquals methods.
     */
    @Test
    public void testChangeStatus() {
        ProtocolExecutor pe0 = new ProtocolExecutor(new String[]{"CHNGE", "Status", "READY"}, getSblList().get(0));
        ProtocolExecutor pe1 = new ProtocolExecutor(new String[]{"CHNGE", "Status", "WAITING"}, getSblList().get(1));
        ProtocolExecutor pe2 = new ProtocolExecutor(new String[]{"CHNGE", "Status", "INGAME"}, getSblList().get(2));
        try {
            pe0.changeTo();
            pe1.changeTo();
            pe2.changeTo();
            sleep(200);
        } catch(NoCommandException nce) {
            System.out.println("Error with testing framework.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Status.READY, pe0.getSBL().getPlayer().getStatus()); // Test: changeTo READY
        assertEquals(Status.WAITING, pe1.getSBL().getPlayer().getStatus());
        // Test: changeTo own status (should not change anything)
        assertEquals(Status.WAITING, pe2.getSBL().getPlayer().getStatus());
        // Test: trying to changeTo INGAME (not allowed, game does this, not the client)

        pe0.setInput(new String[]{"CHNGE", "Status", "WAITING"});
        try {
            pe0.changeTo();
            sleep(400);
        } catch(NoCommandException nce) {
            System.out.println("Error with testing framework.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Status.WAITING, pe0.getSBL().getPlayer().getStatus()); // Test: change back to WAITING
        servLog.debug("END of testChangeToStatus, size of sbListenerList: " + getSblList().size());
    }

    /**
     * Tests the case where the option given to the CHNGE command is not valid.
     */
    @Test(expected = NoCommandException.class)
    public void testChangeToException1() throws NoCommandException {
        new ProtocolExecutor(new String[]{"CHNGE", "NotAnOption"}, getSblList().get(0)).changeTo();
    }

    /**
     * Tests the case where the option given to the CHNGE command equals null.
     */
    @Test(expected = NoCommandException.class)
    public void testChangeToException2() throws NoCommandException {
        new ProtocolExecutor(new String[]{"CHNGE"}, getSblList().get(0)).changeTo();
    }

    /**
     * Tests the implementation of newGame. Details about what is tested are
     * found next to the assertEquals methods.
     */
    @Test
    public void testNewGame() {
        ProtocolExecutor pe0 = new ProtocolExecutor(new String[]{"CHNGE", "Status", "READY"}, getSblList().get(0));
        ProtocolExecutor pe1 = new ProtocolExecutor(new String[]{"NWGME", "New", "2§30"}, getSblList().get(1));
        ProtocolExecutor pe2 = new ProtocolExecutor(new String[]{"NWGME", "New", "2§30"}, getSblList().get(2));
        try {
            pe0.changeTo();
            sleep(200);
            pe1.newGame();
            sleep(200);
            pe2.newGame();
            sleep(400);
        } catch(NoCommandException nce) {
            servLog.debug("Error with testing framework");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, serverLobby.getGames().size());
        // Test: First game was started, second one did not (not enough people ready)
        assertEquals("Manuela\nGuillaume1", serverLobby.getGames().get(0).getPlayerList());
        // Test: Client 0 (Guillaume1) and 1 (Manuela) – the only one that was ready and the one that started the game,
        // even though the client was not ready – are the ones in the game, client 1 comes first since it started game
        assertEquals(30, serverLobby.getGames().get(0).players.get(0).getStockPile().size());
        // Test: Stockpile of the game is 30 cards high
        assertEquals("Marc, Guillaume, SBPlayer, SBPlayer1, John, John1, SBPlayer2, SBPlayer3", getPlayerNotIngameList());
        // Test: Client 2 (Marc) and 3 (Guillaume) remain in Lobby
    }

    @Test
    public void testChatMessage() {

    }


}
