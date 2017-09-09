package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gui.entities.Cell;
import gui.entities.Move;
import gui.entities.Parameter;
import gui.entities.Piece;
import gui.entities.pieces.Bishop;
import gui.entities.pieces.King;
import gui.entities.pieces.Knight;
import gui.entities.pieces.Pawn;
import gui.entities.pieces.Queen;
import gui.entities.pieces.Rook;
import gui.entities.types.PieceColor;
import gui.entities.types.Requests;
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
import model.ChessGameManager;

public class ChessBoardController {
	private ChessGameManager game;
	public static Cell[][] cells = new Cell[8][8];
	private List<Move> moves = new ArrayList<>();
	private GridPane border = new GridPane();
	private GridPane board;
	private VBox controlPanel = new VBox();
	private VBox digits;
	private HBox letters;
	
	public ChessBoardController(Stage primaryStage, ChessGameManager game) {
		this.game = game;
		board = new GridPane();
		initBoard();
		
		initDigits();
        initLetters();
        
        border.add(digits, 0, 1, 1, 1);
        border.add(letters, 1, 2, 1, 1);
        
		border.setPadding(new Insets(20, 20, 0, 0));
		border.setPrefSize(440, 440);
		border.setStyle("-fx-background-color: #2C2020;");
		border.add(board, 1, 1, 1, 1);
		
		
		controlPanel.getChildren().add(border);
		
		//initPieces();
		initTest();
		
        primaryStage.setScene(new Scene(controlPanel));
        
        
        
	}
	
	

