package brain;

import java.util.List;
import java.util.Map;

import game.entities.Cell;
import game.entities.Move;
import game.entities.MovePower;
import game.entities.PiecePower;
import game.entities.types.Condition;
import game.entities.types.PieceColor;

public interface BrainInterface {
	/**
	 * Creative thinking brain part
	 * @param goodMoves
	 * @return
	 */
	public Move thinkForTheBestMove(List<Move> goodMoves);

	/**
	 * Instinctive thinking brain part
	 * @param safeMoves
	 * @return
	 */
	public Move saveMyKing(List<Move> safeMoves);

	public Move rankForTheBestMove(PieceColor pc);

	public Map<Cell, List<MovePower>> insightForAssistant(PieceColor myColor);

	public Condition checkForExceptions();

}
