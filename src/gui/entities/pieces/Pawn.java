package gui.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.Move;
import gui.entities.types.Condition;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.MoveType;
import gui.entities.types.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Pawn extends Piece{
	//flag to temporary include potential moves and captures, must be reset back after each use
	private boolean includePotentials = false;
	
	
	public Pawn(ChessBoardController controller, PieceColor color, String id) {
		super(controller);
		init(color, id, this.getClass().getSimpleName());
		
		pieceValue = 1;
	}
	
	@Override
	public PieceType getType() {
		return PieceType.PAWN;
	}
	
	@Override
	public void initPatterns() {
		//if this is the first move, you are allowed to move one or two cells forward
		Pattern p1 = new Pattern(Condition.FIRST_MOVE);
		p1.addMove(MoveType.FORWARD);
		patterns.add(p1);
		
		Pattern p2 = new Pattern(Condition.FIRST_MOVE);
		p2.addMove(MoveType.FORWARD);
		p2.addMove(MoveType.FORWARD);
		patterns.add(p2);
		
		//one cell forward for other cases
		Pattern p3 = new Pattern(Condition.MOVE);
		p3.addMove(MoveType.FORWARD);
		patterns.add(p3);
		
		//Attacking patterns
		Pattern p4 = new Pattern(Condition.ATTACK);
		p4.addMove(MoveType.FORWARD_LEFT);
		patterns.add(p4);
		
		Pattern p5 = new Pattern(Condition.ATTACK);
		p5.addMove(MoveType.FORWARD_RIGHT);
		patterns.add(p5);
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
	
	private boolean isEmpty(Cell check, boolean includeImpossible) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		
		if(includeImpossible)
			return true;
		else if(temp != null && temp.isEmpty())
			return true;
		else
			return false;
	}
	
	@Override
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		//if this is the first move
		if((current.getRow() == 1 && color == PieceColor.BLACK) || (current.getRow() == 6 && color == PieceColor.WHITE)) {
				for(Pattern pattern : getPatterns(Condition.FIRST_MOVE)) {
					Cell temp = current.copy();
					boolean isValid = true;
					for(MoveType move: pattern.getMoves()) {
						//if black (top), moving ++
						if(color == PieceColor.BLACK) {
							switch(move) {
								case FORWARD:
									temp.moveDown(1);
									break;
								default:
							}
						}
						else {
							switch(move) {
								case FORWARD:
									temp.moveUp(1);
									break;
								default:
							}
						}
						if(!isEmpty(temp, includeImpossible)) {
							isValid = false;
							break;
						}
					}
					//pattern is finished, add final cell
					if(isValid) {
						moves.add(temp);
					}
					else
					{
						break;
					}
				}
				return moves;
		}
		//the regular move
		else {
			for(Pattern pattern : getPatterns(Condition.MOVE)) {
				Cell temp = current.copy();
				boolean isValid = true;
				
				for(MoveType move: pattern.getMoves()) {
					//if black (top), moving ++
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD:
								temp.moveDown(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD:
								temp.moveUp(1);
								break;
							default:
						}
					}
					if(!isEmpty(temp, includeImpossible)) {
						isValid = false;
						break;
					}
				}
				//pattern is finished, add final cell
				if(isValid) {
					moves.add(temp);
				}
				else
				{
					break;
				}
			}
			return moves;
		}
	}
	
	private boolean isGoodForAttack(Cell check) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		//if cell is empty, it is empty for all regular moves (non attacks) 
		//if cell is not empty but we assume that there is attack (which means that opposite color)- except potential attacks where cell might be empty
		if(includePotentials && (temp.isEmpty() || (check.getPiece().getColor() != temp.getPiece().getColor()))) {
			return true;
		}
		else if(!includePotentials && !temp.isEmpty() && (check.getPiece().getColor() != temp.getPiece().getColor())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible) {
		//if this is attack
		List<Cell> moves = new ArrayList<>();
		for(Pattern pattern : getPatterns(Condition.ATTACK)) {
			Cell temp = current.copy();
			for(MoveType move: pattern.getMoves()) {
				//if black (top), moving ++
				if(color == PieceColor.BLACK) {
					switch(move) {
					case FORWARD_RIGHT:
						temp.moveDownLeft(1);
						break;
					case FORWARD_LEFT:
						temp.moveDownRight(1);
						break;
					default:
					}
				}
				else {
					switch(move) {
					case FORWARD_RIGHT:
						temp.moveUpRight(1);
						break;
					case FORWARD_LEFT:
						temp.moveUpLeft(1);
						break;
					default:
					}
				}
				//pattern is finished, add final cell
				if(isGoodForAttack(temp)) {
					moves.add(temp);
				}
				
			}
			
		}	
		return moves;
	}

	@Override
	public List<Cell> getAllPotentialCaptures(Cell p) {
		includePotentials = true;
		List<Cell> potentials = getAllCaptures(p, true);
		includePotentials = false;
		return potentials;
	}

	@Override
	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell) {
		for(Pattern pattern : getPatterns(Condition.ATTACK)) {
			Cell temp = myCell.copy();
			for(MoveType move: pattern.getMoves()) {
				//if black (top), moving ++
				if(color == PieceColor.BLACK) {
					switch(move) {
					case FORWARD_RIGHT:
						temp.moveDownLeft(1);
						break;
					case FORWARD_LEFT:
						temp.moveDownRight(1);
						break;
					default:
					}
				}
				else {
					switch(move) {
					case FORWARD_RIGHT:
						temp.moveUpRight(1);
						break;
					case FORWARD_LEFT:
						temp.moveUpLeft(1);
						break;
					default:
					}
				}
				//pattern is finished, add final cell
				if(isValidForPotentialAttack(temp, newVictomCell, oldVictomCell)) {
					return true;
				}
				
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
		return new Pawn(controller, color, pieceID);
	}
}
