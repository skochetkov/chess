package model;

import game.entities.Cell;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChessApplication extends Application {
	private GridPane border = new GridPane();
	private GridPane board;
	private VBox controlPanel = new VBox();
	private VBox digits;
	private HBox letters;
	public static Stage primaryStage;
	private ChessGameManager game;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chess Game");
		board = new GridPane();
		initDigits();
        initLetters();
		
        border.add(digits, 0, 1, 1, 1);
        border.add(letters, 1, 2, 1, 1);
        
		border.setPadding(new Insets(20, 20, 0, 0));
		border.setPrefSize(440, 440);
		border.setStyle("-fx-background-color: #2C2020;");
		border.add(board, 1, 1, 1, 1);
		
		
        
		controlPanel.getChildren().add(border);
		primaryStage.setScene(new Scene(controlPanel));
		
		game = new ChessGameManager();
		initBoard();

        primaryStage.show();

	}
	
	public Pane getBoard() {
		return board;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	private void initBoard() {
		Cell[][] cells = game.getBoardCells();
		for(int row = 0; row < cells[0].length; row++)
		{
			for(int col = 0; col < cells.length; col++)
			{
				board.add(cells[row][col].getCell(), col, row);
			}		   
		}
	}
	
	private void initDigits() {
		digits = new VBox();
		Text _1;
		HBox _1p;
		for(int i = 1; i <= 8; i++ ) {
			_1 = new Text(i+"");
			_1.setFill(Color.WHEAT);
			_1.setFont(Font.font(null, FontWeight.BOLD, 14));
			
			_1p = new HBox(_1);
			_1p.setAlignment(Pos.CENTER);
			_1p.setPrefSize(20, 50);
	
			digits.getChildren().add(_1p);
		}
	}
	
	private void initLetters() {
		letters = new HBox();
		Text _1;
		HBox _1p;
		for(int i = 1; i <= 8; i++ ) {
			_1 = new Text((char)(64+i)+"");
			_1.setFill(Color.WHEAT);
			_1.setFont(Font.font(null, FontWeight.BOLD, 14));
			
			_1p = new HBox(_1);
			_1p.setAlignment(Pos.CENTER);
			_1p.setPrefSize(50, 20);
	
			letters.getChildren().add(_1p);
		}
	}
	
	public static void main(String[] args) {
        launch(args);
    }
	
}