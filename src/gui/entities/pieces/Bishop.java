package gui.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.Piece;
import gui.entities.types.Condition;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.MoveType;
import gui.entities.types.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Bishop extends Piece{

	public Bishop(PieceColor color, String id) {
		super(color, id);
		pieceValue = 3;
	}

	@Override
	public void init() {
		ui = new HBox();
		Image image;
		
		if(getColor() == PieceColor.WHITE)
			image = new Image("resources/images/white_bishop.png");
		else
			image = new Image("resources/images/black_bishop.png");
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		ui.getChildren().add(imageView);
		initPatterns();
	}
	
	private void initPatterns() {
		Pattern p1 = new Pattern(Condition.MOVE, true);
		p1.addMove(MoveType.FORWARD_LEFT);
		patterns.add(p1);
		
		Pattern p2 = new Pattern(Condition.MOVE, true);
		p2.addMove(MoveType.FORWARD_RIGHT);
		patterns.add(p2);
		
		Pattern p3 = new Pattern(Condition.MOVE, true);
		p3.addMove(MoveType.BACK_LEFT);
		patterns.add(p3);
		
		Pattern p4 = new Pattern(Condition.MOVE, true);
		p4.addMove(MoveType.BACK_RIGHT);
		patterns.add(p4);
	}
	
	@Override
	public PieceType getType() {
		return PieceType.BISHOP;
	}
	
	private boolean isValidForAttack(Cell check) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = ChessBoardController.cells[check.getRow()][check.getCol()];
		
		if(temp != null && !temp.isEmpty() && (check.getPiece().getColor() != temp.getPiece().getColor()) ) {
			return true;
		}
		else {
			return false;
		}
		
	}

	private boolean isEmpty(Cell check, boolean includeImpossible) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = ChessBoardController.cells[check.getRow()][check.getCol()];
		//if cell is empty, it is empty for all regular moves (non attacks)
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
	
	@Override
	public List<Cell> getAllMoves(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			boolean isValid = true;
			//traverse till we meet non empty cell or border 
			while(true) {
				for(MoveType move: pattern.getMoves()) {
					temp = temp.copy();
					//if black (top), moving ++
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveDownRight(1);
								break;
							case FORWARD_RIGHT:
								temp.moveDownLeft(1);
								break;
							case BACK_LEFT:
								temp.moveUpRight(1);
								break;
							case BACK_RIGHT:
								temp.moveUpLeft(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveUpLeft(1);
								break;
							case FORWARD_RIGHT:
								temp.moveUpRight(1);
								break;
							case BACK_LEFT:
								temp.moveDownLeft(1);
								break;
							case BACK_RIGHT:
								temp.moveDownRight(1);
								break;
							default:
						}
					}
					
					if(!isEmpty(temp, includeImpossible)) {
						isValid = false;
						break;
					}
					else {
						moves.add(temp);
					}
				}
				if(!isValid) break;
			} 
		}
		return moves;
	}

	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			boolean isValid = true;
			//traverse till we meet non empty cell or border 
			while(true) {
				for(MoveType move: pattern.getMoves()) {
					temp = temp.copy();
					//if black (top), moving ++
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveDownRight(1);
								break;
							case FORWARD_RIGHT:
								temp.moveDownLeft(1);
								break;
							case BACK_LEFT:
								temp.moveUpRight(1);
								break;
							case BACK_RIGHT:
								temp.moveUpLeft(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveUpLeft(1);
								break;
							case FORWARD_RIGHT:
								temp.moveUpRight(1);
								break;
							case BACK_LEFT:
								temp.moveDownLeft(1);
								break;
							case BACK_RIGHT:
								temp.moveDownRight(1);
								break;
							default:
						}
					}
					
					if(isValidForAttack(temp)) {
						isValid = false;
						moves.add(temp);
						break;
					}
					else if(isEmpty(temp, includeImpossible)) {
						continue;
					}
					else {
						isValid = false;
						break;
					}
				}
				if(!isValid) break;
			} 
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
		List<Cell> potenialCaptures = getAllCaptures(p, true); 
		potenialCaptures.addAll(getAllMoves(p, true));
		return potenialCaptures;
	}

	@Override
	public boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell) {
		//List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = myCell.copy();
			boolean toProceed = false;
			//traverse till we meet non empty cell or border 
			while(true) {
				for(MoveType move: pattern.getMoves()) {
					temp = temp.copy();
					//if black (top), moving ++
					if(color == PieceColor.BLACK) {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveDownRight(1);
								break;
							case FORWARD_RIGHT:
								temp.moveDownLeft(1);
								break;
							case BACK_LEFT:
								temp.moveUpRight(1);
								break;
							case BACK_RIGHT:
								temp.moveUpLeft(1);
								break;
							default:
						}
					}
					else {
						switch(move) {
							case FORWARD_LEFT:
								temp.moveUpLeft(1);
								break;
							case FORWARD_RIGHT:
								temp.moveUpRight(1);
								break;
							case BACK_LEFT:
								temp.moveDownLeft(1);
								break;
							case BACK_RIGHT:
								temp.moveDownRight(1);
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
				if(!toProceed) break;
			} 
		}
		
		return false;
	}
	
	private boolean isPotentiallyEmpty(Cell check, Cell newVictomCell, Cell oldVictomCell) {
		if ((check.getCol() < 0 || check.getCol() > 7) || (check.getRow() < 0 || check.getRow() > 7))
			return false;
		
		Cell temp = ChessBoardController.cells[check.getRow()][check.getCol()];
		
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
		
		Cell temp = ChessBoardController.cells[check.getRow()][check.getCol()];
		
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
}
