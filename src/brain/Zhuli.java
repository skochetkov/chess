package brain;

import java.util.List;
import java.util.Random;

import api.ChessGameConnect;
import game.entities.Move;
import game.entities.pieces.Bishop;
import game.entities.pieces.Knight;
import game.entities.pieces.Piece;
import game.entities.pieces.Queen;
import game.entities.pieces.Rook;
import game.entities.types.Condition;
import game.entities.types.GameStatus;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Zhuli {
	private ChessGameConnect api;
	private PieceColor color;
	private PieceColor opponentColor;
	private Random rand = new Random(); 
	private BrainInterface brain;
	
	public Zhuli(ChessGameConnect api, PieceColor color) {
		this.api = api;
		//initialize with default color (black)
		this.color = color;
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		//initiate and assign brain
		//this brain is just a processor and it doesn't have any memory, it doesn't care of who it belongs to
		//it just calculates the best way to solve the concrete problem
		brain = new Brain1200(api, color);
		//brain = new Brain199(api);
	}
	
	public void setColor(PieceColor color) {
		this.color = color;
	}

	public void myTurn() {
		System.out.println("myTurn");
		if(api.getWhosMoveNow() != color)
			return;
		
		System.out.println("Zhuli is thinking... Please wait....");

		if(brain.checkForExceptions() == Condition.MATE) {
			api.changeStatus(GameStatus.GAME_OVER);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("");
			alert.setHeaderText("Mate Alert");
			String s = "This is mate buddy! I gave up:-(";
			alert.setContentText(s);
			alert.show();
			return;
		}
		
		Move move = getGoodMove();
		Condition condition = move.getCondition() ;
		
		//Now we have (may be) a list of good moves, let's select one
		if(condition != Condition.UNKNOWN) {
			//replace dummy cell with a real one
			move.setDistination(api.getRealCell(move.getDistination()));
			if(move.getCondition() == Condition.MATE) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("");
				alert.setHeaderText("Mate Alert");
				String s = "This is mate buddy! I won!";
				alert.setContentText(s);
				alert.show();
				return;
			}
			
			if(condition == Condition.CASTLING_ON_LEFT || condition == Condition.CASTLING_ON_RIGHT) {
				api.doMove(move);
				api.doMove(move.getFollowingMove());
			}
			else if(condition == Condition.EN_PASSANT_LEFT || condition == Condition.EN_PASSANT_RIGHT) {
				api.doEnPassantMove(move);
			}
			else {
				api.doMove(move);
			}
			//return;
		}
		//this is probably mate
		else {
			api.changeStatus(GameStatus.GAME_OVER);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("");
			alert.setHeaderText("Game is Over. I have nowhere to move.");
			String s = "Game is Over";
			alert.setContentText(s);
			alert.show();
			return;
		}
		
		// One more time to check if I didn't fix the dangerous situation
		Condition conditionAfterMove = brain.checkForExceptions();
		if(conditionAfterMove == Condition.MATE || conditionAfterMove == Condition.CHECK) {
			api.changeStatus(GameStatus.GAME_OVER);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("");
			alert.setHeaderText("Mate Alert");
			String s = "This is mate buddy! I gave up:-(";
			alert.setContentText(s);
			alert.show();
			return;
		}
	}
	
	private List<Move> getGoodMoves() {
		
		return api.getGoodMoves(color);
	}
	
	private Move getGoodMove() {
		System.out.println("getGoodMove: enter");
		
		List<Move> goodMoves = getGoodMoves();
		System.out.println("there are " + goodMoves.size() + " good moves");
		
		if(goodMoves.size() == 1 && goodMoves.get(0).getCondition() == Condition.UNKNOWN)
			return goodMoves.get(0);
		
		///////////////////////////////// PLUG IN BRAIN ///////////////////////////////////////////////////
		
		
		//Move move = brain.thinkForTheBestMove(goodMoves);
		Move move = brain.rankForTheBestMove(color);
		
		///////////////////////////////// BRAIN IS PLUGGED IN ////////////////////////////////
		
		//TODO Handle Promotion here
		if(move.getPiece().getType() == PieceType.PAWN && move.getCondition() == Condition.POSSIBLE_PROMOTION) {
			
				//return promotionType;
				Piece[] options = new Piece[4];
				Piece piece = move.getPiece();
				options[0] = new Rook(api.getBoard(), color, "10"+piece.getId());
				options[1] = new Knight(api.getBoard(), color, "10"+piece.getId());
				options[2] = new Bishop(api.getBoard(), color, "10"+piece.getId());
				options[3] = new Queen(api.getBoard(), color, "10"+piece.getId());
				
				int selection = rand.nextInt(options.length); 
				move.setPiece(options[selection]);
				api.setPieceToRealCell(move.getOriginal(), move.getPiece());
				
		}
		
		return move;
	}
	
	

}
