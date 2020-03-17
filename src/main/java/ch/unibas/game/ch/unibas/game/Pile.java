package ch.unibas.game;
import java.lang.reflect.Array;
import java.util.*;

public class Pile {
    ArrayList<Card> deck;
    private int numOfCards;
    private int size;

    Pile (ArrayList<Card> deck, int numOfCards){
        ArrayList<Card> buildPile = new ArrayList<Card>();
        this.deck = deck;
        this.numOfCards = numOfCards;
    }

    public Card getTopCard(){
        return deck.get(0);
    }

    public int getNumOfCards(){
        return numOfCards;
    }

    public void removeTopCard(){
        Card topCard = getTopCard();
        topCard = deck.get(1);
        int size =deck.size();
        size--;
    }
}

