package game.entities;

import game.entities.pieces.Piece;
import game.entities.types.Condition;
import game.entities.types.PieceColor;

public class Move implements Comparable<Move>{
	
	private Cell original;
	private Cell distination;
	private Piece piece;
	private PieceColor color;
	private Condition condition;
	private Move followingMove;
	//default weight
	private int weight = 0;
	
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
	
	public Move getFollowingMove() {
		return followingMove;
	}

	public void setFollowingMove(Move followingMove) {
		this.followingMove = followingMove;
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
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
    public int compareTo(Move another) {
        if (this.weight < another.weight){
            return 1;
        }else{
            return -1;
        }
    }
	
	@Override
	public String toString() {
		return condition + ": " + piece + " from " + original.getNotation() + " to " + distination.getNotation(); 
	}
	
	@Override
	public boolean equals(Object obj) {
		return (getPiece().equals(((Move)obj).getPiece()) && 
				getDistination().equals(((Move)obj).getDistination()) &&
				getOriginal().equals(((Move)obj).getOriginal()));
	}
}
