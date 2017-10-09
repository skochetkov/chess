package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import api.ChessGameConnect;
import gui.entities.Cell;
import gui.entities.Move;
import gui.entities.Piece;
import gui.entities.pieces.Bishop;
import gui.entities.pieces.Knight;
import gui.entities.pieces.Queen;
import gui.entities.pieces.Rook;
import gui.entities.types.Condition;
import gui.entities.types.GameStatus;
import gui.entities.types.MoveType;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.UIUtil;

public class Zhuli {
	private ChessGameConnect api;
	private PieceColor color;
	private PieceColor opponentColor;
	private Random rand = new Random(); 
	
	public Zhuli(ChessGameConnect api, PieceColor color) {
		this.api = api;
		//initialize with default color (black)
		this.color = color;
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
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
				saveMyKing(moves);
				return;
			}
			
		}
		List<Cell> pieces = api.getPiecesByColor(color);
		
		Move move = getGoodMove(pieces);
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
			alert.setHeaderText("Game is Over Alert");
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
	
	private Move getGoodMove(List<Cell> pieces) {
		System.out.println("getGoodMove: enter");
		List<Move> goodMoves = new ArrayList<>();
		Cell opponentKing = api.getPiecesByTypeAndColor(PieceType.KING, opponentColor).get(0);
		Cell myKing = api.getPiecesByTypeAndColor(PieceType.KING, color).get(0);
		List<Move> possibleMoves = new ArrayList<>();
		
		//first dummiest version of AI
		//find first good move
		for(Cell piece: pieces) {
			//if no moves or no attack, then we ignore those
			if(piece.equals(myKing)) {
				possibleMoves = api.getAllPossibleSafeMoves(piece);
				Cell newLocation = new Cell(piece.getCol() - 2, piece.getRow());
				MoveType castlingType = api.isCastlingAllowed(piece, newLocation);
				if(castlingType == MoveType.CASTLING_ON_RIGHT) {
					Move castlingOnRight = new Move(piece.getPiece(), piece, newLocation, Condition.CASTLING_ON_RIGHT);
					
					Cell rook = new Cell(0, myKing.getRow());
					Cell realRook = api.getRealCell(rook);
					
					
					if(realRook != null && realRook.getPiece() != null) {
						rook.moveRight(3);
						castlingOnRight.setFollowingMove(new Move(realRook.getPiece(), realRook, rook, Condition.MOVE));
						possibleMoves.add(castlingOnRight);
					}
				}
				
				newLocation = new Cell(piece.getCol() + 2, piece.getRow());
				castlingType = api.isCastlingAllowed(piece, newLocation);
				if(castlingType == MoveType.CASTLING_ON_LEFT) {
					Move castlingOnLeft = new Move(piece.getPiece(), piece, newLocation, Condition.CASTLING_ON_LEFT);
					Cell rook = new Cell(7, myKing.getRow());
					Cell realRook = api.getRealCell(rook);
					
					
					if(realRook != null && realRook.getPiece() != null) {
						rook.moveLeft(2);
						castlingOnLeft.setFollowingMove(new Move(realRook.getPiece(), realRook, rook, Condition.MOVE));
						possibleMoves.add(castlingOnLeft);
					}
				}
			}
			else {
				possibleMoves = api.getAllPossibleMoves(piece);
			}
			
			System.out.println("Possible moves for " + piece.getPiece().getType() + " ("+piece.getNotation()+"): " + possibleMoves.size());
			
			if(possibleMoves.size() == 0) {
				continue;
			}
			
			//THIS IS BIG TODO
			for (Move m : possibleMoves) {
				Condition cond = m.getCondition();
				//check if the move is not very bad
				if(!piece.equals(myKing) && isMoveVeryBad(m, myKing)) {
					continue;
				}
				//check if castling puts the king under attack
				else if(cond == Condition.CASTLING_ON_LEFT || cond == Condition.CASTLING_ON_RIGHT) {
					if(isMoveVeryBad(m)) {
						continue;
					}
				}
				
				if(m.getCondition() == Condition.ATTACK) {
					//if by chance you can eat... well... the king... you can do it
					if(m.getDistination().equals(opponentKing)) {
						//change condition from attack to mate
						m.setCondition(Condition.MATE);
						return m;
					}
						
					goodMoves.add(m);
				}
				
				goodMoves.add(m);
			}
		}
		
		if(goodMoves.size() == 0)
			return new Move(Condition.UNKNOWN);
		
		///////////////////////////////// PLUG IN BRAIN ///////////////////////////////////////////////////
		
		
		Move move = brain1(goodMoves);
		
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
	
	/**
	 * CAUTION! WORK WITH BRAIN! 
	 * This is the first non random brain where we assign some kind of weight to moves
	 * @param goodMoves
	 * @return
	 */
	private Move brain1(List<Move> goodMoves) {
		// The first dumbest idea - random move
		//int value = rand.nextInt(goodMoves.size()); 
		//Move move = goodMoves.get(value);
		
		//TODO just for experiment we assign some kind of weight to moves
		//do some preparation before weighting
		System.out.println("GOOD MOVES:");
						
		for(Move m: goodMoves) {
			System.out.println(m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
			if(m.getPiece().getType() == PieceType.PAWN) {
				MoveType promotionType = isPossiblyPromoted(m.getOriginal(), m.getDistination());
				
				if(promotionType == MoveType.POSSIBLE_PROMOTION) {
					m.setCondition(Condition.POSSIBLE_PROMOTION);
				}
			}
		}
						
		// Assign weight
		for(Move m: goodMoves) {
			Condition cond = m.getCondition();
							
			if(cond == Condition.POSSIBLE_PROMOTION)
				m.setWeight(8);
			else if(cond == Condition.CASTLING_ON_LEFT || cond == Condition.CASTLING_ON_RIGHT)
				m.setWeight(6);
			else if(cond == Condition.ATTACK)
				m.setWeight(5);
			else if(cond == Condition.MOVE)
				m.setWeight(4);
			else
				m.setWeight(1);
		}
						
		Move move = goodMoves.get(0);
		// Take the one with highest weight
		for(Move m: goodMoves) {
			if(m.getWeight() > move.getWeight()) {
				move = m;
			}
		}
		return move;
	}

	/**
	 * Checks if the move is very bad, e.g, opening my king to attacker
	 * All other not good moves (e.g, opening Queen for attacker) will be handled in other place because it might be intention to do that way
	 * @param m
	 * @return
	 */
	private boolean isMoveVeryBad(Move move, Cell weakPiece) {
		return api.isMoveVeryBad(move, weakPiece);
	}
	
	private boolean isMoveVeryBad(Move move) {
		return api.isMoveVeryBad(move);
	}
	
	public MoveType isPossiblyPromoted(Cell selected, Cell newLocation) {
		MoveType type = MoveType.ILLIGAL;
		
		if(selected.getPiece().getType() == PieceType.PAWN) {
			if(newLocation.getRow() == 0 || newLocation.getRow() == 7) {
				//UIUtil.showPromotionBox(null, selected, newLocation) ;
				type = MoveType.POSSIBLE_PROMOTION;
			}
		}
		return type;
	}

	//TODO improve, it is very dummy solution right now
	private void saveMyKing(List<Move> safeMoves) {
		System.out.println("It is not mate, it is good, now I need to choose a good move to save my king");
		int value = rand.nextInt(safeMoves.size()); 
		api.doMove(safeMoves.get(value));
		/*Cell king = api.getPiecesByTypeAndColor(PieceType.KING, color).get(0);
		
		List<Cell> kingsMoves = api.getAllSafeMoves(king);
		List<Cell> kingsCaptures = api.getAllSafeCaptures(king); 
		List<Cell> whoEndangersKing = api.getWhoEndangersMe(king);//TODO - is it possible to have more than one here? If yes, we have a problem...
		List<Cell> whoCanSaveKing = api.getWhoCanDefendMe(king);
		
		//check if there is a saver, use it
		for(Cell attacker: whoEndangersKing) {
			if(whoCanSaveKing.size() > 0) {
				
				for(Cell saver : whoCanSaveKing) {
					List<Move> possibleMoves = api.getAllPossibleMoves(saver);
					for (Move m : possibleMoves) {
						if(m.getCondition() == Condition.ATTACK) {
							if(attacker.equals(m.getDistination())) {
								System.out.println("I found a saver");
								api.doMove(m);
								return;
							}
						}
					}
				}
			}
		}
		
		System.out.println("No saver found, I will try to find if my king can defend himself");
		//TODO move if you have an option
		System.out.println("Checking if there is place to move...");
		
		if(kingsMoves.size() > 0) {
			System.out.println("There is place to move. I am moving...");
			int value = rand.nextInt(kingsMoves.size()); 
			Move m = new Move(king.getPiece(), king, kingsMoves.get(value), Condition.ATTACK );
			api.doMove(m);
			return;
		}
		
		System.out.println("Nowhere to move, ");
		//if there is no savers and nowhere to move, try to save yourself
		for(Cell capture : kingsCaptures) {
			for(Cell attacker: whoEndangersKing) {
				if(capture.equals(attacker)) {
					System.out.println("Okey, my king can attack!");
					Move m = new Move(king.getPiece(), king, attacker, Condition.ATTACK );
					api.doMove(m);
					return;
				}
			}
		}
		
		System.out.println("Well, perheps it is mate... ");*/
	}

}
