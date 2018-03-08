package model;

import api.ChessGameConnect;
import game.ChessBoardController;
import game.entities.types.PieceColor;

public class User {
	private ChessGameConnect api;
	private PieceColor color;
	
	public User(ChessGameConnect api, PieceColor color) {
		this.api = api;
		this.color = color;
	}
	
	public void initUser() {
		
	}
	
	public PieceColor getColor() {
		return color;
	}
}
