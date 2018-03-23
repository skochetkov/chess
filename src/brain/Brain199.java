package brain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import api.ChessGameConnect;
import game.entities.Cell;
import game.entities.Move;
import game.entities.MovePower;
import game.entities.PiecePower;
import game.entities.types.Condition;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import util.Config;

public class Brain199 implements BrainInterface{
	
	private final int GOOD_MOVE_THRESHOLD = 50;
	private final int UPPER_LIMIT = 100;
	private Random rand; 
	private ChessGameConnect api;
	private boolean DEBUG = Config.DEBUG; 
	
	public Brain199(ChessGameConnect api) {
		this.api = api;
		rand = new Random(); 
	}
	
	/**
	 * CAUTION! WORK WITH BRAIN! 
	 * This is the first non random brain where we assign some kind of weight to moves
	 * The idea is to assign some weight according to the type of condition and then to assign sub-weight according to specifics of the condition
	 * Example: Generally, the attack is relatively strong move and we assign some weight for all attacks but each attack is different, some of them
	 * are stupid and here we assign extra to the general value based on how stupid or smart attack is
	 * @param goodMoves
	 * @return the best move
	 */
	@Override
	public Move thinkForTheBestMove(List<Move> goodMoves) {
		// The first dumbest idea - random move
		//int value = rand.nextInt(goodMoves.size()); 
		//Move move = goodMoves.get(value);
		
		// just for experiment we assign some kind of weight to moves
		//do some preparation before weighting
			
		// Assign weight //first not that smart ranking idea
		for(Move m: goodMoves) {
			Condition cond = m.getCondition();
							
			if(cond == Condition.POSSIBLE_PROMOTION)
				m.setWeight(80);
			else if(cond == Condition.CASTLING_ON_LEFT || cond == Condition.CASTLING_ON_RIGHT)
				m.setWeight(60);
			else if(cond == Condition.EN_PASSANT_LEFT || cond == Condition.EN_PASSANT_RIGHT )
				m.setWeight(55);
			else if(cond == Condition.ATTACK) {
				int attackWeight = 50 + getAttackWeight(m);
				m.setWeight(attackWeight);
			}
			else if(cond == Condition.MOVE && m.getPiece().getType() != PieceType.KING) {
				int moveWeight = 49 + getMoveWeight(m);
				m.setWeight(moveWeight);
			}
			else if(cond == Condition.MOVE)
				m.setWeight(30);
			else
				m.setWeight(10);
		}
		
		//Print out move rating
		System.out.println("Here is a rating of all moves");
		
		Collections.sort(goodMoves);
		
		for(Move m: goodMoves) {
			System.out.println(m.getWeight() + " " + m.getCondition() + " " + m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
		}
		
		//use threshold to distinguish really good moves from others
		List<Move> veryGoodMoves = new ArrayList<>();
		List<Move> notVeryGoodMoves = new ArrayList<>();
		
		for(Move m: goodMoves) {
			if(m.getWeight() >= GOOD_MOVE_THRESHOLD) {
				veryGoodMoves.add(m);
			}
			else {
				notVeryGoodMoves.add(m);
			}
		}
						
		if(veryGoodMoves.size() > 0) {
			goodMoves = veryGoodMoves;
		}
		
		Move move = goodMoves.get(0);
		int maxWeight = move.getWeight();
		
		// Take the one with highest weight
		for(Move m: goodMoves) {
			if(m.getWeight() > maxWeight) {
				maxWeight = m.getWeight();
			}
		}
		
		List<Move> highestWeightedMoves = new ArrayList<>();
		for(Move m: goodMoves) {
			if(m.getWeight() >= maxWeight){
				highestWeightedMoves.add(m);
			}
		}
		
		int randomValue = rand.nextInt(highestWeightedMoves.size()); 
		
		move = highestWeightedMoves.get(randomValue);
		
		return move;
	}
	
	//TODO improve, it is very dummy solution right now
		@Override
		public Move saveMyKing(List<Move> safeMoves) {
				System.out.println("It is not mate, it is good, now I need to choose a good move to save my king");
				int value = rand.nextInt(safeMoves.size()); 
				return safeMoves.get(value);
				/*Cell king = api.getPiecesByTypeAndColor(PieceType.KING, color).get(0);
				
				List<Cell> kingsMoves = api.getAllSafeMoves(king);
				List<Cell> kingsCaptures = api.getAllSafeCaptures(king); 
				List<Cell> whoEndangersKing = api.getWhoEndangersMe(king);//TODO - is it possible to have more than one here? If yes, we have a problem...
				List<Cell> whoCanSaveKing = api.getWhoCanDefendMe(king);
				
				//check if there is a saver, use it
				for(Cell attacker: whoEndangersKing) {
					if(whoCanSaveKing.size() > 0) {
						
						for(Cell saver : whoCanSaveKing) {
							List<Move> possibleMoves = api.getAllPossibleMoves(saver);
							for (Move m : possibleMoves) {
								if(m.getCondition() == Condition.ATTACK) {
									if(attacker.equals(m.getDistination())) {
										System.out.println("I found a saver");
										api.doMove(m);
										return;
									}
								}
							}
						}
					}
				}
				
				System.out.println("No saver found, I will try to find if my king can defend himself");
				//TODO move if you have an option
				System.out.println("Checking if there is place to move...");
				
				if(kingsMoves.size() > 0) {
					System.out.println("There is place to move. I am moving...");
					int value = rand.nextInt(kingsMoves.size()); 
					Move m = new Move(king.getPiece(), king, kingsMoves.get(value), Condition.ATTACK );
					api.doMove(m);
					return;
				}
				
				System.out.println("Nowhere to move, ");
				//if there is no savers and nowhere to move, try to save yourself
				for(Cell capture : kingsCaptures) {
					for(Cell attacker: whoEndangersKing) {
						if(capture.equals(attacker)) {
							System.out.println("Okey, my king can attack!");
							Move m = new Move(king.getPiece(), king, attacker, Condition.ATTACK );
							api.doMove(m);
							return;
						}
					}
				}
				
				System.out.println("Well, perheps it is mate... ");*/
			}
	
	private int getAttackWeight(Move m) {
		int finalWeight = 0;
		debugLog("getAttackWeight");
		
		//TODO First, we check if the figure is under attack by a figure with lower rating, we greatly increase weight 
		//for any safe move
		//check if it works
		List<Cell> attackers = api.getWhoEndangersMe(m.getOriginal());
		
		if(DEBUG && attackers.size() > 0) {
			System.out.println("before increase " + m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
		}
		for(Cell attacker : attackers) {
			
			debugLog("attacker: " + attacker.getPiece().getType() + " " + attacker.getNotation());
			
			if(attacker.getPiece().getPieceValue() < m.getPiece().getPieceValue()) {
				// if the pawn attacks the queen, it is 9 - 1 = 8 points plus to force escape
				// we simple accumulate all attackers differences in case if there are more than one attacker
				System.out.println("increasing wight by " + (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue()));
				finalWeight += (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue());
			}
			// it is not nice when you are attacked
			else {
				System.out.println("incrimental increasing wight by 1");
				
				finalWeight++;
			}
		}
		
		if(m.getPiece().getType() == PieceType.PAWN) {
			// For pawns we value two cell move then one cell move
			int diff = m.getOriginal().getRow() - m.getDistination().getRow();
			if(diff == -2 || diff == 2) {
				return finalWeight++;
			}
		}
		
		
		
		if(api.isMoveVeryBad(m)) {
			finalWeight -= 2;
		}
		
		return finalWeight;
	}
	
	/**
	 * Getting sub weight. Here are some thoughts:
	 * - For pawns, 2 cells move is slightly better than 1 cell move (1 point)
	 * - If move leads to loosing mover, it is not good (-2 points)
	 * @param m
	 * @return
	 */
	private int getMoveWeight(Move m) {
		
		int finalWeight = 0;
		debugLog("getMoveWeight");
		//TODO First, we check if the figure is under attack by a figure with lower rating, we greatly increase weight 
		//for any safe move
		//check if it works
		List<Cell> attackers = api.getWhoEndangersMe(m.getOriginal());
		
		if(DEBUG && attackers.size() > 0) {
			System.out.println("before increase " + m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
		}
		for(Cell attacker : attackers) {
			
			debugLog("attacker: " + attacker.getPiece().getType() + " " + attacker.getNotation());
			
			if(attacker.getPiece().getPieceValue() < m.getPiece().getPieceValue()) {
				// if the pawn attacks the queen, it is 9 - 1 = 8 points plus to force escape
				// we simple accumulate all attackers differences in case if there are more than one attacker
				System.out.println("increasing wight by " + (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue()));
				finalWeight += (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue());
			}
			// it is not nice when you are attacked
			else {
				System.out.println("incrimental increasing wight by 1");
				
				finalWeight++;
			}
		}
		
		if(m.getPiece().getType() == PieceType.PAWN) {
			// For pawns we value two cell move then one cell move
			int diff = m.getOriginal().getRow() - m.getDistination().getRow();
			if(diff == -2 || diff == 2) {
				return finalWeight++;
			}
		}
		
		
		
		if(api.isMoveVeryBad(m)) {
			finalWeight -= 2;
		}
		
		return finalWeight;
	}

	private void debugLog(String string) {
		if(DEBUG) {
			System.out.println(string);
		}
	}
	
	@Override
	public Move rankForTheBestMove(PieceColor pc) {
		List<Move> moves = api.getGoodMoves(pc);
		return thinkForTheBestMove(moves);
	}

	@Override
	public Map<Cell, List<MovePower>> insightForAssistant(PieceColor myColor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Condition checkForExceptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
