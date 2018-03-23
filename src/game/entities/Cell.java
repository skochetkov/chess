package game.entities;

import game.entities.pieces.Dummy;
import game.entities.pieces.Piece;
import game.entities.types.CellType;
import game.entities.types.PieceColor;
import game.entities.types.Requests;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.ChessGameManager;

public class Cell implements Parameter{
	private boolean isBlack;
	private boolean isSelected;
	private boolean isOptionSelected;
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
		setDummyPiece(new Dummy(null, color, null));
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
		cell.getChildren().add(figure.getImageView());
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
		cell.getChildren().add(fig.getImageView());
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
	
	public void setOptionSelected(boolean isOptionSelected) {
		this.isOptionSelected = isOptionSelected;
		
		if(isOptionSelected) {
			cell.setBorder(new Border(new BorderStroke(Color.RED.darker(), BorderStrokeStyle.DOTTED,
                    null,new BorderWidths(3))));
			InnerShadow is = new InnerShadow();
	        is.setOffsetX(2.0f);
	        is.setOffsetY(2.0f);
			//cell.setEffect(is);
	        cell.setEffect(new Glow(0.5));
			
		}
		else
		{
			cell.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID,
                    null,new BorderWidths(0))));
			InnerShadow is = new InnerShadow();
	        is.setOffsetX(.0f);
	        is.setOffsetY(.0f);
			//cell.setEffect(is);
	        cell.setEffect(new Glow(0));
		}
	}
	
	public void setHighlightedScore(int attack, int defend) {
		Text t = new Text();
		//Label t = new Label();
		//t.setX(20.0f);
		//t.setY(65.0f);
		t.setText(attack + "/" + defend);
		t.setFill(Color.RED);
		t.setFont(Font.font(null, FontWeight.BOLD, 14));
		//t.setAlignment(Pos.TOP_RIGHT);
		
		Node n = cell.getChildren().get(0);
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(n, t);
		
		cell.getChildren().add(stackPane);
	}
	
	public void clearHighlightedScore() {
		if(cell.getChildren().size() > 0 && (cell.getChildren().get(0) instanceof StackPane))
			;
		else
			return;
		
		Node n = ((StackPane)cell.getChildren().get(0)).getChildren().get(0);
		
		cell.getChildren().clear();
		cell.getChildren().add(n);
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean isOptionSelected() {
		return isOptionSelected;
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
	
	@Override
	public String toString() {
		return getNotation();
	}

}
