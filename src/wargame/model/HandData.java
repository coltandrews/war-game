 package wargame.model;

public class HandData {
	public int handCount;
	public String winner;

	public HandData(int handCount, String winner) {
		this.handCount = handCount;
		this.winner = winner;
	}
	
	public int getCount() {
		return handCount;
	}
	public String getWinner() {
		return winner;
	}
}
