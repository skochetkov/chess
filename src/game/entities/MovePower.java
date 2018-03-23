package game.entities;

public class MovePower implements Comparable<MovePower>{
	public Move move;
	
	public int myCurrentAttackingPower;
	public int myCurrentDefendingPower;
	public int myNewAttackingPower;
	public int myNewDefendingPower;
	
	public int opponentCurrentAttackingPower;
	public int opponentCurrentDefendingPower;
	public int opponentNewAttackingPower;
	public int opponentNewDefendingPower;
	
	public int myDeltaAttack;
	public int myDeltaDefense;
	public int myDeltaAttackByTactics;
	public int myDeltaDefenseByTactics;
	public int opponentDeltaAttack;
	public int opponentDeltaDefense;
	
	public int myRating;
	public int opponentRating;

	public int moveRating;	
	
	@Override
	public String toString() {
		return move + " myDeltaAttack:" + myDeltaAttack +
		" myDeltaDefense:" + myDeltaDefense + 
		" myDeltaAttackByTactics:" + myDeltaAttackByTactics +
		" myDeltaDefenseByTactics:" + myDeltaDefenseByTactics + 
		" opponentDeltaAttack:" + opponentDeltaAttack +
		" opponentDeltaDefense:" + opponentDeltaDefense +
		"[opponentRating:" + opponentRating + " myRating:" + myRating + "]";
	}

	@Override
    public int compareTo(MovePower o) {
		return Integer.compare(this.myRating, o.myRating);
    }
}
