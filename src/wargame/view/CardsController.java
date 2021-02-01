package wargame.view;

import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wargame.model.Card;
import wargame.model.IPlayer;

public class CardsController {

	private IPlayer player;
	private IPlayer machine;

	@FXML
	private TableView<Card> playerCardsTable;

	@FXML
	private TableView<Card> machineCardsTable;

	@FXML
	private TableColumn<Card, String> playerCards;

	@FXML
	private TableColumn<Card, String> machineCards;

	@FXML
	private Button btnClose;

	public CardsController(IPlayer player, IPlayer machine) {

		this.player = player;
		this.machine = machine;
	}

	@FXML
	public void initialize() {
		// setup the scene in here
		System.out.println("initialize");

		playerCards.setCellValueFactory(new PropertyValueFactory<Card, String>("label"));
		machineCards.setCellValueFactory(new PropertyValueFactory<Card, String>("label"));

		playerCardsTable.setItems(getCards(player));
		machineCardsTable.setItems(getCards(machine));

		btnClose.setOnAction(this::close);
	}

	public void close(ActionEvent event) {
		Stage myStage = (Stage) btnClose.getScene().getWindow();

		myStage.close();

	}

	public ObservableList<Card> getCards(IPlayer player) {
		ObservableList<Card> cards = FXCollections.observableArrayList();

		Comparator<Card> compReverse = Collections.reverseOrder();
		Collections.sort(player.getCards(), compReverse);

		cards.addAll(player.getCards());
		return cards;
	}

}
