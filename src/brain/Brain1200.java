package brain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import api.ChessGameConnect;
import game.ChessBoardController;
import game.entities.Cell;
import game.entities.Move;
import game.entities.MovePower;
import game.entities.PiecePower;
import game.entities.Tactics;
import game.entities.pieces.King;
import game.entities.pieces.Piece;
import game.entities.types.Condition;
import game.entities.types.GameStatus;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Config;

public class Brain1200 implements BrainInterface{
	
	private final int GOOD_MOVE_THRESHOLD = 50;
	private final int UPPER_LIMIT = 100;
	private Random rand; 
	private ChessGameConnect api;
	private boolean DEBUG = Config.DEBUG; 
	private PieceColor color;
	
	public Brain1200(ChessGameConnect api, PieceColor color) {
		this.api = api;
		this.color = color;
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
	public Move rankForTheBestMove(PieceColor pc) {
		//The idea is to access current situation (all pieces) and try to figure out how to improve situation (higher score) by moving pieces
		
		/**
		 * Ideas:
		 * First idea is to find best move - move with highest possible weight
		 * 1 can be reusable for next brain (where each piece is accessed by highest weight to calculate the total rating of the position and than to define what move can improve the rating)
		 * 1.1 Go through all moves and try to access them by weighting
		 * 1.2 Sort all moves by weights 
		 * 2 Sort again by comparing two neighbors and re-accessing their weights comparing to each other
		 * 
		 * TRY THIS !!!!! 
		 * Second idea is to find the way how to assess position - how to score the position - and to find the move that improves the position to maximum within 2 moves.
		 * 1. Assess my position
		 * 1.1 Traverse through each my piece and evaluate its power
		 * 1.1.1 Move the piece and guess what opponent can do
		 * 		- Will he/she eat my piece - Can I eat him back = will it be fair exchange and will it give me ability to eat something bigger?
		 * 		- Will he/she eat my other piece? If yes, will be the same scenario as above?
		 * 		- Will my move make opponents life harder?
		 * 			After evaluation all above:
		 * 			- If the piece is active (in strong position) - increase the score (depending on how strong)
		 * 			- If the piece is passive (needs development) - nothing changes
		 * 			- If the piece is a victim (loosing the piece or unequal exchange) - decrease the score (depending on how weak)
		 * 2. Assess my opponents position
		 * 3. If I am stronger then I need to attack, if I am weaker then I need to defend 
		 */
		PieceColor opponentColor;
		
		if(color == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		ChessBoardController controller = api.cloneBoard();
		// Assign weight //first not that smart ranking idea
		Map<Cell, List<MovePower>> myPositionMap = rankPieces(color, controller);
		
		controller = api.cloneBoard();
		Map<Cell, List<MovePower>> opponentPositionMap = rankPieces(opponentColor, controller);
		
		int myTotalDefendingPower = 0;
		int myTotalAttackingPower = 0;
		int opponentTotalDefendingPower = 0;
		int opponentTotalAttackingPower = 0;
		
		for (Map.Entry<Cell, List<MovePower>> entry : myPositionMap.entrySet())
		{
		    log((entry.getKey()).getPiece().getColor() + " " + (entry.getKey()).getPiece() + "[" + (entry.getKey()).getNotation() + "]" + ":" + entry.getValue());
		    
		    for(MovePower mp : entry.getValue()) {
			    myTotalDefendingPower += mp.myDeltaDefense;
			    myTotalAttackingPower += mp.myDeltaAttack;
		    }
		}
		
		for (Map.Entry<Cell, List<MovePower>> entry : opponentPositionMap.entrySet())
		{
			log((entry.getKey()).getPiece().getColor() + " " + (entry.getKey()).getPiece() + "[" + (entry.getKey()).getNotation() + "]" + ":" + entry.getValue());
			
			for(MovePower mp : entry.getValue()) {
				opponentTotalDefendingPower += mp.myDeltaDefense;
				opponentTotalAttackingPower += mp.myDeltaAttack;
		    }
		}
		
		//log("my defending power is " + myTotalDefendingPower + " my attacking power is " + myTotalAttackingPower);
		//log("my opponent's defending power is " + opponentTotalDefendingPower + " my opponent's attacking power is " + opponentTotalAttackingPower);
		
		//Define how good attack comparing to the opponent
		int deltaAttack = myTotalAttackingPower - opponentTotalAttackingPower;
		//Define how good defense comparing to the opponent
		int deltaDefense = myTotalDefendingPower - opponentTotalDefendingPower;
		
		Tactics myTactics = Tactics.NEUTRAL;
		
		Condition currentCondition = checkForExceptions();
		
		if(currentCondition == Condition.CHECK) {
			if(deltaAttack > -5 &&  deltaDefense > 0)
				myTactics = Tactics.MODERATE_EXCEPTION;
			else 
				myTactics = Tactics.CRITICAL_EXCEPTION;
		}
		//very good attack
		else if(deltaAttack > 25) {
			//and if I have very strong defense I have very good attacking position
			if(deltaDefense > 15) {
				myTactics = Tactics.AGGRESSIVE;
			}
			else if(deltaDefense > 0) {
				myTactics = Tactics.ATTACKING;
			}
			else if(deltaDefense > -15) {
				myTactics = Tactics.DEFENDING;
			}
			else {
				myTactics = Tactics.NEUTRAL;
			}
		}			
		//somehow good attack
		else if(deltaAttack > -5) {
			if(deltaDefense > 15) {
				myTactics = Tactics.ATTACKING;
			}
			else if(deltaDefense > 0) {
				myTactics = Tactics.NEUTRAL;
			}
			else if(deltaDefense > -15) {
				myTactics = Tactics.DEFENDING;
			}
			else {
				myTactics = Tactics.WEAK;
			}
		}
		//not good attack
		else {
			if(deltaDefense > 15) {
				myTactics = Tactics.NEUTRAL;
			}
			else if(deltaDefense > 0) {
				myTactics = Tactics.DEFENDING;
			}
			else {
				myTactics = Tactics.WEAK;
			}
		}
			
		log("My tactics is " + myTactics);
		
		Map<Cell, List<MovePower>> rankedPostionbyTactics = rankPositionByTactics(myPositionMap, myTactics);
		
		Map<Cell, List<MovePower>> rankedGoodPieces = getGoodPieces(rankedPostionbyTactics);

		Move bestMove = chooseBestMoveByPiece(rankedGoodPieces);
		
		return bestMove;
	}
	
	/**
	 * filter out pieces that has not place to move
		
	 * @param rankedPostionbyTactics
	 * @return
	 */
	private Map<Cell, List<MovePower>> getGoodPieces(Map<Cell, List<MovePower>> rankedPostionbyTactics) {
		for (Map.Entry<Cell, List<MovePower>> entry : rankedPostionbyTactics.entrySet()) {
			if(entry.getValue().size() == 0)
				rankedPostionbyTactics.remove(entry.getKey());
		}
		
		return rankedPostionbyTactics;
	}

	/**
	 * We have all move ratings, what move is the best?
	 * @param cell
	 * @param myRating
	 * @param opponentRating
	 * @return
	 */
	private Move chooseBestMoveByPiece(Map<Cell, List<MovePower>> rankedGoodPieces) {
		List<MovePower> bestMovesPowers = new ArrayList<>();
		
		for (Map.Entry<Cell, List<MovePower>> entry : rankedGoodPieces.entrySet()) {
			bestMovesPowers.addAll(entry.getValue());
		}
		
		
		for(MovePower mp : bestMovesPowers) {
			log("before sort move power: " + mp.toString());
		}
		
		
		Collections.sort(bestMovesPowers, Collections.reverseOrder());
		
		
		for(MovePower mp : bestMovesPowers) {
			log("move power: " + mp.toString());
		}
		
		Move bestMove = bestMovesPowers.get(0).move;
		
		
		return bestMove;
	}

	@Override
	public Map<Cell, List<MovePower>> insightForAssistant(PieceColor myColor) {
		PieceColor opponentColor;
		
		if(myColor == PieceColor.BLACK) 
			opponentColor = PieceColor.WHITE;
		else
			opponentColor = PieceColor.BLACK;
		
		ChessBoardController controller = api.cloneBoard();
		
		Map<Cell, List<MovePower>> positionMap = rankPieces(myColor, controller);
		
		controller = api.cloneBoard();
		Map<Cell, List<MovePower>> opponentPositionMap = rankPieces(opponentColor, controller);
		positionMap.putAll(opponentPositionMap);
		
		return positionMap;
	}
	
	/**
	 * Finds which tactics is better suited for the current position
	 * @param pc
	 */
	public Map<Cell, List<MovePower>> rankPositionByTactics(Map<Cell, List<MovePower>> positionMap, Tactics myTactis) {
		for (Map.Entry<Cell, List<MovePower>> entry : positionMap.entrySet())
		{
		    //log(entry.getKey() + "/" + entry.getValue());
			List<MovePower> ppList = entry.getValue();
			
			for(MovePower pp : ppList) {
				//pp.setCell(entry.getKey());
				double attackingCoef = 1;
				double defendingCoef = 1;
				
				switch(myTactis) {
					case AGGRESSIVE:
						attackingCoef = 1.2;
						defendingCoef = 1.1;
						break;
					case ATTACKING:
						attackingCoef = 1.1;
						defendingCoef = 1.0;
						break;
					case DEFENDING:
						attackingCoef = 0.9;
						defendingCoef = 0.9;
						break;
					case WEAK:
						attackingCoef = 0.9;
						defendingCoef = 0.8;
						break;
					case NEUTRAL:
						attackingCoef = 0.99;
						defendingCoef = 0.99;
						break;
					default:
						break;
				}
				
				pp.myDeltaAttackByTactics = ((int)(pp.myDeltaAttack * attackingCoef));
				pp.myDeltaDefenseByTactics = ((int)(pp.myDeltaDefense * defendingCoef));
				//Set stability
				//log("attackingCoef:"+attackingCoef+" defendingCoef:"+defendingCoef);
				//TODO do we need this?
				int positionStability = (int)(((attackingCoef + defendingCoef)/2) * ((pp.myDeltaAttackByTactics + pp.myDeltaDefenseByTactics) /2));
				
				pp.myRating  = positionStability;
			}
			
		}
		
		//List<PiecePower> list = new ArrayList<>(positionMap.values());
        
        //Collections.sort(list);
		
		return positionMap;
	}
	
	@Override
	public Move thinkForTheBestMove(List<Move> goodMoves) {
		
		/*for(Move m: goodMoves) {
			int weight = getWeight(m);
			m.setWeight(weight);
		}
		
		//Print out move rating
		log("Here is a rating of all moves");
		
		Collections.sort(goodMoves);
		
		for(Move m: goodMoves) {
			log(m.getWeight() + " " + m.getCondition() + " " + m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
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
		}*/
		
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
	
	public Map<Cell, List<MovePower>> rankPieces(PieceColor pc, ChessBoardController controller) {
		// Get the board to calculate position rating
		//TODO Temporary we give some power to the brain to experiment with cloned controller but 
		//that would be todo item to move the functionality to controller or manager in order to use it in other places
		Map<Cell, List<MovePower>> positionMap = new HashMap<Cell, List<MovePower>>();
		//Cell [][] cells = controller.getBoardCells();
		
		//First we assess positions of all my pieces
		List<Cell> pieces = controller.getPiecesByColor(pc);

		for(Cell piece : pieces) {
			ChessBoardController tmpController = controller.clone();
			List<MovePower> defendingPower = assessPieceDefendingPower(piece, tmpController);
			List<MovePower> attackingPower = assessPieceAttackingPower(piece, tmpController);
			List<MovePower> stability = assessStability(piece, attackingPower, defendingPower);
			
			//PiecePower piecePower = new PiecePower(piece.getPiece().getPieceValue(), attackingPower, defendingPower, stability);
			
			positionMap.put(piece, stability);
		}
		
		return positionMap;
	}

	private List<MovePower> assessStability(Cell piece, List<MovePower> attackingPower, List<MovePower> defendingPower) {
		List<MovePower> listOfMovePower = defendingPower;
		
		//TODO - improve
		for(MovePower dmp : listOfMovePower) {
			int i = 0;
			
			for(MovePower amp : attackingPower) {
				if(dmp.move.equals(amp.move) ) {
					dmp.myDeltaAttack = amp.myDeltaAttack;
					attackingPower.remove(i);
					break;
				}
				i++;
			}
		}
		
		listOfMovePower.addAll(attackingPower);
		
		return listOfMovePower;
	}

	/**
	 * Piece power consists of:
	 * - Piece cost
	 * - Attacking power (self describing)
	 * - Defending power - ability to defend other its pieces - it is based on number of choices, what choices are, how a piece is 
	 * defended etc:
	 * 	- 75 - 100 if somebody attacks me but I can beat him with my advantage
	 * 	- 75 if there is nothing that attacks me
	 *  - 15 - 50 if I am just loosing the piece 
 	 * 	- 0 - 15 if I can defend attacker but with my big disadvantage
	 * - Position (how stable position it is)
	 * 
	 * @param piece
	 * @param controller
	 * @return
	 */
	private List<MovePower> assessPieceDefendingPower(Cell cell, ChessBoardController controller) {
		Piece piece = cell.getPiece();
		PiecePower piecePower = new PiecePower();
		piecePower.setPieceCost(cell.getPiece().getPieceValue());
		//double defendingPower = 100;
		
		List<Move> goodMoves = controller.getAllPossibleMoves(cell);
		
		
		//Assess current piece position and inject it first move
		Move dummyMove = new Move(piece, cell, cell, Condition.TEMPORARY);
		ChessBoardController tmpController = controller.clone();
		double positionPowerBeforeMove = assessPositionPowerFromTheMove(dummyMove, tmpController);
		goodMoves.add(0, dummyMove);
		
		//dangerLevel += assessDangerFromTheMove(dummy, controller);
		
		//Assess potential positions
		//Idea:
		//More moves = more freedom but not linear 
		//ax2 + bx + c = y
		//int a = 2;
		//int b = 3;
		//int c = 4;
		//Collect all danger levels of all moves and then to sort them and apply coef (more moves more options/less danger)
		//ArrayList<Double> listOfPositionsPower = new ArrayList<>();
		List<MovePower> listOfMovePower = new ArrayList<>();
		//ArrayList<Double> listOfCoefs = new ArrayList<>();
		
		for(int i = 0; i < goodMoves.size(); i++) {
			MovePower mp = new MovePower();
			//before we do anything we clone controller
			tmpController = controller.clone();
			
			Move move = goodMoves.get(i);
			//int x = i;
			//double coef = a * (x * x) + (b * x) + c;
			//coef = Math.round(Math.log(coef));

			//1. Is it in danger and could be eaten? (danger level out of 100)
			double positionPowerFromTheMove = assessPositionPowerFromTheMove(move, tmpController);
			//listOfPositionsPower.add(positionPowerFromTheMove);
			// more options more power
			//double f = 1 - ((1 / (1 + Math.pow(coef, -coef))) * 0.1);
			//listOfCoefs.add(f);
			
			double delta = positionPowerFromTheMove - positionPowerBeforeMove;
			
			//Compare positions before and after move
			log(move + " defending delta : " + delta);
			
			mp.move = move;
			mp.myDeltaDefense = (int)delta;
				
			listOfMovePower.add(mp);
		}
		
		//No need for all this, I am leaving just to trace ideas
		// Original idea is to summarize all impacts from moves to make average, but it looks like bad idea as we are loosing a lot of interesting particulars
		/*//sort with less powered positions on top
		Collections.sort(listOfPositionsPower);
		
		double accumulativeDangerLevel = 0;
		
		//applying coefficients to position powers
		for(int i = 0; i < listOfPositionsPower.size(); i++) {
			double updatedPositionPower = listOfPositionsPower.get(i);
			
			double coef = listOfCoefs.get(i);
			listOfPositionsPower.set(i, (coef * updatedPositionPower));
			
			accumulativeDangerLevel = (accumulativeDangerLevel + updatedPositionPower) / 2;
			
			//log("updatedPositionPower:" + updatedPositionPower + " & coef:" + coef + " = " + accumulativeDangerLevel);
		}
		
		//Handle the case when there is nowhere to go - size 1 means that only staying at the current position
		if(listOfPositionsPower.size() != 0)
			defendingPower = accumulativeDangerLevel;
		
		return (int)defendingPower;*/
		return listOfMovePower;
	}

	private double assessPositionPowerFromTheMove(Move move, ChessBoardController controller) {
		int positionPower = 0;
		Piece original = move.getPiece();
		//we try to predict situation here, so we just clone current controller and play with it
		ChessBoardController tmpController = controller.clone();
		tmpController.doMove(move);
		
		Cell newCell = tmpController.getRealCell(move.getDistination());
		List<Cell> listOfOpponentAttackers = tmpController.getWhoEndangersMe(newCell);
		//log(list.size() + " danger peices for me");
		//safest position
		//Nobody endangers me
		if(listOfOpponentAttackers.size() == 0) return 75;
		
		//Let's iterate through those who can eat me
		for(Cell opponent : listOfOpponentAttackers) {
			//Let's pretend that the opponent eats me, what's next?
			ChessBoardController copyOfTmpController = tmpController.clone();
			opponent = copyOfTmpController.getRealCell(opponent);
			//Cell attackingCell = new Cell();
			
			Move fakeMove = new Move(opponent.getPiece(), opponent, move.getDistination(), Condition.ATTACK);
			//!!!IT MOVES BUT LEAVES IN OLD CELL ITS CLONE
			//Probably Original cell is referenced by something else
			copyOfTmpController.doMove(fakeMove);
			//reset opponent
			opponent = copyOfTmpController.getRealCell(move.getDistination()); 
			
			Cell afterMove = copyOfTmpController.getRealCell(move.getDistination()); 
			
			List<Move> savers = copyOfTmpController.getWhoCanEatThisPiece(afterMove);
			
			//If nobody can help me, I am just loosing the piece
			if(savers.size() == 0) {
				positionPower = 15;
			}
			else {
				int maxAssessment = 0;
				
				for (Move saver : savers) {
					int tempAssessment = 0;
					int diff = opponent.getPiece().getPieceValue() - original.getPieceValue();
					int points = 0;
					if(diff > 0) 
						points = 1;
					else if(diff < 0)
						points = -1;
					else
						points = 0;
					
					tempAssessment = 75 + (50 * points);
					
					if(tempAssessment > maxAssessment) {
						maxAssessment = tempAssessment;
					}
				}
				
				positionPower = maxAssessment;
			}
		}
		
		return (positionPower > 100) ? 100 : positionPower;
	}
	
	private List<MovePower> assessPieceAttackingPower(Cell cell, ChessBoardController controller) {
		PiecePower piecePower = new PiecePower();
		piecePower.setPieceCost(cell.getPiece().getPieceValue());
		double attackingPower = 25;
		
		ChessBoardController tmpController = controller.clone();
		Move dummyMove = new Move(cell.getPiece(), cell, cell, Condition.TEMPORARY);
		
		double positionPowerBeforeMove = assessPositionPowerFromTheMove(dummyMove, tmpController);
		
		List<Move> attackingMoves = controller.getAllPossibleCaptures(cell);
		List<MovePower> listOfMovePower = new ArrayList<>();
		
		if(attackingMoves.size() == 0)
			return listOfMovePower;

		//Collect all danger levels of all moves and then to sort them and apply coef (more moves more options/less danger)
		//ArrayList<Double> listOfAttackingPower = new ArrayList<>();
		
		for(int i = 0; i < attackingMoves.size(); i++) {
			MovePower mp = new MovePower();
			tmpController = controller.clone();
			Move move = attackingMoves.get(i);

			//1. Is it in danger and could be eaten? (danger level out of 100)
			double attackingPowerFromTheMove = assessAttackingPowerFromTheMove(move, tmpController);
			//listOfAttackingPower.add(attackingPowerFromTheMove);
			double delta = attackingPowerFromTheMove - positionPowerBeforeMove;
			//1.2.1 Am I attacking?
			//TODO code
			//1.2.2 is it equal exchange? (calc the difference: plus or minus diff)
			//TODO code
			//1.3. If it is not in danger 
			
			//Compare positions before and after move
			log(move + " attacking delta : " + delta);
			
			mp.move = move;
			mp.myDeltaAttack = (int)delta;
				
			listOfMovePower.add(mp);
		}
		
		
		//sort with less powered positions on top
		/*Collections.sort(listOfAttackingPower);
		
		double accumulativeAttackingLevel = 0;
		
		//applying coefficients to position powers
		for(int i = 0; i < listOfAttackingPower.size(); i++) {
			double updatedAttackingPower = listOfAttackingPower.get(i);
			
			if(i == 0) 
				accumulativeAttackingLevel = updatedAttackingPower;
			
			accumulativeAttackingLevel =  (accumulativeAttackingLevel + updatedAttackingPower) / 2;
			
			//log("updatedPositionPower:" + updatedPositionPower + " & coef:" + coef + " = " + accumulativeDangerLevel);
		}
		
		//Handle the case when there is nowhere to go - size 1 means that only staying at the current position
		if(listOfAttackingPower.size() != 0)
			attackingPower = accumulativeAttackingLevel;
		
		return (int)attackingPower;*/
		return listOfMovePower;
	}

	private double assessAttackingPowerFromTheMove(Move move, ChessBoardController controller) {
		int attackingPower = 25;
		int deltaStability = 0;
		Piece myPiece = move.getPiece();
		
		Cell opponent = controller.getRealCell(move.getDistination());
		
		ChessBoardController tmpController = controller.clone();
		
		double positionPowerBeforeMove = assessPositionPowerFromTheMove(move, tmpController);
		
		tmpController.doMove(move);
		Cell newCell = tmpController.getRealCell(move.getDistination());
		List<Cell> listOfOpponentAttackers = tmpController.getWhoEndangersMe(newCell);
		
		//Nobody endangers me
		if(listOfOpponentAttackers.size() == 0) {
			int tempAssessment = 0;
			int diff = myPiece.getPieceValue() - opponent.getPiece().getPieceValue();
			int bonusPoints = 0;
			if(diff > 0) 
				bonusPoints = diff;
			else
				bonusPoints = 0;
			
			tempAssessment = 75 + (5 * bonusPoints);
			return (tempAssessment > 100) ? 100 : tempAssessment;
		}
		
		Move dummyMove = new Move(myPiece, newCell,newCell, Condition.TEMPORARY);
		double positionPowerAfterMove = assessPositionPowerFromTheMove(dummyMove, tmpController);
		
		log(move + " attacking delta:" + (positionPowerAfterMove - positionPowerBeforeMove));
		double delta = positionPowerAfterMove - positionPowerBeforeMove;
		
		//Compare positions before and after move
		if(delta > 0) {
			//do something here
		}
		
		return (attackingPower > 100) ? 100 : attackingPower;
	}

	private int getWeight(Move m) {
		int weight = 0;
		Condition cond = m.getCondition();
		
		if(cond == Condition.POSSIBLE_PROMOTION)
			weight = 80 + getMoveWeight(m);
		else if(cond == Condition.CASTLING_ON_LEFT || cond == Condition.CASTLING_ON_RIGHT)
			weight = 60;
		else if(cond == Condition.EN_PASSANT_LEFT || cond == Condition.EN_PASSANT_RIGHT )
			weight = 55 + getMoveWeight(m);
		else if(cond == Condition.ATTACK) { //TODO is attack better then move?
			int attackWeight = 50 + getMoveWeight(m);
			weight = attackWeight;
		}
		else if(cond == Condition.MOVE) {
			int moveWeight = 49 + getMoveWeight(m);
			weight = moveWeight;
		}
		else
			weight = 10;
		
		return weight;
	}

	@Override
	public Condition checkForExceptions() {
		//first,  checks if she is in danger
		if(api.checkIfItIsCheck(GameStatus.BLACK_MOVE)) {
			
			List<Move> moves = api.checkIfItIsMate(GameStatus.BLACK_MOVE);
		
			if(moves.size() == 0) {
				return Condition.MATE;
			}
			else
			{
				return Condition.CHECK;
			}
		}
		
		return Condition.UNKNOWN;
	}
	
	//TODO improve, it is very dummy solution right now
	@Override
	public Move saveMyKing(List<Move> safeMoves) {
		log("It is not mate, it is good, now I need to choose a good move to save my king");
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
										log("I found a saver");
										api.doMove(m);
										return;
									}
								}
							}
						}
					}
				}
				
				log("No saver found, I will try to find if my king can defend himself");
				//TODO move if you have an option
				log("Checking if there is place to move...");
				
				if(kingsMoves.size() > 0) {
					log("There is place to move. I am moving...");
					int value = rand.nextInt(kingsMoves.size()); 
					Move m = new Move(king.getPiece(), king, kingsMoves.get(value), Condition.ATTACK );
					api.doMove(m);
					return;
				}
				
				log("Nowhere to move, ");
				//if there is no savers and nowhere to move, try to save yourself
				for(Cell capture : kingsCaptures) {
					for(Cell attacker: whoEndangersKing) {
						if(capture.equals(attacker)) {
							log("Okey, my king can attack!");
							Move m = new Move(king.getPiece(), king, attacker, Condition.ATTACK );
							api.doMove(m);
							return;
						}
					}
				}
				
				log("Well, perheps it is mate... ");*/
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
		log("getMoveWeight");
		//TODO First, we check if the figure is under attack by a figure with lower rating, we greatly increase weight 
		//for any safe move
		//check if it works
		List<Cell> attackers = api.getWhoEndangersMe(m.getOriginal());
		
		if(DEBUG && attackers.size() > 0) {
			log("before increase " + m.getPiece().getType() + " " + m.getOriginal().getNotation() + ":" + m.getDistination().getNotation());
		}
		for(Cell attacker : attackers) {
			
			log("attacker: " + attacker.getPiece().getType() + " " + attacker.getNotation());
			
			if(attacker.getPiece().getPieceValue() < m.getPiece().getPieceValue()) {
				// if the pawn attacks the queen, it is 9 - 1 = 8 points plus to force escape
				// we simple accumulate all attackers differences in case if there are more than one attacker
				log("increasing wight by " + (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue()));
				finalWeight += (m.getPiece().getPieceValue() - attacker.getPiece().getPieceValue());
			}
			// it is not nice when you are attacked
			else {
				log("incrimental increasing wight by 1");
				
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

	private void log(String string) {
		if(DEBUG) {
			System.out.println(string);
		}
	}
}
