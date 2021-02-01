package wargame.model;

public class Card implements Comparable<Card>
{
    public final int SUIT_SIZE = 13;
    public static final int CLUB = 0;
    public static final int DIAMOND = 1;
    public static final int HEART = 2;
    public static final int SPADE = 3;

    private static final String ranks = "23456789TJQKA";
    private static final String suits = "CDHS";
    
    private int suit;
    private int rank;

    public Card(int card)
    {
	rank = card % SUIT_SIZE;
	suit = card / SUIT_SIZE;
    }

    public Card(String card)
    {
	String rankChar = card.substring(0, 1);
	String suitChar = card.substring(1, 2);

	rank = ranks.indexOf(rankChar);

	suit = suits.indexOf(suitChar);

    }

    public int getRank()
    {
	return rank;
    }

    public int getSuit()
    {
	return suit;
    }
    
    public String getLabel()
    {

	return this.toString();
    }
    
    public String toString()
    {
	return ranks.charAt(rank) + "" + suits.charAt(suit);
    }

    @Override
    public int compareTo(Card o)
    {
	return (this.rank - o.rank);
    }
}
