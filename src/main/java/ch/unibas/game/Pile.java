
import java.lang.reflect.Array;
import java.util.*;

public class Pile {
    ArrayList<Card> deck;
    private int numOfCards;
    private int size;

    public Pile(ArrayList<Card> deck, int numOfCards) {
        ArrayList<Card> deck2 = new ArrayList<Card>(numOfCards);
        this.deck = deck;
        this.numOfCards = numOfCards;
    }

    public Card getTopCard() {

        return deck.get(0);
    }

    public int getNumOfCards() {

        return numOfCards;
    }

    public void removeTopCard() {
        Card topCard = getTopCard();
        topCard = deck.get(1);
        int size = deck.size();
        size--;
    }


    /**
     * switch (Input)
     *      case LOGIN:
     *      // ()
     *      case CHAT_MSG:
     *      // ()
     */
}

