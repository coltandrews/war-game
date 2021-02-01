package wargame.model;

import java.util.ArrayList;
import java.util.Random;

public class Player implements IPlayer {

	ArrayList<Card> hand = new ArrayList<Card>();
	ArrayList<Card> startingHand = new ArrayList<Card>();
	Random rand = new Random();
	String name = "";

	public Player() {
	}

	public Player(String name) {
		this.name = name;
	}

	@Override
	public Card playCard() {

		if (hand.size() > 0) {
			int cardNumber = rand.nextInt(hand.size());
			Card selectedCard = hand.get(cardNumber);

			hand.remove(cardNumber);

			return selectedCard;
		}
		return null;

	}

	@Override
	public void collectCards(ArrayList<Card> cards) {
		this.hand.addAll(cards);
		
	}
	
	public void dataCollectStartingHand(ArrayList<Card> cards){
		this.startingHand.addAll(cards);
	}

	public ArrayList<Card> getStartingHand() {
		return startingHand;
	}

	@Override
	public int getCardCount() {
		return hand.size();
	}

	@Override
	public ArrayList<Card> getCards() {
		return hand;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toRecord() {

		ArrayList<String> data = new ArrayList<String>();
		data.add(getName());

		for (Card card : hand) {
			data.add(card.toString());
		}

		return String.join("\t", data);
	}

}
