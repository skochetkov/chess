package gui.entities.pieces;

import java.util.List;

import gui.entities.Cell;
import gui.entities.Piece;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Pattern;
import javafx.scene.layout.HBox;

public class Dummy  implements Piece{
	private PieceColor color;
	
	public Dummy(PieceColor color) {
		this.color = color;
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
	public PieceColor getColor() {
		return color;
	}

	@Override
	public HBox getUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
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
}
