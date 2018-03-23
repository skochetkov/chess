package brain;

import java.util.List;
import java.util.Map;

import api.ChessGameConnect;
import game.entities.Cell;
import game.entities.Move;
import game.entities.MovePower;
import game.entities.PiecePower;
import model.User;

/**
 * Assistant is Singleton (only one assistant can exists)
 * and should be accessible from anywhere and doesn't belong to anything
 *
 */
public class Assistant {
	private BrainInterface assistantBrain;
	private ChessGameConnect api;
	private User user;
	private static Assistant assistant;
	private static boolean isOn;
	private static boolean enableInfo;
	
	private Assistant() {}
	
	public static Assistant getAssistant() {
		if(assistant == null)
			assistant = new Assistant();
		
		return assistant;
	}
	
	public void initAssistant(ChessGameConnect api, User user) {
		this.api = api;
		this.user = user;
		assistantBrain = new Brain1200(api, user.getColor());
	}

	public Move chooseTheBestMove(List<Move> goodMoves) {
		return assistantBrain.thinkForTheBestMove(goodMoves);
	}

	public static void setOn(boolean b) {
		isOn = b;
	}
	
	public static void enableInfo(boolean b) {
		enableInfo = b;
	}
	
	public static boolean isOn() {
		return isOn;
	}
	
	public static boolean isInfoEnabled() {
		return enableInfo;
	}
	
	public void displayPiecesAssessment() {
		if(!Assistant.isOn()) return;
		//assistantBrain.rankBestMoves(user.getColor());
		
		Map<Cell, List<MovePower>> positionMap = assistantBrain.insightForAssistant(user.getColor());
		
		
		for (Map.Entry<Cell, List<MovePower>> entry : positionMap.entrySet())
		{
			int attack = 0;
			int defend = 0;
			
			List<MovePower> list = entry.getValue();
			Cell piece = entry.getKey();
			
			//attack = pp.getAttackingPower();
			//defend = pp.getDefendingPower();
			
			//piece.setHighlightedScore(attack, defend);
			api.getRealCell(piece).setHighlightedScore(attack, defend);
			
		}
		
		//TODO - turn off as it is just prove of concept
		assistantBrain.rankForTheBestMove(user.getColor());
		//api.getBoard()
	}
	
	public void clearPiecesAssessment() {
		if(!Assistant.isOn()) return;
		
		api.getBoard().clearOptionSelectedCells();
	}
	
	public void clearOptionSelectedCells() {
		if(!Assistant.isOn()) return;
		
		api.getBoard().clearOptionSelectedCells();
	}
	
	public void whatOptionsUserHas(Cell param) {
		if(!Assistant.isOn()) return;
		
		List<Move> moves = api.getBoard().getAllPossibleMoves(param);
		
		if(moves.size() == 0) {
			System.out.println("No Options!");
			return;
		}
		
		if(Assistant.isOn()) {
			//List<Move> goodMoves = api.getGoodMoves(user.getColor());
			//Move move = Assistant.getAssistant().chooseTheBestMove(goodMoves);
			
			Assistant.getAssistant().displayPiecesAssessment();
		}
		
		System.out.println("Options for " + moves.get(0).getPiece().getColor() + " " + moves.get(0).getPiece().getType() + ":");
		for(Move move : moves) {
			if(Assistant.isOn()) 
				api.getRealCell(move.getDistination()).setOptionSelected(true);
			System.out.println(move.getCondition() + " " + move.getDistination().getNotation());
		}
		
		
	}
}
