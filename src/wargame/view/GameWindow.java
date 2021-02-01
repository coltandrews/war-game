package wargame.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.paint.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.util.Duration;
import wargame.model.Card;
import wargame.model.Game;
import wargame.model.IPlayer;
import wargame.model.WinnerData;

public class GameWindow {

	private final int SUIT_SIZE = 13;
	private Image[] cardImages = new Image[4 * SUIT_SIZE];
	private Stage stage;
	private double keyFrameRate = 1000;

	private double timeLineRate = 1;

	// ArrayList<Data> dataList = new ArrayList<Data>();
	ObservableList<Series<Number, String>> graphData;

	XYChart.Series<Number, String> statData = new XYChart.Series<>();

	WinnerData winnerData = new WinnerData();

	IPlayer winner;
	IPlayer matchWinner;

	ImageView logoView = new ImageView();

	ImageView imgMachineCard = new ImageView();
	ImageView imgPlayerCard = new ImageView();

	Label lblMessage = new Label();
	Label lblHandCount = new Label();
	Label lblPlayerCount = new Label();
	Label lblMachineCount = new Label();
	Label lblPlayerName = new Label();
	Label lblMachineName = new Label();

	Button btnGo = new Button("Play Single Hand");
	Button btnStart = new Button("Start");
	Button btnStop = new Button("Stop");
	Button btnSave = new Button("Save Game");
	Button btnLoad = new Button("Load Game");
	Button btnData = new Button("Data");
	Button btnPeekPlayer = new Button("Peek at cards");
	Button btnReset = new Button("Reset");
	Button btnQuit = new Button("Quit");

	Slider slider = new Slider(0, 1, 0.5);

	Group root = new Group();

	Game game;

	Stage graphStage;
	Timeline timeLine;
	KeyFrame autoPlayFrame;

	boolean simMode = false;

	public GameWindow(Stage stage, Game game, boolean gameInProgress) {
		this.game = game;
		this.stage = stage;
		loadImages();

		setScene(stage);

		if (gameInProgress) {
			updateLabelsForRound();
			displayCard(game.getPlayersLastCard(), imgPlayerCard);
			displayCard(game.getMachineLastCard(), imgMachineCard);
		} else {
			game.deal();
		}

		createTimeLine();

		graphData = FXCollections.observableArrayList();

	}

