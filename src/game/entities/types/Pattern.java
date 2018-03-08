package game.entities.types;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
	private List<MoveType> moves = new ArrayList<>();
	private Condition condition = Condition.MOVE;
	boolean recursive = false;
	
	public Pattern(Condition condition) {
		this.condition = condition;
	}
	
	public Pattern(Condition condition, boolean recursive) {
		this.condition = condition;
		this.recursive = recursive;
	}

	public void addMove(MoveType move) {
		moves.add(move);
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public List<MoveType> getMoves() {
		return moves;
	}
	
	public boolean isRecusive() {
		return recursive;
	}
}
