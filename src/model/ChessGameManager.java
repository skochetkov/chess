package model;

import java.util.List;

import api.ChessGameConnect;
import brain.Assistant;
import brain.Brain1200;
import brain.BrainInterface;
import brain.Zhuli;
import game.ChessBoardController;
import game.entities.Cell;
import game.entities.Move;
import game.entities.Parameter;
import game.entities.pieces.Piece;
import game.entities.types.CellType;
import game.entities.types.Condition;
import game.entities.types.GameStatus;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import game.entities.types.Requests;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.UIUtil;

public class ChessGameManager {
	private ChessBoardController board;
	private ChessGameConnect api;
	private User user;
	private Zhuli zhuli;
	private GameStatus status = GameStatus.NONE;
	
	//Configuration fields
	private boolean withAssistant = false;
	private static boolean debug = false;
	
	public ChessGameManager() {
		board = new ChessBoardController();
		board.initBoard(this);
		//board.initPieces();
		board.initTests();
        
		api = new ChessGameConnect(this);
        //regular scenario playing user with computer
        initDefaultGame();
	}
	
	private void initDefaultGame() {
		//status = GameStatus.WHITE_MOVE;
		
			
		//initialize user
		user = new User(api, PieceColor.WHITE);
        user.initUser();
        
        if(withAssistant) {
        		Assistant.setOn(true);
        		Assistant.enableInfo(true);
			Assistant.getAssistant().initAssistant(api, user);
		}
        
        //initialize Zhuli
        zhuli = new Zhuli(api, PieceColor.BLACK);
        
		//now user should do first move
		
	}
	
	public ChessBoardController getController() {
		return board;
	}

	public boolean isClickLegal() {
		return true;
	}
	
