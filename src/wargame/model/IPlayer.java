package wargame.model;

import java.util.ArrayList;

public interface IPlayer {
	
	public Card playCard();
	public int getCardCount();
	public ArrayList<Card> getCards();
	public void collectCards(ArrayList<Card> cards);
	public String getName();
	public String toRecord();
	public ArrayList<Card> getStartingHand();
		
}
