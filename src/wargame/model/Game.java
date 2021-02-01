package wargame.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	private static final int HALF_DECK = 26;
	private boolean gameOver = false;
	private int handCount = 0;

	Machine machine = new Machine();
	Player player = new Player();

	Deck deck = new Deck();

	ArrayList<Card> cardsToBeWon = new ArrayList<Card>();

	Card playersLastCard;
	Card machinesLastCard;

	public Game(String playerName) {
		player.setName(playerName);
	}

	public Game() {

	}

	public void deal() {
		deck.shuffle();

		ArrayList<Card> deck1 = deck.deal(HALF_DECK);
		ArrayList<Card> deck2 = deck.deal(HALF_DECK);

		machine.collectCards(deck1);
		machine.dataCollectStartingHand(deck1);
		player.collectCards(deck2);
		machine.dataCollectStartingHand(deck2);
	}

	public Card getPlayersLastCard() {
		return playersLastCard;
	}

	public Card getMachineLastCard() {
		return machinesLastCard;
	}

	// play the game from console, for testing
	public void play() throws InterruptedException {
		ArrayList<Card> cardsForRound = new ArrayList<Card>();
		ArrayList<Card> cardsToBeWon = new ArrayList<Card>();

		int numRounds = 0;
		while (!gameOver) {

			numRounds++;
			System.out.println();
			System.out.println("Round: " + numRounds);

			cardsForRound.clear();
			cardsForRound.addAll(getCardsForRound());

			cardsToBeWon.addAll(cardsForRound);
			System.out.println("Machine's Card: " + cardsForRound.get(0).toString() + " // Player's Card: "
					+ cardsForRound.get(1).toString());
			// machine is first in array
			IPlayer winner = determineRoundWinner(cardsForRound.get(0), cardsForRound.get(1));
			if (winner == null) {
				// its a tie!
				System.out.println("Tie");
			} else {
				System.out.println("WINNER: " + winner.getName());

				winner.collectCards(cardsToBeWon);
				cardsToBeWon.clear();

				System.out
						.println("Machine has: " + machine.getCardCount() + " // player has " + player.getCardCount());
				System.out.println();
			}
			// sleeper
			// Thread.sleep(200);
			if (machine.getCardCount() == 0 || player.getCardCount() == 0) {
				System.out.println("Game Over");
				System.out.println("Rounds: " + numRounds);
				if (machine.getCardCount() == 0) {
					System.out.println("Player won!");
				} else {
					System.out.println("Machine won!");
				}
				gameOver = true;
			}
		}

	}
	
	public static void main(String[] args) {
	
	}

	public ArrayList<Card> getCardsForRound() {
		machinesLastCard = machine.playCard();
		playersLastCard = player.playCard();

		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(machinesLastCard);
		cards.add(playersLastCard);

		cardsToBeWon.addAll(cards);

		handCount++;

		return cards;
	}

	public void saveGame(File file) throws Exception {
		try {
			FileWriter fw = new FileWriter(file);

			// save game stats
			String record = this.toRecord();
			fw.write(record);
			fw.write("\n");

			// save player
			record = player.toRecord();
			fw.write(record);
			fw.write("\n");

			// save machine
			record = machine.toRecord();
			fw.write(record);

			fw.close();

		} catch (IOException ex) {
			throw new Exception("Sorry. Error saving game.");
		}
	}

	public void loadGame(File file) throws Exception {

		int lineNumber = 0;
		Scanner input;
		try {
			input = new Scanner(file);
			while (input.hasNext()) {

				lineNumber++;

				String line = input.nextLine();
				String[] fields = line.split("\t");

				String name = fields[0];

				if (name.toLowerCase().equals("machine")) {

					// create machine player
					machine = new Machine();
					loadPlayersCards(machine, fields);
				} else if (name.toLowerCase().equals("player")) {
					// create player
					player = new Player(name);
					loadPlayersCards(player, fields);
				} else if (name.toLowerCase().equals("game stats")) {
					// set stats
					try {
						handCount = Integer.parseInt(fields[1]);

					} catch (NumberFormatException ex) {
						input.close();
						throw new Exception("Error loading file. Invalid record on line " + lineNumber
								+ ". Number of hands played is invalid.");

					}

					try {
						machinesLastCard = new Card(fields[2]);
						playersLastCard = new Card(fields[3]);

					} catch (Exception ex) {
						input.close();
						throw new Exception("Error loading file. Invalid game stats at line " + lineNumber);
					}

				} else {
					input.close();
					throw new Exception("Error loading file. Invalid record on line " + lineNumber
							+ ". Must be a machine or player record.");
				}

			}

			input.close();

			cardsToBeWon.clear();

		} catch (IOException ex) {
			throw new Exception("Sorry. Error loading game.");
		}

	}

	public void loadPlayersCards(IPlayer player, String[] fields) {
		ArrayList<Card> cards = new ArrayList<Card>();

		for (int i = 1; i < fields.length; i++) {
			String cardName = fields[i];
			Card card = new Card(cardName);
			cards.add(card);
		}
		player.collectCards(cards);
	}

	public void giveWinnerCards(IPlayer winner) {
		winner.collectCards(cardsToBeWon);
		cardsToBeWon.clear();
	}

	public IPlayer getMachine() {
		return machine;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public IPlayer determineRoundWinner(Card machineCard, Card playerCard) {
		int result = machineCard.compareTo(playerCard);
		if (result > 0) {
			return machine;
		} else if (result < 0) {
			return player;

		} else {
			return null;
		}

	}

	public boolean isGameOver() {
		if (machine.getCardCount() == 0 || player.getCardCount() == 0) {
			return true;
		}
		return false;
	}

	public int getHandCount() {
		return handCount;
	}

	public String toRecord() {

		ArrayList<String> data = new ArrayList<String>();
		data.add("Game stats");
		data.add(Integer.toString(handCount));
		data.add(machinesLastCard.toString());
		data.add(playersLastCard.toString());

		return String.join("\t", data);
	}

}
