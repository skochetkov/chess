package gui.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import gui.ChessBoardController;
import gui.entities.Cell;
import gui.entities.Piece;
import gui.entities.types.Condition;
import gui.entities.types.MoveType;
import gui.entities.types.PieceColor;
import gui.entities.types.PieceType;
import gui.entities.types.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class King implements Piece{
	public String pieceID = "0";
	private HBox ui;
	private PieceColor color;
	private List<Pattern> patterns = new ArrayList<>();
	
	public King(PieceColor color, String id) {
		this.color = color;
		setId(id);
		init();
	}
	
	@Override
	public void init() {
		ui = new HBox();
		Image image;
		
		if(getColor() == PieceColor.WHITE)
			image = new Image("resources/white_king.png");
		else
			image = new Image("resources/black_king.png");
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		ui.getChildren().add(imageView);
		initPatterns();
	}
	
	private void initPatterns() {
		Pattern p1 = new Pattern(Condition.MOVE, false);
		p1.addMove(MoveType.FORWARD_LEFT);
		patterns.add(p1);
		
		Pattern p2 = new Pattern(Condition.MOVE, false);
		p2.addMove(MoveType.FORWARD_RIGHT);
		patterns.add(p2);
		
		Pattern p3 = new Pattern(Condition.MOVE, false);
		p3.addMove(MoveType.BACK_LEFT);
		patterns.add(p3);
		
		Pattern p4 = new Pattern(Condition.MOVE, false);
		p4.addMove(MoveType.BACK_RIGHT);
		patterns.add(p4);
		
		Pattern p5 = new Pattern(Condition.MOVE, false);
		p5.addMove(MoveType.FORWARD);
		patterns.add(p5);
		
		Pattern p6 = new Pattern(Condition.MOVE, false);
		p6.addMove(MoveType.BACK);
		patterns.add(p6);
		
		Pattern p7 = new Pattern(Condition.MOVE, false);
		p7.addMove(MoveType.LEFT);
		patterns.add(p7);
		
		Pattern p8 = new Pattern(Condition.MOVE, false);
		p8.addMove(MoveType.RIGHT);
		patterns.add(p8);
	}
	
	@Override
	public PieceType getType() {
		return PieceType.KING;
	}
	
	@Override
	public PieceColor getColor() {
		return color;
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
			
			for(MoveType move: pattern.getMoves()) {
					
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
				
				if(!isEmpty(temp, includeImpossible)) {
					break;
				}
				else {
					moves.add(temp);
				}
			} 
		}
		return moves;
	}

	@Override
	public HBox getUI() {
		return ui;
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

	@Override
	public List<Cell> getAllCaptures(Cell current, boolean includeImpossible) {
		List<Cell> moves = new ArrayList<>();
		
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = current.copy();
			
			for(MoveType move: pattern.getMoves()) {
					
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
				
				if(isValidForAttack(temp)) {
					Cell victim = ChessBoardController.cells[temp.getRow()][temp.getCol()];
					//temp = victim.copy();
					moves.add(victim);
					break;
				}
				else if(isEmpty(temp, includeImpossible)) {
					continue;
				}
				else {
					break;
				}
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
		for(Pattern pattern : getPatterns(Condition.MOVE)) {
			Cell temp = myCell.copy();
			
			for(MoveType move: pattern.getMoves()) {
					
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
				
				if(isValidForPotentialAttack(temp, newVictomCell, oldVictomCell)) {
					return true;
				}
				else if(isPotentiallyEmpty(temp, newVictomCell, oldVictomCell)) {
					continue;
				}
				else {
					break;
				}
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
	
	@Override
	public void setId(String i) {
		pieceID = i;
	}

	@Override
	public String getId() {
		return pieceID;
	}
}