package game.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import game.ChessBoardController;
import game.entities.Cell;
import game.entities.types.Pattern;
import game.entities.types.PieceColor;
import game.entities.types.PieceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Piece {
	
	protected ChessBoardController controller;
	protected List<Pattern> patterns = new ArrayList<>();
	protected String pieceID = "0";
	protected PieceColor color;
	protected boolean isMoved = false;
	protected int pieceValue = 1;
	protected ImageView imageView;
	
	public Piece(ChessBoardController controller) {
		this.controller = controller;
	}
	
	public void init(PieceColor color, String id, String pieceName) {
		this.color = color;
		setId(id);
		
		if(pieceName == null) return;
			
		String imageUrl  = String.format("resources/images/%s_%s.png", color.name().toLowerCase(), pieceName.toLowerCase());
		
		imageView = new ImageView();
		imageView.setImage(new Image(imageUrl));

		initPatterns();
	}
	
	public abstract List<Cell> getAllMoves(Cell current, boolean includeImpossible);
	
	public abstract List<Cell> getAllCaptures(Cell current, boolean includeImpossible);
	
	public abstract PieceType getType();
	
	public abstract List<Pattern> getPatterns();

	public abstract List<Cell> getAllPotentialCaptures(Cell p);

	public abstract boolean isItEatable(Cell myCell, Cell newVictomCell, Cell oldVictomCell);
	
	public abstract Piece clonePiece(ChessBoardController controller);
	
	public abstract void initPatterns();
	
	public ImageView getImageView() {
		return imageView;
	}
	
	public PieceColor getColor() {
		return color;
	}
	
	public void setId(String i) {
		pieceID = i;
	}

	public String getId() {
		return pieceID;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved() {
		isMoved = true;
	}
	
	public void resetMoved() {
		isMoved = false;
	}
	
	public int getPieceValue() {
		return pieceValue;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return pieceID.equals(((Piece)obj).getId());
	}
}
