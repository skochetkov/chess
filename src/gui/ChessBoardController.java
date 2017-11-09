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
import gui.entities.types.Condition;
import gui.entities.types.GameStatus;
import gui.entities.types.MoveType;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
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
import tests.TestCases;
import util.UIUtil;

public class ChessBoardController {
	private ChessGameManager game;
	public static Cell[][] cells = new Cell[8][8];
	private List<Move> moves = new ArrayList<>();
	
	
	public ChessBoardController(ChessGameManager game) {
		this.game = game;
		initBoard();
		
		initPieces();
		//initTests();
	}
	
	private void initTests() {
		
		TestCases testCase  = TestCases.DEFAULT;
		
		//Select test case
		//testCase = TestCases.CASTLING_BLACK;
		//testCase = TestCases.CASTLING_WHITE;
		//testCase = TestCases.PAWN_ADVANCES;
		//testCase = TestCases.PAWN_ADVANCES_BLACK;
		//testCase = TestCases.EN_PASSANT_WHITE;
		//testCase = TestCases.EN_PASSANT_BLACK;
		testCase = TestCases.WEIGHT_BLACK;
		TestController.getTestCase(testCase, cells);
		
	} 

	private void initBoard() {
		int count = 0;
		
		for(int row = 0; row < cells[0].length; row++)
		{
			count++;
			for(int col = 0; col < cells.length; col++)
		   {
		      cells[row][col] = new Cell((count % 2 == 0) ? true : false, this, col, row);
		      
		      count++;
		   }		   
		}
	}
	
