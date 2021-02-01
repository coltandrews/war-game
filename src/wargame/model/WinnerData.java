package wargame.model;

import java.util.ArrayList;

public class WinnerData {

	ArrayList<Card> startingHand = new ArrayList<Card>();
	String winnerName;
	

	WinnerData winnerData = this;

	public WinnerData() {
		
	}

	
	public void setWinnersStartingHand(ArrayList<Card> startingHand) {
		this.startingHand = startingHand;
	}
}
