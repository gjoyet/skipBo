package skipboTest.gameTest;

import org.junit.Ignore;
import org.junit.Test;
import skipbo.game.Card;
import skipbo.game.Game;
import skipbo.game.Player;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the 'play' methods (playToMiddle, playToDiscard, playFromStockToMiddle and playFromDiscardToMiddle)
 * of the Game class. It is important to note that, since the ProtocolExecutor class already processes and prevents
 * certain invalid moves, the response of the program to the latter cannot be tested by using the mentioned methods.
 */
public class GamePlayTest {

    @Ignore
    @Test
    public void testPlayToMiddle() {
        // set game framework
        Player p0 = new Player(0);
        Player p1 = new Player(1);
        Player p2 = new Player(2);
        Player p3 = new Player(4);
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(p0); playerList.add(p1); playerList.add(p2); playerList.add(p3);
        Game game = new Game(playerList);

        // set hand cards of player 0
        ArrayList<Card> cards0 = new ArrayList<>();
        cards0.add(new Card(1, Color.green));  cards0.add(new Card(5, Color.red));
        cards0.add(new Card(9, Color.orange));  cards0.add(new Card(1, Color.green));
        cards0.add(new Card(5, Color.red));
        p0.setHandCards(cards0);
        // set hand cards of player 1
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(new Card(2, Color.green));  cards1.add(new Card(6, Color.red));
        cards1.add(new Card(10, Color.orange));  cards1.add(new Card(2, Color.green));
        cards1.add(new Card(6, Color.red));
        p0.setHandCards(cards1);
        // set hand cards of player 2
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(3, Color.green));  cards2.add(new Card(7, Color.red));
        cards2.add(new Card(11, Color.orange));  cards2.add(new Card(3, Color.green));
        cards2.add(new Card(7, Color.red));
        p0.setHandCards(cards2);
        // set hand cards of player 3
        ArrayList<Card> cards3 = new ArrayList<>();
        cards3.add(new Card(4, Color.green));  cards3.add(new Card(8, Color.red));
        cards3.add(new Card(12, Color.orange));  cards3.add(new Card(4, Color.green));
        cards3.add(new Card(8, Color.red));
        p0.setHandCards(cards3);

        // Actual tests:
        assertEquals(true, game.playToMiddle(p0, 0, 0));
    }
}
