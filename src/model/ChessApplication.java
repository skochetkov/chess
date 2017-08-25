package model;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChessApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chess Game");
        ChessGameManager game = new ChessGameManager(primaryStage);

        primaryStage.show();

	}
	
	public static void main(String[] args) {
        launch(args);
    }
	
}