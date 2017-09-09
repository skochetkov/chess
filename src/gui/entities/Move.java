package gui.entities;

import gui.entities.types.Condition;
import gui.entities.types.PieceColor;

public class Move {
	
	private Cell original;
	private Cell distination;
	private Piece piece;
	private PieceColor color;
	private Condition condition;
	
	public Move(Condition condition) {
		this.setCondition(condition);
	}
	
	public Move(Piece piece, Cell original, Cell distination, Condition condition) {
		this.setPiece(piece);
		this.setOriginal(original);
		this.setDistination(distination);
		this.setCondition(condition);
		this.setColor(piece.getColor());
	}

	public Cell getOriginal() {
		return original;
	}

	public void setOriginal(Cell original) {
		this.original = original;
	}

	public Cell getDistination() {
		return distination;
	}

	public void setDistination(Cell distination) {
		this.distination = distination;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public PieceColor getColor() {
		return color;
	}

	public void setColor(PieceColor color) {
		this.color = color;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	
}