package wargame.view;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import wargame.model.Game;

public class StartupWindow {
	Stage stage;

	Button btnPlay = new Button("Play");
	Button btnLoad = new Button("Load");
	Button btnQuit = new Button("Quit");

	Label lblName = new Label("Name :");
	ImageView logoView = new ImageView();
	TextField tfName = new TextField();

	String name;

	StartupWindow(Stage stage) {

		this.stage = stage;

		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setWidth(500);
		stage.setHeight(500);
		stage.centerOnScreen();
		pane.setStyle("-fx-background-color:white");

		String logoPath = "file:resources/miscpng/war.png";
		Image logo = new Image(logoPath);
		logoView.setImage(logo);
		logoView.setPreserveRatio(true);
		logoView.setFitWidth(150);

		btnPlay.setOnAction(this::play);
		btnLoad.setOnAction(this::load);
		btnQuit.setOnAction(this::quit);

		VBox center = new VBox(20);
		HBox btnRow = new HBox(20);
		HBox nameRow = new HBox(20);

		tfName.setMaxWidth(200);
		tfName.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				play(event);
			}
		});

		nameRow.getChildren().addAll(lblName, tfName);
		nameRow.setAlignment(Pos.CENTER);

		btnRow.getChildren().addAll(btnPlay, btnLoad, btnQuit);
		btnRow.setAlignment(Pos.CENTER);

		center.getChildren().addAll(logoView, nameRow, btnRow);
		center.setAlignment(Pos.CENTER);

		pane.setCenter(center);

	}

	public void show() {
		stage.show();
	}

	private void play(ActionEvent event) {
		name = tfName.getText();

		Game game = new Game(name);

		launchGameWindow(game, false);

	}

	private void play(KeyEvent event) {
		name = tfName.getText();

		Game game = new Game(name);

		launchGameWindow(game, false);

	}

	private void launchGameWindow(Game game, boolean gameInProgress) {

		Stage gameStage = new Stage();
		// gameStage.initOwner(stage);

		GameWindow gw = new GameWindow(gameStage, game, gameInProgress);
		gw.show();

	}

	private void load(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Game");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Game files (*.game)", "*.game"));

		// to do: what if they hit cancel
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			try {
				Game game = new Game();
				game.loadGame(file);

				launchGameWindow(game, true);

			} catch (Exception ex) {
				showErrorAlert(ex.getMessage(), "Load Error");
			}
		}
	}

	private void quit(ActionEvent event) {
		stage.close();
	}

	public String getName() {
		return name;
	}

	private void showErrorAlert(String message, String title) {
		Alert gameOver = new Alert(Alert.AlertType.ERROR);
		gameOver.setTitle(title);
		gameOver.setContentText(message);
		gameOver.show();

	}
}
