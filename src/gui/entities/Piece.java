package gui.entities;

import java.util.List;

import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Pattern;
import javafx.scene.layout.HBox;

public interface Piece {
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible);
	
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible);
	
	public PieceType getType();
	
	public PieceColor getColor();
	
	public HBox getUI();
	
	void init();

	public List<Pattern> getPatterns();

	public List<Cell> getAllPotentialCaptures(Cell p);

	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell);
	
}