	private void setScene(Stage stage) {
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setTitle("War 1.0");
		stage.setHeight(1200);
		stage.setWidth(700);
		stage.centerOnScreen();

		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMin(1);
		slider.setMax(10);
		slider.setMajorTickUnit(1);
		slider.setBlockIncrement(1);
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

				timeLineRate = newValue.doubleValue();
				System.out.println(timeLineRate);

				if (simMode) {
					changeTimeLineRate();
				}
			}
		});

		pane.setStyle("-fx-background-color:white");

		String logoPath = "file:resources/miscpng/war.png";
		Image logo = new Image(logoPath);
		logoView.setImage(logo);
		logoView.setPreserveRatio(true);
		logoView.setFitWidth(150);

		HBox warzone = new HBox(30);
		warzone.setPadding(new Insets(30, 30, 30, 30));
		warzone.setAlignment(Pos.CENTER);
		imgMachineCard.setFitWidth(100);
		imgPlayerCard.setFitWidth(100);
		warzone.getChildren().setAll(imgPlayerCard, imgMachineCard);

		HBox btnRow1 = new HBox(10);
		btnRow1.setAlignment(Pos.CENTER);
		btnRow1.getChildren().addAll(btnStart, btnStop, slider);

		HBox btnRow2 = new HBox(10);
		btnRow2.setAlignment(Pos.CENTER);
		btnRow2.getChildren().addAll(btnGo, btnPeekPlayer, btnReset);

		HBox btnRow3 = new HBox(10);
		btnRow3.setAlignment(Pos.CENTER);
		btnRow3.getChildren().addAll(btnSave, btnLoad, btnData, btnQuit);

		btnGo.setOnAction(this::playHand);
		btnStart.setOnAction(this::start);
		btnStop.setOnAction(this::stop);
		btnSave.setOnAction(this::saveGame);
		btnLoad.setOnAction(this::loadGame);
		btnPeekPlayer.setOnAction(this::peekCardsPlayer);
		btnData.setOnAction(this::showData);
		btnReset.setOnAction(this::reset);
		btnQuit.setOnAction(this::quit);
		btnStop.setDisable(true);
		btnData.setDisable(true);

		VBox vbLeft = new VBox(20);
		vbLeft.setAlignment(Pos.CENTER);
		vbLeft.setPadding(new Insets(0, 0, 0, 100));
		VBox vbRight = new VBox(20);
		vbRight.setAlignment(Pos.CENTER);
		vbRight.setPadding(new Insets(0, 100, 0, 0));
		VBox vbCenter = new VBox(20);
		vbCenter.setAlignment(Pos.CENTER);
		VBox vbBottom = new VBox(10);
		vbBottom.setAlignment(Pos.CENTER);
		vbBottom.setPadding(new Insets(0, 0, 50, 0));

		vbLeft.getChildren().addAll(lblPlayerName, lblPlayerCount);
		vbRight.getChildren().addAll(lblMachineName, lblMachineCount);
		vbCenter.getChildren().addAll(logoView, lblMessage, lblHandCount);
		vbBottom.getChildren().addAll(btnRow1, btnRow2, btnRow3);

		lblMachineName.setText(game.getMachine().getName());
		lblMachineName.setTextFill(Color.RED);
		lblMachineName.setFont(new Font("Helvetica", 25));
		lblPlayerName.setText(game.getPlayer().getName());
		lblPlayerName.setFont(new Font("Helvetica", 25));

		BorderPane.setAlignment(vbCenter, Pos.CENTER);
		BorderPane.setAlignment(warzone, Pos.CENTER);

		lblMessage.setTextAlignment(TextAlignment.CENTER);
		lblMessage.setFont(new Font("Helvetica", 25));

		pane.setTop(vbCenter);
		pane.setRight(vbRight);
		pane.setLeft(vbLeft);
		pane.setCenter(warzone);
		pane.setBottom(vbBottom);

	}

	private void saveGame(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT files (*.txt)", "*.txt"));

		File file = fileChooser.showSaveDialog(stage);
		if (file != null) {
			try {
				game.saveGame(file);
			} catch (Exception ex) {
				showErrorAlert(ex.getMessage(), "Save Error");
			}
		}

	}

	private void loadGame(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Game");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT files (*.txt)", "*.txt"));

		// to do: what if they hit cancel
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			try {
				game.loadGame(file);
			} catch (Exception ex) {
				showErrorAlert(ex.getMessage(), "Load Error");
			}

			// reset for new game
			updateLabelsForRound();
			displayCard(game.getPlayersLastCard(), imgPlayerCard);
			displayCard(game.getMachineLastCard(), imgMachineCard);
		}

	}

	private void start(ActionEvent event) {

		simMode = true;
		timeLine.play();

		btnGo.setDisable(true);
		btnStart.setDisable(true);
		btnSave.setDisable(true);
		btnLoad.setDisable(true);

		btnStop.setDisable(false);

	}

	public void createTimeLine() {

		// control time of auto play here
		autoPlayFrame = new KeyFrame(Duration.millis(keyFrameRate), this::playHand);
		timeLine = new Timeline(autoPlayFrame);
		timeLine.setCycleCount(Timeline.INDEFINITE);
	}

	public void changeTimeLineRate() {
		timeLine.setRate(timeLineRate);
	}

	private void peekCardsPlayer(ActionEvent event) {
		stop(event);
		Stage peekStage = new Stage();
		peekStage.initOwner(this.stage);
		peekStage.initModality(Modality.APPLICATION_MODAL);
		peekStage.setTitle("Peek at Cards");
		peekStage.setX(1000);
		peekStage.setY(100);

		Pane pane;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cards.fxml"));
			fxmlLoader.setController(new CardsController(game.getPlayer(), game.getMachine()));
			pane = fxmlLoader.load();
			Scene scene = new Scene(pane);
			peekStage.setScene(scene);
			peekStage.showAndWait();

		} catch (IOException ex) {
			// to do: show load error
			showErrorAlert(ex.getMessage(), "Uh oh!");

		}

	}

	private void showData(ActionEvent event) {
		// stop(event);

		if (graphStage == null) {
			graphStage = new Stage();
		}

		GraphWindow gw = new GraphWindow(graphStage, game, statData);
		gw.show();

	}

	private void stop(ActionEvent event) {
		timeLine.stop();
		simMode = false;

		btnGo.setDisable(false);
		btnStart.setDisable(false);
		btnSave.setDisable(false);
		btnLoad.setDisable(false);

		btnStop.setDisable(true);

	}

	public void updateLabelsForRound() {

		lblHandCount.setText("Round: " + game.getHandCount());
		lblPlayerCount.setText(game.getPlayer().getCardCount() + " Cards");
		lblMachineCount.setText(game.getMachine().getCardCount() + " Cards");

	}

	private void playHand(ActionEvent event) {

		if (simMode) {
			// System.out.println(keyFrameRate);
		}

		ArrayList<Card> hand = game.getCardsForRound();
		displayCard(hand.get(0), imgMachineCard);
		displayCard(hand.get(1), imgPlayerCard);

		// compare cards
		winner = game.determineRoundWinner(hand.get(0), hand.get(1));
		if (winner == null) {
			lblMessage.setText("Tie!");
			// System.out.println("TIE");
		} else {
			lblMessage.setText(winner.getName() + " Wins!");
			game.giveWinnerCards(winner);
		}

		updateLabelsForRound();

		if (game.isGameOver()) {
			if (simMode) {
				stop(null);
			}
			winner = this.matchWinner;

			winnerData.setWinnersStartingHand(winner.getStartingHand());

			btnGo.setDisable(true);

			System.out.println("GAME OVER // WINNER: " + winner.getName().toString());
			System.out.println(winner.getStartingHand());
			saveWinData(winner, winner.getCards());

			showData(event);

			Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
			gameOver.setTitle("GAME OVER");
			gameOver.setContentText("Winner is " + winner.getName() + "  Rounds: " + game.getHandCount());
			gameOver.show();

		}

		if (winner != null) {

			statData.getData().add(new XYChart.Data<Number, String>(game.getHandCount(), winner.getName()));

		} else {

			statData.getData().add(new XYChart.Data<Number, String>(game.getHandCount(), "Tie"));
		}
		btnData.setDisable(false);

	}

	private void saveWinData(IPlayer winner, ArrayList<Card> cards) {

	}

	private void showErrorAlert(String message, String title) {
		Alert gameOver = new Alert(Alert.AlertType.ERROR);
		gameOver.setTitle(title);
		gameOver.setContentText(message);
		gameOver.show();

	}

	public void displayCard(Card card, ImageView imgView) {
		int index = card.getSuit() * SUIT_SIZE + card.getRank();
		imgView.setImage(cardImages[index]);
		imgView.setPreserveRatio(true);
	}

	private void loadImages() {
		String resourceDir = "file:resources/cardspng/";
		char[] suits = { 'c', 'd', 'h', 's' };
		char[] rank = { '2', '3', '4', '5', '6', '7', '8', '9', '0', 'j', 'q', 'k', 'a' };
		int slot = 0;
		// load images
		for (int s = 0; s < 4; s++) {
			for (int r = 0; r < SUIT_SIZE; r++) {
				String path = resourceDir + suits[s] + rank[r] + ".png";
				cardImages[slot] = new Image(path);
				slot++;
			}
		}
	}

	private void reset(ActionEvent event) {
		// dataList.clear();
		game.deal();

		// GameWindow gw = new GameWindow(stage, game);
		// gw.show();
	}

	private void quit(ActionEvent event) {
		stage.close();
	}

	public void show() {
		stage.show();
	}
}
