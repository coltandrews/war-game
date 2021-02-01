package wargame.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import wargame.model.Game;

public class GraphWindow
{

    Game game;
    Stage dataStage;
    XYChart.Series<Number, String> statData;

    public GraphWindow(Stage stage, Game game, XYChart.Series<Number, String> statData)
    {
	this.game = game;
	this.dataStage = stage;
	this.statData = statData;
	
	setupGraph();

	
    }

    private void setupGraph() {

	HBox root = new HBox();
	Scene scene = new Scene(root, 1020, 350);

	dataStage.setTitle("Winner Data");
	dataStage.setY(600);
	dataStage.setX(900);
	dataStage.setScene(scene);

	ObservableList<String> categories = FXCollections.observableArrayList();
	categories.add("Machine");
	categories.add("Tie");
	categories.add(game.getPlayer().getName());

	NumberAxis xAxis = new NumberAxis();
	xAxis.setLabel("Rounds");

	CategoryAxis yAxis = new CategoryAxis();
	yAxis.setLabel("Players");
	yAxis.setCategories(categories);
	
	LineChart<Number, String> lineChart = new LineChart<Number, String>(xAxis, yAxis);
	lineChart.setTitle("Winners Data");
	lineChart.setPrefWidth(1000);
	lineChart.setMinWidth(1000);
	lineChart.setMaxWidth(1000);
	
	lineChart.getData().add(statData);
	root.getChildren().add(lineChart);

    }
    
    public void show() {
	this.dataStage.show();
    }
    

}
