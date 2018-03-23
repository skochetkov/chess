package game.entities;

/**
 * Piece power consists of:
 * - Piece cost
 * - Attacking power (self describing)
 * - Defending power (ability to defend other its pieces)
 * - Position (how stable position it is)
 */
public class PiecePower implements Comparable<Object>{
	private int pieceCost;
	private int attackingPower;
	private int defendingPower;
	private int positionStability;
	private Cell cell;
	
	public PiecePower() {
		
	}
	
	public PiecePower(int pieceCost, int attackingPower, int defendingPower, int positionStability) {
		this.pieceCost = pieceCost;
		this.attackingPower = attackingPower;
		this.defendingPower = defendingPower;
		this.positionStability = positionStability;
	}
	
	public int getPieceCost() {
		return pieceCost;
	}
	
	public void setPieceCost(int pieceCost) {
		this.pieceCost = pieceCost;
	}
	
	public int getAttackingPower() {
		return attackingPower;
	}
	
	public void setAttackingPower(int attackingPower) {
		this.attackingPower = attackingPower;
	}
	
	public int getDefendingPower() {
		return defendingPower;
	}
	
	public void setDefendingPower(int defendingPower) {
		this.defendingPower = defendingPower;
	}
	
	public int getPositionStability() {
		return positionStability;
	}
	
	public void setPositionStability(int positionStability) {
		this.positionStability = positionStability;
	}
	
	public void setCell(Cell key) {
		cell = key;
	}
	
	public Cell getCell() {
		return cell;
	}

	@Override
	public String toString() {
		return "pieceCost:" + pieceCost +
		" attackingPower:" + attackingPower + 
		" defendingPower:" + defendingPower +
		" positionStability:" + positionStability;
	}

	@Override
	public int compareTo(Object o) {
		return (this.positionStability > ((PiecePower)o).positionStability ) ? -1: (this.positionStability < ((PiecePower)o).positionStability) ? 1:0 ;
	}
}
