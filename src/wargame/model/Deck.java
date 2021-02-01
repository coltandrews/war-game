package wargame.model;

import java.util.ArrayList;
import java.util.Random;

public class Deck
{
    // list of cards still in the deck
    private ArrayList<Card> deck = new ArrayList<>();

    // list of cards being used
    private ArrayList<Card> used = new ArrayList<>();

    // used to shuffle the deck
    Random dealer = new Random();

    public Deck()
    {
	// builds the deck
	for (int i = 0; i < 52; i++)
	{
	    deck.add(new Card(i));
	}

    }

    public ArrayList<Card> deal(int handSize)
    {
	ArrayList<Card> hand = new ArrayList<>();

	for (int i = 0; i < handSize; i++)
	{
	    Card next = deck.remove(deck.size() - 1);
	    hand.add(next);
	    used.add(next);
	}

	return hand;
    }

    public void shuffle()
    {
	deck.addAll(used);
	for (int i = 0; i < deck.size() - 1; i++)
	{
	    int swap = dealer.nextInt(deck.size() - i) + i;
	    if (swap != i)
	    {
		Card tmp1 = deck.get(i);
		Card tmp2 = deck.get(swap);
		deck.set(i, tmp2);
		deck.set(swap, tmp1);
	    }
	}
    }

}
