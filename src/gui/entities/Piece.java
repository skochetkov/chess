package gui.entities;

import java.util.ArrayList;
import java.util.List;

import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Pattern;
import javafx.scene.layout.HBox;

public abstract class Piece {
	
	protected List<Pattern> patterns = new ArrayList<>();
	protected HBox ui;
	protected String pieceID = "0";
	protected PieceColor color;
	protected boolean isMoved = false;
	protected int pieceValue = 1;
	
	public Piece(PieceColor color, String id) {
		this.color = color;
		setId(id);
		init();
	}
	public abstract List<Cell> getAllMoves(Cell current, boolean includeImpossible);
	
	public abstract List<Cell> getAllCaptures(Cell current, boolean includeImpossible);
	
	public abstract PieceType getType();
	
	public abstract void init();

	public abstract List<Pattern> getPatterns();

	public abstract List<Cell> getAllPotentialCaptures(Cell p);

	public abstract boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell);
	
	public HBox getUI() {
		return ui;
	}
	
	public PieceColor getColor() {
		return color;
	}
	
	public void setId(String i) {
		pieceID = i;
	}

	public String getId() {
		return pieceID;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved() {
		isMoved = true;
	}
	
	public void resetMoved() {
		isMoved = false;
	}
	
	public int getPieceValue() {
		return pieceValue;
	}
}