	public void recordMove(Move move) {
		move.getPiece().setMoved();
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

	public void replacePiece(Cell promotedPiece, Cell piece, Cell oldPiece) {
		Cell newCell = getRealCell(promotedPiece);
		newCell.setPiece(piece.getPiece());
		//cells[oldPiece.getRow()][oldPiece.getCol()].resetPiece();
		//cells[oldPiece.getRow()][oldPiece.getCol()].setSelected(false);
		game.doMove(new Move(newCell.getPiece(), newCell, oldPiece, Condition.MOVE));
		
	}

	public void setPieceToRealCell(Cell original, Piece piece) {
		cells[original.getRow()][original.getCol()].setPiece(piece);
	}

	public List<Move> checkIfItIsMate(GameStatus status) {
		PieceColor color = null;
		PieceColor opponentColor = null;
		
		if(status == GameStatus.WHITE_MOVE) {
			color = PieceColor.WHITE;
			opponentColor = PieceColor.BLACK;
		}
		else {
			color = PieceColor.BLACK;
			opponentColor = PieceColor.WHITE;
		}
		
		//Get the king
		Cell king = getPiecesByTypeAndColor(PieceType.KING, color).get(0);
		System.out.println("check if king can be defended by his pieces");
		//TODO (exclude piece itself) if king can be defended by his pieces except the king itself
		List<Move>  defendingMoves = getWhoCanDefendMe(king, color);
		
		if(defendingMoves.size() > 0) {
			System.out.println("I can be saved!");
		}
		//get all king's moves and captures
		List<Cell> safeMoves = getAllSafeMoves(king);
		List<Cell> safeCaptures = getAllSafeCaptures(king);
		System.out.println("Checking if the king has safe moves or safe captures....");
		
		System.out.println("defendingMoves.size() : "+defendingMoves.size()+" safeMoves.size() : "+safeMoves.size()+" safeCaptures.size() : "+safeCaptures.size());
		//rear case where there is no way to move
		if(defendingMoves.size() > 0 || safeMoves.size() > 0 || safeCaptures.size() > 0) {
			if(safeMoves.size() > 0) {
				System.out.println("there is safe moves for the king");
				for(Cell mv : safeMoves) {
					defendingMoves.add(new Move(king.getPiece(), king, mv, Condition.MOVE));
				}
			}
			
			if(safeCaptures.size() > 0) {
				System.out.println("there is safe captures for the king");
				for(Cell mv : safeCaptures) {
					defendingMoves.add(new Move(king.getPiece(), king, mv, Condition.ATTACK));
				}
			}
			
			return defendingMoves;
		}
	
		return defendingMoves;
	}

	public List<Cell> getPiecesByTypeAndColor(PieceType type, PieceColor color) {
		List<Cell> pieces = new ArrayList<>();
		
		List<Cell> allPieces = getPiecesByColor(color);
		
		for(Cell p : allPieces) {
			if(p.getPiece().getType() == type) {
				pieces.add(p);
			}
		}
		
		return pieces;
	}
	
	public List<Cell> getPiecesByColor(PieceColor color) {
		List<Cell> pieces = new ArrayList<>();
		Cell[][] cells = getBoardCells();
		
		for(int row = 0; row < cells[0].length; row++)
		{
			for(int col = 0; col < cells.length; col++)
			{
				if(cells[row][col].getPiece() != null && cells[row][col].getPiece().getColor() == color) 
					pieces.add(cells[row][col]);
			}	
		}
		
		return pieces;
	}

	public List<Cell> getAttackingPiecesByColor(Cell newPieceLocation, Cell oldPieceLocation, PieceColor color) {
		List<Cell> pieces = new ArrayList<>();
		
		List<Cell> allPieces = getPiecesByColor(color);
		
		for(Cell p : allPieces) {
			
			if(p.getPiece().isItEatable(p, newPieceLocation, oldPieceLocation)) {
				pieces.add(p);
			}
		}
		
		return pieces;
	}
	
	public List<Move> getAllPossibleMoves(Cell selected) {
		Piece piece = selected.getPiece();
		List<Move> allPossibleMoves = new ArrayList<Move>(); 
		
		List<Cell> moves = piece.getAllMoves(selected, false);
		
		for(Cell move : moves) {
			allPossibleMoves.add(new Move(piece, selected, move, Condition.MOVE));
		}
		
		List<Cell> captureMoves = piece.getAllCaptures(selected, false);
		
		for(Cell move : captureMoves) {
			allPossibleMoves.add(new Move(piece, selected, move, Condition.ATTACK));
		}
		
		return allPossibleMoves;
	}
	
	public List<Move> getAllPossibleSafeMoves(Cell selected) {
		Piece piece = selected.getPiece();
		List<Move> allPossibleMoves = new ArrayList<Move>(); 
		
		List<Cell> moves = getAllSafeMoves(selected);
		
		for(Cell move : moves) {
			allPossibleMoves.add(new Move(piece, selected, move, Condition.MOVE));
		}
		
		List<Cell> captureMoves = getAllSafeCaptures(selected);
		
		for(Cell move : captureMoves) {
			allPossibleMoves.add(new Move(piece, selected, move, Condition.ATTACK));
		}
		
		return allPossibleMoves;
	}
	
	

	public List<Cell> getAllSafeCaptures(Cell piece) {
		List<Cell> allCaptures = piece.getPiece().getAllCaptures(piece, false);
		List<Cell> safeCaptures = new ArrayList<>();
		PieceColor color = null;
		
		if(piece.getPiece().getColor() == PieceColor.BLACK) 
			color = PieceColor.WHITE;
		else
			color = PieceColor.BLACK;
		
		boolean isSafeCapture = true;
		//TODO - something wrong here - fix it
		for(Cell myCapture : allCaptures) {
			List<Cell> attackers = getAttackingPiecesByColor(myCapture, piece, color);
			//if there is at least one attacker who can eat, it is not safe
			if(attackers.size() > 0) 
				isSafeCapture = false;
			
			/*for(Cell attacker: attackers) {
				//skip if attacker is victim 
				if(myCapture.equals(attacker)) continue;
				
				List<Cell> attackerCaptures = attacker.getPiece().getAllCaptures(attacker, false);
				for(Cell attackerCapture : attackerCaptures) {
					if(myCapture.equals(attackerCapture))
						isSafeCapture = false;
				}
			}*/
			
			if(isSafeCapture) {
				safeCaptures.add(myCapture);
				isSafeCapture = true;
			}
		}
		
		return safeCaptures;
	}
	
	public List<Cell> getAllSafeMoves(Cell piece) {
		List<Cell> safeMoves = new ArrayList<>();
		List<Cell> allMoves = piece.getPiece().getAllMoves(piece, false);
		PieceColor opponentColor = null;
		
		if(piece.getPiece().getColor() == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		//List<Cell> attackers = getAttackingPiecesByColor(opponentColor);
		List<Cell> potentialAttackers = getPiecesByColor(opponentColor); 
		boolean isSafeMove = true;
		
		//TODO - check if it works
		for(Cell potentialMove : allMoves) {
			isSafeMove = true;
			for(Cell attacker: potentialAttackers) {
				List<Cell> captureMoves = attacker.getPiece().getAllPotentialCaptures(attacker);
				for(Cell attackerMove : captureMoves) {
					if(attackerMove.equals(potentialMove)) {
						if(attacker.getPiece().isItEatable(attacker, potentialMove, piece)) {
							isSafeMove = false;
							break;
						}
					}
				}
				if(!isSafeMove)
					break;
			}
			
			if(isSafeMove) {
				safeMoves.add(potentialMove);
				isSafeMove = true;
			}
		}
		
		return safeMoves;
	}
	
	public List<Move> getGoodMoves(PieceColor color) {
		List<Cell> pieces = getPiecesByColor(color);
		PieceColor opponentColor = null;
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		List<Move> goodMoves = new ArrayList<>();
		Cell opponentKing = getPiecesByTypeAndColor(PieceType.KING, opponentColor).get(0);
		Cell myKing = getPiecesByTypeAndColor(PieceType.KING, color).get(0);
		List<Move> possibleMoves = new ArrayList<>();
		
		//first dummiest version of AI
		//find first good move
		for(Cell piece: pieces) {
			//if no moves or no attack, then we ignore those
			if(piece.equals(myKing)) {
				possibleMoves = getAllPossibleSafeMoves(piece);
				Cell newLocation = new Cell(piece.getCol() - 2, piece.getRow());
				Condition castlingType = isCastlingAllowed(piece, newLocation);
				if(castlingType == Condition.CASTLING_ON_RIGHT) {
					Move castlingOnRight = new Move(piece.getPiece(), piece, newLocation, castlingType);
					
					Cell rook = new Cell(0, myKing.getRow());
					Cell realRook = getRealCell(rook);
					
					
					if(realRook != null && realRook.getPiece() != null) {
						rook.moveRight(3);
						castlingOnRight.setFollowingMove(new Move(realRook.getPiece(), realRook, rook, Condition.MOVE));
						possibleMoves.add(castlingOnRight);
					}
				}
				
				newLocation = new Cell(piece.getCol() + 2, piece.getRow());
				castlingType = isCastlingAllowed(piece, newLocation);
				if(castlingType == Condition.CASTLING_ON_LEFT) {
					Move castlingOnLeft = new Move(piece.getPiece(), piece, newLocation, castlingType);
					Cell rook = new Cell(7, myKing.getRow());
					Cell realRook = getRealCell(rook);
					
					
					if(realRook != null && realRook.getPiece() != null) {
						rook.moveLeft(2);
						castlingOnLeft.setFollowingMove(new Move(realRook.getPiece(), realRook, rook, Condition.MOVE));
						possibleMoves.add(castlingOnLeft);
					}
				}
			}
			else {
				possibleMoves = getAllPossibleMoves(piece);
				//Add En Passant
				if(piece.getPiece().getType() == PieceType.PAWN) {
					Move enPassant = isEnPassantAllowed(piece);
					
					if(enPassant.getCondition() == Condition.EN_PASSANT_LEFT || enPassant.getCondition() == Condition.EN_PASSANT_RIGHT) {
						possibleMoves.add(enPassant);
					}
				}
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
				
				System.out.println(m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
				if(m.getPiece().getType() == PieceType.PAWN) {
					MoveType promotionType = isPossiblyPromoted(m.getOriginal(), m.getDistination());
						
					if(promotionType == MoveType.POSSIBLE_PROMOTION) {
						m.setCondition(Condition.POSSIBLE_PROMOTION);
						//goodMoves.add(m);
						//continue;
					}
						
				}
				//check if castling puts the king under attack
				else if(cond == Condition.CASTLING_ON_LEFT || cond == Condition.CASTLING_ON_RIGHT) {
					if(isMoveVeryBad(m)) {
						continue;
					}
				}
				
				/*if(m.getCondition() == Condition.ATTACK) {
					//if by chance you can eat... well... the king... you can do it
					if(m.getDistination().equals(opponentKing)) {
						//change condition from attack to mate
						m.setCondition(Condition.MATE);
						goodMoves.add(m);
						return goodMoves;
					}
						
					//goodMoves.add(m);
				}*/
				
				goodMoves.add(m);
			}
		}
		
		if(goodMoves.size() == 0)
			goodMoves.add(new Move(Condition.UNKNOWN));
		
		return goodMoves;
	}
	
	public List<Cell> getWhoEndangersMe(Cell me) {
		List<Cell> pieces = new ArrayList<>();
		PieceColor color;
		
		if(me.getPiece().getColor() == PieceColor.BLACK) 
			color = PieceColor.WHITE;
		else
			color = PieceColor.BLACK;
		
		List<Cell> opponentsPieces = getPiecesByColor(color);
		
		for(Cell p : opponentsPieces) {
			List<Cell> captureMoves = p.getPiece().getAllCaptures(p, false);
			if(captureMoves.size() == 0)
				continue;
			
			for(Cell cm : captureMoves) {
				if(cm.equals(me)) {
					pieces.add(p);
					break;
				}
			}
		}
		
		return pieces;
	}
	
	/**
	 * Use cases:
	 * 	- a piece can be attacked by more than one opponents figure - need to check if all attackers can be neutralized 
	 *  - any defender can defend by more than one moves, so we return list of moves, and not only defenders
	 * @param piece
	 * @param color
	 * @return
	 */
	public List<Move> getWhoCanDefendMe(Cell piece, PieceColor color) {
		List<Cell> listOfAllWhoAttacksMe = getWhoEndangersMe(piece);
		List<Cell> listOfAllMyPieces = getPiecesByColor(color);
		List<Move> listOfMyDefendersMoves = new ArrayList<>();
		
		//list of all attackers one by one
		for(Cell attacker : listOfAllWhoAttacksMe) {
			for(Cell myPiece : listOfAllMyPieces) {
				if(myPiece.equals(piece)) continue;
				//check if one or more my pieces can beat attacker
				List<Cell> captureMoves = myPiece.getPiece().getAllCaptures(myPiece, false);
				for(Cell cm : captureMoves) {
					if(cm.equals(attacker)) {
						listOfMyDefendersMoves.add(new Move(myPiece.getPiece(), myPiece, cm, Condition.ATTACK));
					}
				}
				
				//We cannot block if the attacker is pawn, knight or king
				if(attacker.getPiece().getType() == PieceType.PAWN ||
						attacker.getPiece().getType() == PieceType.KNIGHT ||
						attacker.getPiece().getType() == PieceType.KING) {
					continue;
				}
				
				//king cannot defend itself by blocking
				if(myPiece.getPiece().getType() == PieceType.KING) continue;
				//check if one of my piece can defend me by blocking the attacker
				List<Cell> blockingMoves = myPiece.getPiece().getAllMoves(myPiece, false);
				List<Cell> path = getPath(piece, attacker);
				for(Cell cm : blockingMoves) {
					//Move move = new Move(cm.getPiece(), myPiece, cm, Condition.BLOCK);
					//if(!canMoveDefendMe(piece, attacker, move)) {
					//	listOfMyDefendersMoves.add(move);
					//}
					for(Cell p : path) {
						if(p.equals(cm)) {
							listOfMyDefendersMoves.add(new Move(cm.getPiece(), myPiece, cm, Condition.BLOCK));
						}
					}
				}
			}
			
		}

		return listOfMyDefendersMoves;
	}
	
	/**
	 * Bring the list of all defending pieces and their defending moves except piece itself
	 * @param me
	 * @return
	 */
	public List<Cell> getWhoCanDefendMe(Cell me) {
		List<Cell> listOfAllWhoAttacksMe = getWhoEndangersMe(me);
		List<Cell> listOfAllMyPieces = getPiecesByColor(me.getPiece().getColor());
		List<Cell> listOfWhoCanDefendMe = new ArrayList<>();
		
		//check if one of my pieces can defend me
		for(Cell myPiece : listOfAllMyPieces) {
			List<Cell> captureMoves = myPiece.getPiece().getAllCaptures(myPiece, false);
			if(captureMoves.size() == 0)
				continue;
			
			for(Cell cm : captureMoves) {
				for(Cell attacker : listOfAllWhoAttacksMe) {
					if(cm.equals(attacker)) {
						listOfWhoCanDefendMe.add(myPiece);
					}
				}
			}
		}
		return listOfWhoCanDefendMe;
	}
	
	public Move isMoveLegal(Cell selected, Cell newLocation) {
		
		//take its figure
		Piece piece = selected.getPiece();
		
		if(selected.getPiece().getType() == PieceType.PAWN) {
			MoveType promotionType = isPromoted(selected, newLocation);
			if(promotionType == MoveType.PROMOTION) {
				return new Move(Condition.POSSIBLE_PROMOTION);
			}
			
			Move enPassant = isEnPassantAllowed(selected);
			
			if( (enPassant.getCondition() == Condition.EN_PASSANT_LEFT || enPassant.getCondition() == Condition.EN_PASSANT_RIGHT) && enPassant.getDistination().equals(newLocation) ) {
				return enPassant;
			}
		}
		
		List<Cell> moves = piece.getAllMoves(selected, false);
		
		for(Cell move : moves) {
			//if new location is in the list of legal moves and it is not occupied by other pieces
			if(move.equals(newLocation) && newLocation.isEmpty()) {
				return new Move(Condition.MOVE);
			}
		}
		
		List<Cell> captureMoves = piece.getAllCaptures(selected, false);
		
		for(Cell move : captureMoves) {
			//if new location is in the list of legal moves and it is not occupied by other pieces
			if(move.equals(newLocation) && !newLocation.isEmpty() && selected.getPiece().getColor() != newLocation.getPiece().getColor())
				return new Move(Condition.MOVE);
		}
		
		// there are a few exceptions here (it is better to handle them here instead of delegating to piece's logic)
		//Get the king
		//1. Castling
		if(selected.getPiece().getType() == PieceType.KING) {
			Condition castlingType = isCastlingAllowed(selected, newLocation);
			if(castlingType == Condition.CASTLING_ON_LEFT || castlingType == Condition.CASTLING_ON_RIGHT) {
				return  new Move(castlingType);
			}
		}
		return new Move(Condition.UNKNOWN);
	}
	
	public MoveType isPromoted(Cell selected, Cell newLocation) {
		MoveType type = MoveType.ILLIGAL;
		
		if(selected.getPiece().getType() == PieceType.PAWN) {
			if(newLocation.getRow() == 0 || newLocation.getRow() == 7) {
				UIUtil.showPromotionBox(this, selected, newLocation) ;
				type = MoveType.PROMOTION;
			}
		}
		return type;
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
	
	public boolean checkIfItIsCheck(GameStatus status) {
		boolean isCheck = false;
		PieceColor color = null;
		PieceColor opponentColor = null;
		
		if(status == GameStatus.WHITE_MOVE) {
			color = PieceColor.WHITE;
			opponentColor = PieceColor.BLACK;
		}
		else {
			color = PieceColor.BLACK;
			opponentColor = PieceColor.WHITE;
		}
		
		List<Cell> allPieces = getPiecesByColor(opponentColor);
		
		for(Cell p : allPieces) {
			List<Cell> captureMoves = p.getPiece().getAllCaptures(p, false);
			if(captureMoves.size() == 0)
				continue;
			
			for(Cell c : captureMoves) {
				if(getRealCell(c).getPiece().getType() == PieceType.KING)  {
					isCheck = true;
					break;
				}
			}
			if(isCheck) break;
		}
		
		return isCheck;
	}
	
	public Move isEnPassantAllowed(Cell selected) {
		PieceColor opponentColor = null;
		PieceColor color = selected.getPiece().getColor();
		Move enPassantMove = new Move(Condition.UNKNOWN);
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		if(selected.getPiece().getType() != PieceType.PAWN) 
			return new Move(Condition.UNKNOWN);
		
		List<Move> recordedMoves = getRecordedMovesByColor(opponentColor);
		
		if(recordedMoves.size() < 1)
			return new Move(Condition.UNKNOWN);
		
		Move lastOppenentMove = recordedMoves.get(recordedMoves.size() - 1);
		
		//if last opponents move is not pawn and it is not its first move ignore it
		if(lastOppenentMove.getPiece().getType() != PieceType.PAWN)
			return new Move(Condition.UNKNOWN);
		
		int numberOfMoves = 0;
		
		for(Move rm : recordedMoves) {
			if(rm.getPiece().getId() == lastOppenentMove.getPiece().getId())
				numberOfMoves++;
			
		}
		//it moved before last move
		if(numberOfMoves > 1)
			return new Move(Condition.UNKNOWN);
		
		int col = selected.getCol();
		int row = selected.getRow();
		int oppCol = lastOppenentMove.getOriginal().getCol();
		int oppRow = lastOppenentMove.getOriginal().getRow();
		
		//white right
		if(color == PieceColor.WHITE && (col == (lastOppenentMove.getOriginal().getCol() - 1)) &&
				(row == lastOppenentMove.getDistination().getRow())	) {
			enPassantMove = new Move(selected.getPiece(), selected, new Cell(oppCol, oppRow + 1), Condition.EN_PASSANT_RIGHT ); 
		}
		//white left
		else if(color == PieceColor.WHITE && (col == (lastOppenentMove.getOriginal().getCol() + 1)) &&
				(row == lastOppenentMove.getDistination().getRow())	) {
			enPassantMove = new Move(selected.getPiece(), selected, new Cell(oppCol, oppRow + 1), Condition.EN_PASSANT_LEFT ); 
		}
		//black right
		else if(color == PieceColor.BLACK && (col == (lastOppenentMove.getOriginal().getCol() + 1)) &&
				(row == lastOppenentMove.getDistination().getRow())	) {
			enPassantMove = new Move(selected.getPiece(), selected, new Cell(oppCol, oppRow - 1), Condition.EN_PASSANT_RIGHT ); 
		}
		//black left
		else if(color == PieceColor.BLACK && (col == (lastOppenentMove.getOriginal().getCol() - 1)) &&
				(row == lastOppenentMove.getDistination().getRow())	) {
			enPassantMove = new Move(selected.getPiece(), selected, new Cell(oppCol, oppRow - 1), Condition.EN_PASSANT_LEFT ); 
		}
		
		return enPassantMove;
	}
	
	public Condition isCastlingAllowed(Cell selected, Cell newLocation) {
		Condition type = Condition.UNKNOWN;
		
		if(selected.getPiece().getType() == PieceType.KING) {
			List<Move> recordedMoves = getRecordedMovesForPieceId(selected.getPiece());
			//if no recorded moves for king and for rook
			if(recordedMoves.isEmpty() && newLocation.isEmpty() 
					&& (newLocation.getRow() == selected.getRow()) && 
					(newLocation.getCol() == (selected.getCol() - 2) || (newLocation.getCol() == selected.getCol() + 2))) {
				
				Cell rook = null;
				boolean othersEmpty = false;
				//if castling is on the left side, check if there is a rook is there
				if(newLocation.getCol() == (selected.getCol() - 2)) {
					rook = new Cell(0, selected.getRow());
					
					if(getRealCell(rook).getPiece() == null || getRealCell(rook).getPiece().getType() != PieceType.ROOK)
						return Condition.UNKNOWN;
					
					Cell one = new Cell(1, selected.getRow());
					Cell three = new Cell(3, selected.getRow());
					if(getRealCell(one).isEmpty() && getRealCell(three).isEmpty()) {
						othersEmpty = true;
						//if it is on the top
						if(selected.getRow() == 0)
							type = Condition.CASTLING_ON_RIGHT;
						else
							type = Condition.CASTLING_ON_LEFT;
					}
					
				}
				else {
					rook = new Cell(7, selected.getRow());
					
					if(getRealCell(rook).getPiece() == null || getRealCell(rook).getPiece().getType() != PieceType.ROOK)
						return Condition.UNKNOWN;
					Cell six = new Cell(6, selected.getRow());
					if(getRealCell(six).isEmpty()) {
						othersEmpty = true;
						//if it is on the top
						if(selected.getRow() == 0)
							type = Condition.CASTLING_ON_LEFT;
						else
							type = Condition.CASTLING_ON_RIGHT;
					}
				}
				
				if(getRealCell(rook).getPiece() != null && getRealCell(rook).getPiece().getType() == PieceType.ROOK && getRealCell(rook).getPiece().getColor() == selected.getPiece().getColor()) {
					
					//Last check if King is not under attack when it moves
					//Top right corner
					Cell start = new Cell(selected.getCol(), selected.getRow());
					Cell next = new Cell(selected.getCol(), selected.getRow());
					Cell last = new Cell(selected.getCol(), selected.getRow());
					
					if((type == Condition.CASTLING_ON_LEFT && selected.getRow() == 0) || (type == Condition.CASTLING_ON_RIGHT && selected.getRow() == 7)) {
						next = new Cell(selected.getCol() + 1, selected.getRow());
						last = new Cell(selected.getCol() + 2, selected.getRow());
					}
					else if((type == Condition.CASTLING_ON_RIGHT && selected.getRow() == 0) || (type == Condition.CASTLING_ON_LEFT && selected.getRow() == 7)) {
						next = new Cell(selected.getCol() - 1, selected.getRow());
						last = new Cell(selected.getCol() - 2, selected.getRow());
					}
					
					Move startMove = new Move(selected.getPiece(), selected, selected, Condition.MOVE );
					Move nextMove = new Move(selected.getPiece(), selected, next, Condition.MOVE );
					Move lastMove = new Move(selected.getPiece(), selected, last, Condition.MOVE );
					
					if(isMoveVeryBad(startMove) || isMoveVeryBad(nextMove) || isMoveVeryBad(lastMove))
						return Condition.UNKNOWN; 
					
						
					//And there are all empty spaces between the king and the rook
					if(othersEmpty)
						return type;
				}
			}
		}
		
		return type;
	}
	
	public boolean isMoveVeryBad(Move move, Cell weakPiece) {
		PieceColor opponentColor = null;
		
		if(weakPiece.getPiece().getColor() == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		List<Cell> potentialAttackers = getPiecesByColor(opponentColor); 
		
		for(Cell attacker: potentialAttackers) {
			List<Cell> captureMoves = attacker.getPiece().getAllPotentialCaptures(attacker);
			for(Cell attackerMove : captureMoves) {
				if(attackerMove.equals(weakPiece)) {
					if(attacker.getPiece().isItEatable(attacker, weakPiece, move.getOriginal())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isMoveVeryBad(Move move) {
		PieceColor opponentColor = null;
		Cell weakPiece = move.getDistination();
		
		if(move.getPiece().getColor() == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		List<Cell> potentialAttackers = getPiecesByColor(opponentColor); 
		
		for(Cell attacker: potentialAttackers) {
			List<Cell> captureMoves = attacker.getPiece().getAllPotentialCaptures(attacker);
			for(Cell attackerMove : captureMoves) {
				if(attackerMove.equals(weakPiece)) {
					if(attacker.getPiece().isItEatable(attacker, weakPiece, move.getOriginal())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if a given cell is under opponent's attack (by one of opponent's pieces)
	 * @param move
	 * @param opponentColor
	 * @return
	 */
	private boolean checkIfCellIsSafe(Cell move, PieceColor opponentColor) {
		List<Cell> allPieces = getPiecesByColor(opponentColor);
		
		for(Cell p : allPieces) {
			List<Cell> captureMoves = p.getPiece().getAllPotentialCaptures(p);//TODO - needs to bring all potential captures, not only real ones
			if(captureMoves.size() == 0)
				continue;
			
			for(Cell c : captureMoves) {
				if(c.getCol() == move.getCol() && c.getRow() == move.getRow()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets path between two pieces
	 * @param piece
	 * @param attacker
	 * @return
	 */
	private List<Cell> getPath(Cell piece, Cell attacker) {
		List<Cell> path = new ArrayList<>();
		int horizontal = 0;
		int vertical = 0;
		int x = 0, y = 0;
		int col = attacker.getCol(), row = attacker.getRow();
		horizontal = attacker.getCol() - piece.getCol();
		vertical = attacker.getRow() - piece.getRow();
		
		if(horizontal < 0)
			x = 1; //move right ++
		else if(horizontal > 0)
			x = -1; //move left --
		
		if(vertical < 0)
			y = 1; //move down ++
		else if(vertical > 0)
			y = -1; //move up --
		
		while ((col >= 0 && col <= 7 && row >=0 && row <= 7) && 
				!(col == piece.getCol() && row == piece.getRow()) ){
			//attacker.move(x, y);
			col += x; 
			row += y;
			path.add(new Cell(col, row));
		}
		
		return path;
	}
	
	private boolean canMoveDefendMe(Cell piece, Cell attacker, Move move) {
		//List<Cell> captureMoves = attacker.getPiece().getAllCaptures(attacker);
		//take a list of all moves from attacker to victim
		List<Cell> path = getPath(piece, attacker);
		
		for(Cell p : path) {
			if(p.equals(move.getDistination())) {
				System.out.println("defending move: " + move.getOriginal() + " " + move.getDistination() + " " + move.getPiece());
				return true;
			}
				
		}
		return false;
	}
}
