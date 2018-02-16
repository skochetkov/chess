package gui.entities.pieces;

import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Pattern;

public class Dummy  extends Piece{
	public Dummy(ChessBoardController controller, PieceColor color, String id) {
		super(controller);
		init(color, id, null);
	}
	
	@Override
	public List<Cell> getAllMoves(Cell current, boolean includeImpossibl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossibl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PieceType getType() {
		return PieceType.DUMMY;
	}

	@Override
	public List<Pattern> getPatterns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cell> getAllPotentialCaptures(Cell p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Piece clonePiece(ChessBoardController controller) {
		return new Dummy(controller, color, pieceID);
	}

	@Override
	public void initPatterns() {
		// TODO Auto-generated method stub
		
	}

}