	public Cell[][] getBoardCells() {
		return board.getBoardCells();
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
			if(Assistant.isOn()) {
				Assistant.getAssistant().whatOptionsUserHas((Cell)param);
			}
			whatUserCanDo((Cell)param);
			break;
		case I_WAS_PROPOSED_BY_USER:
			whatUserCanDo((Cell)param);
			break;
		default: 
			
		}
	}

	public void clearSelectedCellsExceptOne(Cell param) {
		board.clearSelectedCells(param);
	}
	
	public void clearSelectedCells() {
		board.clearSelectedCells();
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
				Move move = board.isMoveLegal(selected, param);
				
				switch (move.getCondition()) {
					case MOVE:
						doMove(new Move(selected.getPiece(), selected, param, move.getCondition()));
						break;
					case CASTLING_ON_LEFT: {
						doMove(new Move(selected.getPiece(), selected, param, Condition.MOVE));
						//get rook and move it too
						Cell rook = new Cell(0, selected.getRow());
						rook = getRealCell(rook);
						Cell newPlace = new Cell(3, selected.getRow());
						doMove(new Move(rook.getPiece(), rook, newPlace, Condition.MOVE));
						break;
					}
					case CASTLING_ON_RIGHT: {
						doMove(new Move(selected.getPiece(), selected, param, Condition.MOVE));
						//get rook and move it too
						Cell rook = new Cell(7, selected.getRow());
						rook = getRealCell(rook);
						Cell newPlace = new Cell(5, selected.getRow());
						doMove(new Move(rook.getPiece(), rook, newPlace, Condition.MOVE));
						break;
					}
					case EN_PASSANT_LEFT: {
						doEnPassantMove(move);
						break;
					}
					case EN_PASSANT_RIGHT: {
						doEnPassantMove(move);
						break;
					}
					case POSSIBLE_PROMOTION: {
						UIUtil.showPromotionBox(this, selected, param) ;
						return;
					}
					case UNKNOWN: {
						return;
					}
					default:
						break;
				}
				
				//TODO repaint here somehow
				//////
				status = GameStatus.BLACK_MOVE;
			}
			//user wants to un-select his piece
			else {
				param = getRealCell(param);
				param.setSelected(false);
				Assistant.getAssistant().clearOptionSelectedCells();
				Assistant.getAssistant().clearPiecesAssessment();
				return;
			}
		}
		//If Promotion
		else if(status == GameStatus.WHITE_MOVE && param.getPiece() != null && param.getType() == CellType.PROMOTED) {
			status = GameStatus.BLACK_MOVE;
			param.setType(CellType.NONE);
			param = getRealCell(param);
			param.setSelected(false);
			Assistant.getAssistant().clearOptionSelectedCells();
			Assistant.getAssistant().clearPiecesAssessment();
			
		}
		//if user is supposed to select figure with which to move (but we are not allowed to touch opponents pieces)
		//in other words: it's our move, we haven't selected anything and we are not trying to select opponents piece
		else if(status == GameStatus.WHITE_MOVE && !isCurrentPlayerHasSelectedCell && 
				(param.getPiece() != null  && param.getPiece().getColor() == PieceColor.WHITE)) {
			param.setSelected(true);
		}
		else {
			log("Unknown situation");
			return;
		}
		
		//this is supposed to be Zhuli role
		if(status == GameStatus.BLACK_MOVE) {
			//log("Lets pretend that Zhuli made a move. Now your turn!");
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
		List<Move>  defendingMoves = board.checkIfItIsMate(status);
	
		return defendingMoves;
	}
	
	/**
	 * Bring the list of all defending pieces and their defending moves except piece itself
	 * @param piece
	 * @param color
	 * @return
	 */
	public boolean checkIfICanBeDefended(Cell piece, PieceColor color) {
		
		return (getWhoCanDefendMe(piece, color).size() > 0)? true : false;
	}
	
	public void doMove(Move move) {
		board.doMove(move);
		if(Assistant.isOn()) {
			Assistant.getAssistant().clearOptionSelectedCells();
		}
	}
	
	public void doEnPassantMove(Move move) {
		//take its figure
		Cell selected = move.getOriginal();
		Cell newLocation = move.getDistination();
		PieceColor opponentColor;
		
		Piece piece = getRealCell(selected).getPiece();
		newLocation = getRealCell(newLocation);
		log("###EN PASSANT! MOVING " + piece.getColor() + " " + piece.getType() + ": from " + selected.getNotation() + " to " + newLocation.getNotation());
		

		
		// Reset opponents pawn
		PieceColor color = selected.getPiece().getColor();
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		if(selected.getPiece().getType() != PieceType.PAWN) 
			return;
		
		List<Move> recordedMoves = board.getRecordedMovesByColor(opponentColor);
		Move lastOppenentMove = recordedMoves.get(recordedMoves.size() - 1);
		
		//if last opponents move is not pawn and it is not its first move ignore it
		if(lastOppenentMove.getPiece().getType() != PieceType.PAWN)
			return;
		
		Cell pieceToReset = getRealCell(lastOppenentMove.getDistination());
		
		pieceToReset.resetPiece();
		//if it has opponents figure, eat it
		//if(newLocation.getPiece() != null) {
		//	newLocation.setPiece(piece);
		//}
		
		//and move piece to a new location
		newLocation.setPiece(piece);
		
		//unselect previous cell
		selected.resetPiece();
		selected.setSelected(false);
		if(Assistant.isOn()) {
			Assistant.getAssistant().clearOptionSelectedCells();
		}
		
		//record en passant move
		board.recordMove(move);
		
	}
	
	public void doTempMove(Cell selected, Cell newLocation) {
		//take its figure
		Piece piece = getRealCell(selected).getPiece();
		newLocation = getRealCell(newLocation);
		log("###TEMP MOVING " + piece.getColor() + " " + piece.getType() + ": from " + selected.getNotation() + " to " + newLocation.getNotation());
		
		newLocation.setPiece(piece);
		
		selected.resetPiece();
	}

	public PieceColor getWhosMoveNow() {
		if(status == GameStatus.BLACK_MOVE)
			return PieceColor.BLACK;
		else
			return PieceColor.WHITE;
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
	
	/**
	 * Use cases:
	 * 	- a piece can be attacked by more than one opponents figure - need to check if all attackers can be neutralized 
	 *  - any defender can defend by more than one moves, so we return list of moves, and not only defenders
	 * @param piece
	 * @param color
	 * @return
	 */
	public List<Move> getWhoCanDefendMe(Cell piece, PieceColor color) {
		return board.getWhoCanDefendMe(piece, color);
	}

	public boolean checkIfItIsCheck(GameStatus status) {
		return board.checkIfItIsCheck(status);
	}
	
	public List<Cell> getPiecesByTypeAndColor(PieceType piece, PieceColor color) {
		return board.getPiecesByTypeAndColor(piece, color);
	}
	
	public List<Cell> getWhoEndangersMe(Cell me) {
		return board.getWhoEndangersMe(me);
	}
	

	public List<Move> getWhoCanEatThisPiece(Cell cell) {
		return board.getWhoCanEatThisPiece(cell);
	}
	public List<Cell> getPiecesByColor(PieceColor color) {
		return board.getPiecesByColor(color);
	}
	
	/**
	 * Bring the list of all defending pieces and their defending moves except piece itself
	 * @param me
	 * @return
	 */
	public List<Cell> getWhoCanDefendMe(Cell me) {
		return board.getWhoCanDefendMe(me);
	}

	public Move isEnPassantAllowed(Cell selected) {
		return board.isEnPassantAllowed(selected);
	}

	public Condition isCastlingAllowed(Cell selected, Cell newLocation) {
		return board.isCastlingAllowed(selected, newLocation);
	}

	public Cell getRealCell(Cell distination) {
		return board.getRealCell(distination);
	}
	
	public List<Move> getAllPossibleMoves(Cell selected) {
		return board.getAllPossibleMoves(selected);
	}
	
	public List<Move> getAllPossibleSafeMoves(Cell selected) {
		return board.getAllPossibleSafeMoves(selected);
	}

	public List<Cell> getAllSafeCaptures(Cell piece) {
		return board.getAllSafeCaptures(piece);
	}
	
	public List<Cell> getAllSafeMoves(Cell piece) {
		return board.getAllSafeMoves(piece);
	}

	public boolean changeStatus(GameStatus gameStatus) {
		status = gameStatus;
		return true;
	}

	public boolean isMoveVeryBad(Move move, Cell weakPiece) {
		return board.isMoveVeryBad(move, weakPiece);
	}
	
	public boolean isMoveVeryBad(Move move) {
		return board.isMoveVeryBad(move);
	}

	public void setPieceToRealCell(Cell original, Piece piece) {
		board.setPieceToRealCell(original, piece);
	}

	public List<Move> getGoodMoves(PieceColor color) {
		return board.getGoodMoves(color);
	}

	public Cell getSelectedCell() {
		return board.getSelectedCell();
	}

	public void replacePiece(Cell newLocation, Cell cell, Cell oldPiece) {
		board.replacePiece(newLocation, cell, oldPiece);
	}

	public ChessBoardController cloneController() {
		return board.clone();
	}
	
	public static boolean isDebugEnabled() {
		return debug;
	}
	
	private void log(String logMessage) {
		if(ChessGameManager.isDebugEnabled())
			System.out.println(logMessage);
	}

}
