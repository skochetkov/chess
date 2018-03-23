package util;

import game.entities.Cell;
import game.entities.pieces.Bishop;
import game.entities.pieces.Knight;
import game.entities.pieces.Queen;
import game.entities.pieces.Rook;
import game.entities.types.PieceColor;
import game.entities.types.Requests;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ChessApplication;
import model.ChessGameManager;

public class UIUtil {
	public static void showPromotionBox(ChessGameManager manager, Cell piece, Cell newLocation) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(ChessApplication.primaryStage);
		VBox promotionVbox = new VBox(20);
		promotionVbox.getChildren().add(new Text("Select a piece for promotion"));
		Scene dialogScene = new Scene(promotionVbox, 200, 100);
		dialog.setScene(dialogScene);
		dialog.show();
		
		PieceColor color = piece.getPiece().getColor();
		GridPane gpBoard = new GridPane();
		gpBoard.setPadding(new Insets(0, 10, 10, 10));
		//gpBoard.setPrefSize(440, 440);
		//gpBoard.setStyle("-fx-background-color: #2C2020;");
		//gpBoard.add(board, 1, 1, 1, 1);
		int row = 0;
		int col = 0;
				
		Cell[] cells = new Cell[4];
		for(col = 0; col < cells.length; col++)
		{
			cells[col] = new Cell((col % 2 == 0) ? true : false, col, row, color);
			Cell c = cells[col];
			cells[col].getCell().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {
					dialog.hide();
					manager.replacePiece(newLocation, c, piece);
					//TODO check if it works correctly
					//controlBoard.askChessGame(Requests.I_WAS_PROPOSED_BY_USER, newLocation);
					manager.whatToDoNext(Requests.I_WAS_PROPOSED_BY_USER, newLocation);
				}
			});
			gpBoard.add(cells[col].getCell(), col, row);
		}	
		
		cells[0].setPiece(new Rook(manager.getController(), color, "10"+piece.getPiece().getId()));
		cells[1].setPiece(new Knight(manager.getController(), color, "10"+piece.getPiece().getId()));
		cells[2].setPiece(new Bishop(manager.getController(), color, "10"+piece.getPiece().getId()));
		cells[3].setPiece(new Queen(manager.getController(), color, "10"+piece.getPiece().getId()));
		
		//gpBoard.add(cells[0].getCell(), 0, 0);
		//gpBoard.add(cells[1].getCell(), 1, 0);
		//gpBoard.add(cells[2].getCell(), 2, 0);
		//gpBoard.add(cells[3].getCell(), 3, 0);
		promotionVbox.getChildren().add(gpBoard);
	}
}