	public Pane getBoard() {
		return board;
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
	
	private void initBoard() {
		int count = 0;
		
		for(int row = 0; row < cells[0].length; row++)
		{
			count++;
			for(int col = 0; col < cells.length; col++)
		   {
		      cells[row][col] = new Cell((count % 2 == 0) ? true : false, this, col, row);
		      
		      board.add(cells[row][col].getCell(), col, row);
		      count++;
		   }		   
		}
	}
	
	public void recordMove(Move move) {
		moves.add(move);
	}
	
	public List<Move> getRecordedMoves() {
		return moves;
	}
	
	public List<Move> getRecordedMovesByColor(PieceColor color) {
		List<Move> colorMoves = new ArrayList<>();
		
		for(Move move : moves) {
			if(move.getPiece().getColor() == color)
				colorMoves.add(move);
		}
		
		return colorMoves;
	}
	
	public List<Move> getRecordedMovesForPieceId(Piece piece) {
		List<Move> pieceMoves = new ArrayList<>();
		
		for(Move move : moves) {
			if(move.getPiece().getId() == piece.getId()) {
				pieceMoves.add(move);
			}
		}
		
		return pieceMoves;
	}
	
	public void clearSelectedCells(Cell exceptThisOne) {
		for(int row = 0; row < cells[0].length; row++)
		{
			for(int col = 0; col < cells.length; col++)
			{
				if(cells[row][col].isSelected() && cells[row][col] != exceptThisOne) 
					cells[row][col].setSelected(false);
			}		   
		}
	}
	
	public void clearSelectedCells() {
		for(int row = 0; row < cells[0].length; row++)
		{
			for(int col = 0; col < cells.length; col++)
			{
				if(cells[row][col].isSelected()) 
					cells[row][col].setSelected(false);
			}		   
		}
	}
	
	
	private void initPieces() {
		//Init white pieces
		cells[1][0].setPiece(new Pawn(PieceColor.BLACK, "1"));
		cells[1][1].setPiece(new Pawn(PieceColor.BLACK, "2"));
		cells[1][2].setPiece(new Pawn(PieceColor.BLACK, "3"));
		cells[1][3].setPiece(new Pawn(PieceColor.BLACK, "4"));
		cells[1][4].setPiece(new Pawn(PieceColor.BLACK, "5"));
		cells[1][5].setPiece(new Pawn(PieceColor.BLACK, "6"));
		cells[1][6].setPiece(new Pawn(PieceColor.BLACK, "7"));
		cells[1][7].setPiece(new Pawn(PieceColor.BLACK, "8"));
		
		cells[0][0].setPiece(new Rook(PieceColor.BLACK, "9"));
		cells[0][1].setPiece(new Knight(PieceColor.BLACK, "10"));
		cells[0][2].setPiece(new Bishop(PieceColor.BLACK, "11"));
		cells[0][3].setPiece(new Queen(PieceColor.BLACK, "12"));
		cells[0][4].setPiece(new King(PieceColor.BLACK, "13"));
		cells[0][5].setPiece(new Bishop(PieceColor.BLACK, "14"));
		cells[0][6].setPiece(new Knight(PieceColor.BLACK, "15"));
		cells[0][7].setPiece(new Rook(PieceColor.BLACK, "16"));
		
		cells[6][0].setPiece(new Pawn(PieceColor.WHITE, "17"));
		cells[6][1].setPiece(new Pawn(PieceColor.WHITE, "18"));
		cells[6][2].setPiece(new Pawn(PieceColor.WHITE, "19"));
		cells[6][3].setPiece(new Pawn(PieceColor.WHITE, "20"));
		cells[6][4].setPiece(new Pawn(PieceColor.WHITE, "21"));
		cells[6][5].setPiece(new Pawn(PieceColor.WHITE, "22"));
		cells[6][6].setPiece(new Pawn(PieceColor.WHITE, "23"));
		cells[6][7].setPiece(new Pawn(PieceColor.WHITE, "24"));
		
		cells[7][0].setPiece(new Rook(PieceColor.WHITE, "25"));
		cells[7][1].setPiece(new Knight(PieceColor.WHITE, "26"));
		cells[7][2].setPiece(new Bishop(PieceColor.WHITE, "27"));
		cells[7][3].setPiece(new Queen(PieceColor.WHITE, "28"));
		cells[7][4].setPiece(new King(PieceColor.WHITE, "29"));
		cells[7][5].setPiece(new Bishop(PieceColor.WHITE, "30"));
		cells[7][6].setPiece(new Knight(PieceColor.WHITE, "31"));
		cells[7][7].setPiece(new Rook(PieceColor.WHITE, "32"));
		
		
		
	}
	
	/**
	 * Just function for testing various scenarios
	 */
	private void initTest() {
		//Init white pieces
		
		cells[0][4].setPiece(new King(PieceColor.BLACK, "1"));
		//cells[1][4].setPiece(new Knight(PieceColor.BLACK));
		cells[2][4].setPiece(new Bishop(PieceColor.BLACK, "2"));
		
		cells[7][0].setPiece(new Rook(PieceColor.WHITE, "3"));
		cells[7][1].setPiece(new Knight(PieceColor.WHITE, "4"));
		cells[7][2].setPiece(new Bishop(PieceColor.WHITE, "5"));
		cells[7][3].setPiece(new Queen(PieceColor.WHITE, "6"));
		cells[7][4].setPiece(new King(PieceColor.WHITE, "7"));
		cells[7][5].setPiece(new Bishop(PieceColor.WHITE, "8"));
		cells[7][6].setPiece(new Knight(PieceColor.WHITE, "9"));
		cells[7][7].setPiece(new Rook(PieceColor.WHITE, "10"));
	}

	public void askChessGame(Requests req, Parameter param) {
		game.whatToDoNext(req, param);
	}



	public Cell getSelectedCell() {
		for(int row = 0; row < cells[0].length; row++)
		{
			for(int col = 0; col < cells.length; col++)
			{
				if(cells[row][col].isSelected()) 
					return cells[row][col];
			}	
		}
		
		return null;
	}
	
	public Cell getRealCell(Cell dummy) {
		return cells[dummy.getRow()][dummy.getCol()];
	}
	
	public Cell[][] getBoardCells() {
		return cells;
	}





}
