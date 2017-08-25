package model;

import java.util.ArrayList;
import java.util.List;

import api.ChessGameConnect;
import engine.Zhuli;
import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.Move;
import gui.entities.Piece;
import gui.entities.Parameter;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Condition;
import gui.entities.types.GameStatus;
import gui.entities.types.Requests;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ChessGameManager {
	private ChessBoardController board;
	private ChessGameConnect api;
	private User user;
	private Zhuli zhuli;
	private GameStatus status = GameStatus.NONE;
	
	public ChessGameManager(Stage primaryStage) {
		board = new ChessBoardController(primaryStage, this);
        
		api = new ChessGameConnect(this);
        //regular scenario playing user with computer
        initDefaultGame();
	}
	
	private void initDefaultGame() {
		//status = GameStatus.WHITE_MOVE;
		
		//initialize user
		user = new User(api, PieceColor.WHITE);
        user.initUser();
        
        //initialize Zhuli
        zhuli = new Zhuli(api, PieceColor.BLACK);
        
		//now user should do first move
		
	}

	public boolean isClickLegal() {
		return true;
	}

	public void whatToDoNext(Requests req, Parameter param) {
		//if the game is over, it is over
		if(status == GameStatus.GAME_OVER) {
			return;
		}
		
		switch(req) {
		case CLEAR_ALL_SELECTED_CELLS:
			clearSelectedCells();
			break;
		case CLEAR_ALL_SELECTED_CELLS_EXCEPT_ONE:
			clearSelectedCellsExceptOne((Cell)param);
			break;
		case I_WAS_SELECTED_BY_USER:
			whatOptionsUserHas((Cell)param);
			whatUserCanDo((Cell)param);
			break;
		case I_WAS_PROPOSED_BY_USER:
			whatUserCanDo((Cell)param);
			break;
		default: 
			
		}
	}

	private void clearSelectedCellsExceptOne(Cell param) {
		board.clearSelectedCells(param);
	}
	
	private void clearSelectedCells() {
		board.clearSelectedCells();
	}
	
	private void whatOptionsUserHas(Cell param) {
		List<Move> moves = getAllPossibleMoves(param);
		
		if(moves.size() == 0) {
			System.out.println("No Options!");
			return;
		}
		
		System.out.println("Options for " + moves.get(0).getPiece().getColor() + " " + moves.get(0).getPiece().getType() + ":");
		for(Move move : moves) {
			System.out.println(move.getCondition() + " " + move.getDistination().getNotation());
		}
	}
	
	private void whatUserCanDo(Cell param) {
		//if the game is over, it is over
		if(status == GameStatus.GAME_OVER) {
			return;
		}
		//check first if this is the first ever move
		if(status == GameStatus.NONE) {
			//this was just dummy click, do not select anything
			if(param.getPiece() == null) {
				//param.setSelected(false);
				return;
			}
			
			if(user.getColor() == PieceColor.WHITE) 
				status = GameStatus.WHITE_MOVE;
			else
				status = GameStatus.BLACK_MOVE;
			
			param.setSelected(true);
			
			return;
		}
		
		boolean isCurrentPlayerHasSelectedCell = isCurrentPlayerHasSelectedCell();
		//user wants to move selected figure
		if(status == GameStatus.WHITE_MOVE && isCurrentPlayerHasSelectedCell) {
			//if selected cell is empty or with opponents piece, then do the move
			if(param.isEmpty() || (param.getPiece() != null && param.getPiece().getColor() == PieceColor.BLACK))
			{
				//
				//take selected cell
				Cell selected = board.getSelectedCell();
				param = getRealCell(param);
				if(!isMoveLegal(selected, param))
					return;
				doTheMove(selected, param);
				//TODO repaint here somehow
				//////
				status = GameStatus.BLACK_MOVE;
			}
			//user wants to un-select his piece
			else {
				param = getRealCell(param);
				param.setSelected(false);
				return;
			}
		}
		//if user is supposed to select figure with which to move (but we are not allowed to touch opponents pieces)
		//in other words: it's our move, we haven't selected anything and we are not trying to select opponents piece
		else if(status == GameStatus.WHITE_MOVE && !isCurrentPlayerHasSelectedCell && 
				(param.getPiece() != null  && param.getPiece().getColor() == PieceColor.WHITE)) {
			param.setSelected(true);
		}
		else {
			System.out.println("Unknown situation");
			return;
		}
		
		//this is supposed to be Zhuli role
		if(status == GameStatus.BLACK_MOVE) {
			//System.out.println("Lets pretend that Zhuli made a move. Now your turn!");
			zhuli.myTurn();
			status = GameStatus.WHITE_MOVE;
			
			//We check if Zhuli made actual mate (lost game)
			if(checkIfItIsCheck(status)) {
				List<Move> moves = checkIfItIsMate(status);
				if(moves.size() == 0) {
					status = GameStatus.GAME_OVER;
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("");
					alert.setHeaderText("Mate Alert");
					String s = "This is mate!";
					alert.setContentText(s);
					alert.show();
					return;
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("");
					alert.setHeaderText("Check Alert");
					String s = "Your King is uder attack!";
					alert.setContentText(s);
					alert.show();
				}
				
			}
		}
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
		// if king can be defended by his pieces
		List<Move>  defendingMoves = checkIfICanBeDefended(king, color);
		if(defendingMoves.size() > 0) {
			System.out.println("I can be saved!");
		}
		//get all king's moves and captures
		List<Cell> safeMoves = getAllSafeMoves(king);
		List<Cell> safeCaptures = getAllSafeCaptures(king);
		System.out.println("Checking if the king has safe moves or safe captures....");
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
	
	public List<Move> checkIfICanBeDefended(Cell piece, PieceColor color) {
		
		return getWhoCanDefendMe(piece, color);
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
	
	public List<Cell> getAttackingPiecesByColor(PieceColor color) {
		List<Cell> pieces = new ArrayList<>();
		
		List<Cell> allPieces = getPiecesByColor(color);
		
		for(Cell p : allPieces) {
			List<Cell> captureMoves = p.getPiece().getAllCaptures(p, false);
			if(captureMoves.size() == 0)
				continue;
			
			pieces.add(p);
		}
		
		return pieces;
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

	private boolean isCurrentPlayerHasSelectedCell() {
		if(status == GameStatus.WHITE_MOVE) {
			Cell selected = board.getSelectedCell();
			if(selected != null && selected.getPiece() != null && selected.getPiece().getColor() == PieceColor.WHITE) {
				return true;
			}
		}
		return false;
	}

	private boolean isMoveLegal(Cell selected, Cell newLocation) {
		
		//take its figure
		Piece piece = selected.getPiece();
		
		List<Cell> moves = piece.getAllMoves(selected, false);
		
		for(Cell move : moves) {
			//if new location is in the list of legal moves and it is not occupied by other pieces
			if(move.equals(newLocation) && newLocation.isEmpty())
				return true;
		}
		
		List<Cell> captureMoves = piece.getAllCaptures(selected, false);
		
		for(Cell move : captureMoves) {
			//if new location is in the list of legal moves and it is not occupied by other pieces
			if(move.equals(newLocation) && !newLocation.isEmpty() && selected.getPiece().getColor() != newLocation.getPiece().getColor())
				return true;
		}
		
		return false;
	}
	
	public void doTheMove(Cell selected, Cell newLocation) {
		//take its figure
		Piece piece = getRealCell(selected).getPiece();
		newLocation = getRealCell(newLocation);
		System.out.println("###MOVING " + piece.getColor() + " " + piece.getType() + ": from " + selected.getNotation() + " to " + newLocation.getNotation());
		
		//if it has opponents figure, eat it
		//if(newLocation.getPiece() != null) {
		//	newLocation.setPiece(piece);
		//}
		
		//and move piece to a new location
		newLocation.setPiece(piece);
		
		//unselect previous cell
		selected.resetPiece();
		selected.setSelected(false);
	}
	
	public void doTempMove(Cell selected, Cell newLocation) {
		//take its figure
		Piece piece = getRealCell(selected).getPiece();
		newLocation = getRealCell(newLocation);
		System.out.println("###TEMP MOVING " + piece.getColor() + " " + piece.getType() + ": from " + selected.getNotation() + " to " + newLocation.getNotation());
		
		newLocation.setPiece(piece);
		
		selected.resetPiece();
	}

	public PieceColor getWhosMoveNow() {
		if(status == GameStatus.BLACK_MOVE)
			return PieceColor.BLACK;
		else
			return PieceColor.WHITE;
	}

	public List<Cell> getPiecesByColor(PieceColor color) {
		List<Cell> pieces = new ArrayList<>();
		Cell[][] cells = board.getBoardCells();
		
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

	public Cell getRealCell(Cell distination) {
		return board.getRealCell(distination);
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
		List<Cell> attackers = getAttackingPiecesByColor(color);
		
		boolean isSafeCapture = true;
		
		for(Cell myCapture : allCaptures) {
			for(Cell attackerCapture: attackers) {
				//if my capture is under attack
				if(myCapture.equals(attackerCapture))
					isSafeCapture = false;
			}
			
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

	public boolean changeStatus(GameStatus gameStatus) {
		status = gameStatus;
		return true;
	}

}
