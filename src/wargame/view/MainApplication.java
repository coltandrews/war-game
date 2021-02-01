package wargame.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		StartupWindow sw = new StartupWindow(arg0);
		sw.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}