package gui.entities;

import gui.entities.pieces.Dummy;
import gui.entities.types.CellType;
import gui.entities.types.PieceColor;
import gui.entities.types.Requests;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.ChessGameManager;

public class Cell implements Parameter{
	private boolean isBlack;
	private boolean isSelected;
	private CellType type = CellType.NONE;
	private Piece piece;
	private ChessGameManager manager;
	private HBox cell;
	private int col, row;
	
	public Cell(boolean isBlack, ChessGameManager manager, int col, int row) {
		this.isBlack = isBlack;
		this.manager = manager;
		this.col = col;
		this.row = row;
		
		cell = new HBox();
		
		if(isBlack)
			cell.setStyle("-fx-background-color: #7A645F;");
		else
			cell.setStyle("-fx-background-color: #F9F4EB;");
		
		cell.setPrefSize(50, 50);
		initEvent();
	}
	
	public Cell(boolean isBlack, int col, int row, PieceColor color) {
		this.isBlack = isBlack;
		this.col = col;
		this.row = row;
		//set dummy figure color
		setDummyPiece(new Dummy(color, null));
		cell = new HBox();
		if(isBlack)
			cell.setStyle("-fx-background-color: #7A645F;");
		else
			cell.setStyle("-fx-background-color: #F9F4EB;");
		
		cell.setPrefSize(50, 50);
	}
	
	public Cell(int col, int row) {
		this.col = col;
		this.row = row;
	}
	
	public ChessGameManager getCellManager() {
		return manager;
	}
	
	public boolean getIsBlack() {
		return isBlack;
	}
	
	public void setPiece(Piece figure) {
		cell.getChildren().clear();
		cell.getChildren().add(figure.getUI());
		this.piece = figure;
		
	}
	
	public void setDummyPiece(Piece figure) {
		this.piece = figure;
	}
	
	public void resetPiece() {
		//cell.getChildren().remove(0);
		cell.getChildren().clear();
		piece = null;
	}
	
	public void replacePiece(Piece fig) {
		cell.getChildren().clear();
		cell.getChildren().add(fig.getUI());
		this.piece = fig;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	private void initEvent() {
		cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override public void handle(MouseEvent e) {
		    		//if nothing selected, we assume that the user want to select figure which he wants to move
		    		if(manager.getSelectedCell() == null) {
		    			manager.whatToDoNext(Requests.I_WAS_SELECTED_BY_USER, Cell.this);
		    		}
		    		else {
		    			manager.whatToDoNext(Requests.I_WAS_PROPOSED_BY_USER, Cell.this);
		    		}
		    }
		});
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		
		if(isSelected) {
			cell.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,
                    null,new BorderWidths(2))));
			
		}
		else
		{
			cell.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID,
                    null,new BorderWidths(0))));
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean isEmpty() {
		return (piece == null) ? true : false;
	}

	public Node getCell() {
		return cell;
	}
	
	public void move(int xStep, int yStep) {
		row += yStep;
		col += xStep;
	}

	public boolean moveDown(int i) {
		//check if it is not empty or it's a border
		//if() return false;
		row += i;
		return true;
	}
	
	public boolean moveUp(int i) {
		row -= i;
		return true;
	}
	
	public boolean moveLeft(int i) {
		col -= i;
		return true;
	}
	
	public void moveDownLeft(int i) {
		moveDown(i);
		moveLeft(i);
	}

	public void moveDownRight(int i) {
		moveDown(i);
		moveRight(i);
	}

	public void moveUpRight(int i) {
		moveUp(i);
		moveRight(i);
	}

	public void moveUpLeft(int i) {
		moveUp(i);
		moveLeft(i);
	}
	
	public boolean moveRight(int i) {
		col += i;
		return true;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	@Override
	public boolean equals(Object other){
		if(getCol() == ((Cell)other).getCol() && getRow() == ((Cell)other).getRow()) {
			return true;
		}
		return false;
	}

	public Cell copy() {
		return new Cell(isBlack, getCol(), getRow(), getPiece().getColor());
	}

	public String getNotation() {
		
		return ((char)(64+getCol()+1)+"") + "" + (getRow()+1) ;
	}

	public CellType getType() {
		return type;
	}

	public void setType(CellType type) {
		this.type = type;
	}


}
