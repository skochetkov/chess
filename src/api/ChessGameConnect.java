package api;

import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.Move;
import gui.entities.pieces.Piece;
import gui.entities.types.Condition;
import gui.entities.types.GameStatus;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import model.ChessGameManager;

public class ChessGameConnect {
	ChessGameManager manager;
	
	public ChessGameConnect(ChessGameManager manager) {
		this.manager = manager;
	}
	
	public List<Piece> getAllPieces() {
		return null;
	}
	
	public List<Cell> getPiecesByColor(PieceColor color) {
		return manager.getPiecesByColor(color);
	}
	
	public PieceColor getWhosMoveNow() {
		return manager.getWhosMoveNow();
	}
	
	public List<Move> getAllPossibleMoves(Cell selected) {
		return manager.getAllPossibleMoves(selected);
	}
	
	public boolean doMove(Move move) {
		manager.doMove(move);
		return true;
	}

	public Cell getRealCell(Cell distination) {
		return manager.getRealCell(distination);
	}

	public boolean checkIfItIsCheck(GameStatus status) {
		return manager.checkIfItIsCheck( status);
	}

	public List<Move> checkIfItIsMate(GameStatus status) {
		return manager.checkIfItIsMate(status);
	}

	public List<Cell> getPiecesByTypeAndColor(PieceType piece, PieceColor color) {
		return manager.getPiecesByTypeAndColor(piece, color);
	}
	
	public List<Cell> getWhoEndangersMe(Cell me) {
		return manager.getWhoEndangersMe(me);
	}
	
	public List<Cell> getWhoCanDefendMe(Cell me) {
		return manager.getWhoCanDefendMe(me);
	}

	public List<Cell> getAllSafeCaptures(Cell piece) {
		return manager.getAllSafeCaptures(piece);
	}

	public List<Cell> getAllSafeMoves(Cell piece) {
		return manager.getAllSafeMoves(piece);
	}

	public boolean changeStatus(GameStatus gameStatus) {
		return manager.changeStatus(gameStatus);
	}

	public List<Move> getAllPossibleSafeMoves(Cell piece) {
		return manager.getAllPossibleSafeMoves(piece);
	}

	/**
	 * Checks if the move of the piece leads to loosing provided piece
	 * @param move
	 * @param weakPiece
	 * @return
	 */
	public boolean isMoveVeryBad(Move move, Cell weakPiece) {
		return manager.isMoveVeryBad(move, weakPiece);
	}
	
	/**
	 * Checks if the move of the piece leads to loosing one
	 * @param move
	 * @return
	 */
	public boolean isMoveVeryBad(Move move) {
		return manager.isMoveVeryBad(move);
	}
	
	public Condition isCastlingAllowed(Cell selected, Cell newLocation) {
		return manager.isCastlingAllowed(selected, newLocation);
	}
	
	public Move isEnPassant(Cell selected) {
		return manager.isEnPassantAllowed(selected);
	}

	public void setPieceToRealCell(Cell original, Piece piece) {
		manager.setPieceToRealCell(original, piece);
	}

	public List<Move> getGoodMoves(PieceColor color) {
		return manager.getGoodMoves(color);
	}
	
	public void doEnPassantMove(Move move) {
		manager.doEnPassantMove(move);
	}

	//Not the best solution to give reference to the controller (cloned)
	public ChessBoardController cloneBoard() {
		return manager.cloneController();
	}

	public ChessBoardController getBoard() {
		return manager.getController();
	}
}
