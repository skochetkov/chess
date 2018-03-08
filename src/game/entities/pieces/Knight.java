package game.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import game.ChessBoardController;
import game.entities.Cell;
import game.entities.types.Condition;
import game.entities.types.MoveType;
import game.entities.types.Pattern;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Knight extends Piece{
	
	public Knight(ChessBoardController controller, PieceColor color, String id) {
		super(controller);
		init(color, id, this.getClass().getSimpleName());
		pieceValue = 3;
	}
	
	@Override
	public void initPatterns() {
		Pattern p1 = new Pattern(Condition.MOVE, false);
		p1.addMove(MoveType.FORWARD);
		p1.addMove(MoveType.FORWARD);
		p1.addMove(MoveType.LEFT);
		patterns.add(p1);
		
		Pattern p2 = new Pattern(Condition.MOVE, false);
		p2.addMove(MoveType.FORWARD);
		p2.addMove(MoveType.FORWARD);
		p2.addMove(MoveType.RIGHT);
		patterns.add(p2);
		
		Pattern p3 = new Pattern(Condition.MOVE, false);
		p3.addMove(MoveType.FORWARD);
		p3.addMove(MoveType.RIGHT);
		p3.addMove(MoveType.RIGHT);
		patterns.add(p3);
		
		Pattern p4 = new Pattern(Condition.MOVE, false);
		p4.addMove(MoveType.FORWARD);
		p4.addMove(MoveType.LEFT);
		p4.addMove(MoveType.LEFT);
		patterns.add(p4);
		
		Pattern bp1 = new Pattern(Condition.MOVE, false);
		bp1.addMove(MoveType.BACK);
		bp1.addMove(MoveType.BACK);
		bp1.addMove(MoveType.LEFT);
		patterns.add(bp1);
		
		Pattern bp2 = new Pattern(Condition.MOVE, false);
		bp2.addMove(MoveType.BACK);
		bp2.addMove(MoveType.BACK);
		bp2.addMove(MoveType.RIGHT);
		patterns.add(bp2);
		
		Pattern bp3 = new Pattern(Condition.MOVE, false);
		bp3.addMove(MoveType.BACK);
		bp3.addMove(MoveType.RIGHT);
		bp3.addMove(MoveType.RIGHT);
		patterns.add(bp3);
		
		Pattern bp4 = new Pattern(Condition.MOVE, false);
		bp4.addMove(MoveType.BACK);
		bp4.addMove(MoveType.LEFT);
		bp4.addMove(MoveType.LEFT);
		patterns.add(bp4);
	}

	@Override
	public PieceType getType() {
		return PieceType.KNIGHT;
	}

	private boolean isEmpty(Cell check, boolean includeImpossible, boolean isAttack) {
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		
		if(isAttack) {
			if(temp != null && !temp.isEmpty() && (check.getPiece().getColor() != temp.getPiece().getColor()) ) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if(includeImpossible) {
				return true;
			}
			else if(temp != null && temp.isEmpty()) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible) {
		return getAllMoves(current, includeImpossible, false);
	}
	
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible, boolean isAttack) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			boolean isValid = false;
			//traverse till we meet non empty cell or border 
			//while(true) 
			{
				temp = temp.copy();
				
				for(MoveType move: pattern.getMoves()) {
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD:
								temp.moveDown(1);
								break;
							case BACK:
								temp.moveUp(1);
								break;
							case LEFT:
								temp.moveRight(1);
								break;
							case RIGHT:
								temp.moveLeft(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD:
								temp.moveUp(1);
								break;
							case BACK:
								temp.moveDown(1);
								break;
							case LEFT:
								temp.moveLeft(1);
								break;
							case RIGHT:
								temp.moveRight(1);
								break;
							default:
						}
					}
					
					if((temp.getCol() < 0 || temp.getCol() > 7) || (temp.getRow() < 0 || temp.getRow() > 7)) {
						isValid = false;
						break;
					}
					else {
						isValid = true;
					}
				}
				
				//break;
			} 
			
			if(isValid && isEmpty(temp, includeImpossible, isAttack)){
				moves.add(temp);
			}
		}
		return moves;
	}

	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible) {
		return getAllMoves(current, includeImpossible, true);
	}

	@Override
	public List<Pattern> getPatterns() {
		return patterns;
	}

	public List<Pattern> getPatterns(Condition condition) {
		List<Pattern> byCondition = new ArrayList<>();
		
		for(Pattern pattern: getPatterns()) {
			if(pattern.getCondition() == condition) {
				byCondition.add(pattern);
			}
		}
		return byCondition;
	}
	
	@Override
	public List<Cell> getAllPotentialCaptures(Cell p) {
		return getAllMoves(p, true);
	}

	@Override
	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell) {
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = myCell.copy();
			boolean isValid = false;
			//traverse till we meet non empty cell or border 
			//while(true) 
			{
				temp = temp.copy();
				
				for(MoveType move: pattern.getMoves()) {
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD:
								temp.moveDown(1);
								break;
							case BACK:
								temp.moveUp(1);
								break;
							case LEFT:
								temp.moveRight(1);
								break;
							case RIGHT:
								temp.moveLeft(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD:
								temp.moveUp(1);
								break;
							case BACK:
								temp.moveDown(1);
								break;
							case LEFT:
								temp.moveLeft(1);
								break;
							case RIGHT:
								temp.moveRight(1);
								break;
							default:
						}
					}
					
					if((temp.getCol() < 0 || temp.getCol() > 7) || (temp.getRow() < 0 || temp.getRow() > 7)) {
						isValid = false;
						break;
					}
					else {
						isValid = true;
					}
				}
				
				//break;
			} 
			
			if(isValid && isValidForPotentialAttack(temp, newVictomCell, oldVictomCell)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidForPotentialAttack(Cell check, Cell newVictomCell, Cell oldVictomCell) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		
		if(check.equals(oldVictomCell)) {
			return false;
		}
		else if(check.equals(newVictomCell)) {
			return true;
		}
		else if(temp != null && !temp.isEmpty() && (check.getPiece().getColor() != temp.getPiece().getColor()) ) {
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public Piece clonePiece(ChessBoardController controller) {
		return new Knight(controller, color, pieceID);
	}
}
