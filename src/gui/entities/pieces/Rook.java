package gui.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.types.Condition;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.MoveType;
import gui.entities.types.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Rook extends Piece{
		
	public Rook(ChessBoardController controller, PieceColor color, String id) {
		super(controller);
		init(color, id, this.getClass().getSimpleName());
		pieceValue = 5;
	}
	
	@Override
	public void initPatterns() {
		Pattern p1 = new Pattern(Condition.MOVE, true);
		p1.addMove(MoveType.FORWARD);
		patterns.add(p1);
		
		Pattern p2 = new Pattern(Condition.MOVE, true);
		p2.addMove(MoveType.BACK);
		patterns.add(p2);
		
		Pattern p3 = new Pattern(Condition.MOVE, true);
		p3.addMove(MoveType.LEFT);
		patterns.add(p3);
		
		Pattern p4 = new Pattern(Condition.MOVE, true);
		p4.addMove(MoveType.RIGHT);
		patterns.add(p4);
		
	}
	
	@Override
	public PieceType getType() {
		return PieceType.ROOK;
	}

	@Override
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			boolean isValid = true;
			
			//traverse till we meet non empty cell or border 
			while( true) {
				for(MoveType move: pattern.getMoves()) {
					temp = temp.copy();
					//if black (top), moving ++
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
					
					if( (temp.getCol() < 0 || temp.getCol() > 7) || (temp.getRow() < 0 || temp.getRow() > 7) ) {
						isValid = false;
						break;
					}
					else if(includeImpossible) {
						isValid = true;
					}
					else if(!isEmpty(temp, includeImpossible)) {
						isValid = false;
						break;
					}
					
					if(isValid) {
						moves.add(temp);
					}
					else
					{
						break;
					}
				}
				if(!isValid) break;
			} 
		}
		return moves;
	}
	
	private boolean isEmpty(Cell check, boolean includeImpossible) {
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		//if cell is empty, it is empty for all regular moves (non attacks)
		if(includeImpossible) {
			return true;
		}
		if(temp != null && temp.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isGoodForAttack(Cell check) {
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		//if cell is empty, it is empty for all regular moves (non attacks)
		//if cell is not empty but we assume that there is attack (which means that opposite color)
		
		if(!temp.isEmpty() && (check.getPiece().getColor() != temp.getPiece().getColor())) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			boolean toProceed = false;
			
			//traverse till we meet non empty cell or border 
			while( true) {
				for(MoveType move: pattern.getMoves()) {
					//temp = temp.copy();
					//if black (top), moving ++
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
						toProceed = false;
						break;
					}
					else if(isEmpty(temp, includeImpossible)) {
						toProceed = true;
						continue;
					}
					else if(isGoodForAttack(temp)) {
						toProceed = false;
						moves.add(temp);
						break;
					}
					else {
						toProceed = false;
						break;
					}
				}
				//for a while we assume that only one move exists in a pattern
				if(!toProceed) break;
			} 
			//if(!isValid) break;
		}
		return moves;
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
		List<Cell> potenialCaptures = getAllMoves(p, true);
		return potenialCaptures;
	}

	@Override
	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell) {
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = myCell.copy();
			boolean toProceed = false;
			
			//traverse till we meet non empty cell or border 
			while( true) {
				for(MoveType move: pattern.getMoves()) {
					//temp = temp.copy();
					//if black (top), moving ++
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
					//We move till end of the board or non empty space or victim found
					if((temp.getCol() < 0 || temp.getCol() > 7) || (temp.getRow() < 0 || temp.getRow() > 7)) {
						toProceed = false;
						break;
					}
					else if(temp.equals(newVictomCell)) {
						return true;
					}
					else if(isPotentiallyEmpty(temp, newVictomCell, oldVictomCell)) {
						toProceed = true;
						break;
					}
					else {
						toProceed = false;
						break;
					}
				}
				//for a while we assume that only one move exists in a pattern
				if(!toProceed) break;
			} 
		}
		return false;
	}
	
	private boolean isPotentiallyEmpty(Cell check, Cell newVictomCell, Cell oldVictomCell) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = controller.cells[check.getRow()][check.getCol()];
		
		if(check.equals(oldVictomCell)) {
			return true;
		}
		else if(check.equals(newVictomCell)) {
			return false;
		}
		else if(temp != null && temp.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
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
		return new Rook(controller, color, pieceID);
	}
}
