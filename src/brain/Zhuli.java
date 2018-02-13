package brain;

import java.util.List;
import java.util.Random;

import api.ChessGameConnect;
import gui.entities.Move;
import gui.entities.pieces.Bishop;
import gui.entities.pieces.Knight;
import gui.entities.pieces.Piece;
import gui.entities.pieces.Queen;
import gui.entities.pieces.Rook;
import gui.entities.types.Condition;
import gui.entities.types.GameStatus;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
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
		
		//imitation of thinking
		//TODO - do not forget to remove it when it really slows down
		/*for(int i = 0; i < 100000; i++) {
			for(int j = 0; j < 10000; j++) {
				for(int k = 0; k < 1000; k++) {
					;
				}
			}
		}*/
		
		//first, Zhuli checks if she is in danger
		if(api.checkIfItIsCheck(GameStatus.BLACK_MOVE)) {
			
			System.out.println("It is check, now I am checking if it is mate!");
			List<Move> moves = api.checkIfItIsMate(GameStatus.BLACK_MOVE);
			if(moves.size() == 0) {
				api.changeStatus(GameStatus.GAME_OVER);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("");
				alert.setHeaderText("Mate Alert");
				String s = "This is mate buddy! I gave up:-(";
				alert.setContentText(s);
				alert.show();
				return;
			}
			else {
				/*Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("");
				alert.setHeaderText("Check Alert");
				String s = "Oh, my King is uder attack dear?";
				alert.setContentText(s);
				alert.show();*/
				Move saverMove = brain.saveMyKing(moves);
				api.doMove(saverMove);
				return;
			}
			
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
		//if there is no "good" move, then just do random move
		/*while(true) {
			int value = rand.nextInt(pieces.size()); 
			selected = pieces.get(value);
			
			List<Move> possibleMoves = api.getAllPossibleMoves(selected);
			
			if(possibleMoves.size() > 0) {
				int mv = rand.nextInt(possibleMoves.size()); 
				move = possibleMoves.get(mv);
				//replace dummy cell with a real one
				move.setDistination(api.getRealCell(move.getDistination()));
				api.doMove(possibleMoves.get(mv));
				break;
			}
		}*/
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
		
		
		Move move = brain.thinkForTheBestMove(goodMoves);
		
		///////////////////////////////// BRAIN IS PLUGGED IN ////////////////////////////////
		
		//TODO Handle Promotion here
		if(move.getPiece().getType() == PieceType.PAWN && move.getCondition() == Condition.POSSIBLE_PROMOTION) {
			
				//return promotionType;
				Piece[] options = new Piece[4];
				Piece piece = move.getPiece();
				options[0] = new Rook(color, "10"+piece.getId());
				options[1] = new Knight(color, "10"+piece.getId());
				options[2] = new Bishop(color, "10"+piece.getId());
				options[3] = new Queen(color, "10"+piece.getId());
				
				int selection = rand.nextInt(options.length); 
				move.setPiece(options[selection]);
				api.setPieceToRealCell(move.getOriginal(), move.getPiece());
				
		}
		
		return move;
	}
	
	

}
