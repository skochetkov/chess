package game;

import game.entities.Cell;
import game.entities.pieces.Bishop;
import game.entities.pieces.King;
import game.entities.pieces.Knight;
import game.entities.pieces.Pawn;
import game.entities.pieces.Queen;
import game.entities.pieces.Rook;
import game.entities.types.PieceColor;
import tests.TestCases;

public class TestController {
	
	public static void getTestCase(ChessBoardController controller, TestCases testCase, Cell[][] cells) {
		if(testCase == TestCases.CASTLING_BLACK) {
			initTestCastlingBlack(controller, cells);
		}
		else if(testCase == TestCases.CASTLING_WHITE) {
			initTestCastlingWhite(controller, cells);
		}
		else if(testCase == TestCases.PAWN_ADVANCES) {
			initTestPawnAdnvances(controller, cells);
		}
		else if(testCase == TestCases.PAWN_ADVANCES_BLACK) {
			initTestPawnAdnvancesBlack(controller, cells);
		}
		else if(testCase == TestCases.EN_PASSANT_BLACK) {
			initTestEnPassantBlack(controller, cells);
		}
		else if(testCase == TestCases.EN_PASSANT_WHITE) {
			initTestEnPassantWhite(controller, cells);
		}
		else if(testCase == TestCases.WEIGHT_BLACK) {
			initTestWeightBlack(controller, cells);
		}
	}
	/**
	 * Just function for testing various scenarios
	 */
	 static void initTestCastlingBlack(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[0][4].setPiece(new King(controller, PieceColor.BLACK, "21"));
		//cells[1][4].setPiece(new Knight(PieceColor.BLACK));
		cells[0][7].setPiece(new Rook(controller, PieceColor.BLACK, "22"));
		cells[0][0].setPiece(new Rook(controller, PieceColor.BLACK, "23"));
		
		cells[7][0].setPiece(new Rook(controller, PieceColor.WHITE, "3"));
		cells[7][1].setPiece(new Knight(controller, PieceColor.WHITE, "4"));
		cells[7][2].setPiece(new Bishop(controller, PieceColor.WHITE, "5"));
		cells[7][3].setPiece(new Queen(controller, PieceColor.WHITE, "6"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "7"));
		cells[7][5].setPiece(new Bishop(controller, PieceColor.WHITE, "8"));
		cells[7][6].setPiece(new Knight(controller, PieceColor.WHITE, "9"));
		cells[7][7].setPiece(new Rook(controller, PieceColor.WHITE, "10"));
	}
	
	static void initTestCastlingWhite(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[0][3].setPiece(new King(controller, PieceColor.BLACK, "21"));
		//cells[0][4].setPiece(new Rook(controller, PieceColor.BLACK, "22"));
		cells[0][2].setPiece(new Rook(controller, PieceColor.BLACK, "23"));
		
		cells[7][0].setPiece(new Rook(controller, PieceColor.WHITE, "3"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "7"));
		cells[7][7].setPiece(new Rook(controller, PieceColor.WHITE, "10"));
	}
	
	static void initTestPawnAdnvances(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[0][4].setPiece(new King(controller, PieceColor.BLACK, "21"));
		
		cells[7][0].setPiece(new Rook(controller, PieceColor.WHITE, "3"));
		cells[2][0].setPiece(new Pawn(controller, PieceColor.WHITE, "4"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "7"));
		cells[7][7].setPiece(new Rook(controller, PieceColor.WHITE, "10"));
		cells[2][6].setPiece(new Pawn(controller, PieceColor.WHITE, "11"));
		cells[2][7].setPiece(new Pawn(controller, PieceColor.WHITE, "14"));
	}
	
	static void initTestPawnAdnvancesBlack(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "21"));
		
		//cells[0][0].setPiece(new Rook(controller, PieceColor.BLACK, "3"));
		cells[6][0].setPiece(new Pawn(controller, PieceColor.BLACK, "4"));
		cells[0][4].setPiece(new King(controller, PieceColor.BLACK, "7"));
		//cells[0][7].setPiece(new Rook(controller, PieceColor.BLACK, "10"));
		cells[6][6].setPiece(new Pawn(controller, PieceColor.BLACK, "11"));
		cells[6][7].setPiece(new Pawn(controller, PieceColor.BLACK, "14"));
	}
	
	static void initTestEnPassantBlack(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[6][5].setPiece(new Pawn(controller, PieceColor.WHITE, "21"));
		cells[6][7].setPiece(new Pawn(controller, PieceColor.WHITE, "22"));
		cells[6][3].setPiece(new Pawn(controller, PieceColor.WHITE, "32"));
		cells[6][1].setPiece(new Pawn(controller, PieceColor.WHITE, "23"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "24"));
		cells[1][2].setPiece(new Queen(controller, PieceColor.WHITE, "6"));
		
		cells[4][6].setPiece(new Pawn(controller, PieceColor.BLACK, "11"));
		cells[4][4].setPiece(new Pawn(controller, PieceColor.BLACK, "12"));
		cells[4][2].setPiece(new Pawn(controller, PieceColor.BLACK, "13"));
		cells[4][0].setPiece(new Pawn(controller, PieceColor.BLACK, "14"));
		cells[0][0].setPiece(new King(controller, PieceColor.BLACK, "7"));
		
	}
	
	static void initTestEnPassantWhite(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[4][5].setPiece(new Pawn(controller, PieceColor.WHITE, "21"));
		cells[4][7].setPiece(new Pawn(controller, PieceColor.WHITE, "22"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "24"));
		cells[1][2].setPiece(new Queen(controller, PieceColor.WHITE, "6"));
		
		cells[1][6].setPiece(new Pawn(controller, PieceColor.BLACK, "11"));
		cells[1][4].setPiece(new Pawn(controller, PieceColor.BLACK, "10"));
		cells[0][0].setPiece(new King(controller, PieceColor.BLACK, "7"));
		
	}
	
	static void initTestWeightBlack(ChessBoardController controller, Cell[][] cells) {
		//Init white pieces
		
		cells[3][4].setPiece(new Pawn(controller, PieceColor.WHITE, "21"));
		cells[3][7].setPiece(new Pawn(controller, PieceColor.WHITE, "22"));
		cells[7][4].setPiece(new King(controller, PieceColor.WHITE, "24"));
		cells[1][2].setPiece(new Queen(controller, PieceColor.WHITE, "6"));
		
		cells[1][6].setPiece(new Queen(controller, PieceColor.BLACK, "11"));
		cells[1][4].setPiece(new Pawn(controller, PieceColor.BLACK, "10"));
		cells[1][3].setPiece(new Knight(controller, PieceColor.BLACK, "9"));
		cells[0][0].setPiece(new King(controller, PieceColor.BLACK, "7"));
		
	}
}
