package pieces;

import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;

/***
 * @author 1200046h The Piece class and its children define the logic of moving
 *         for each individual piece
 */

public abstract class Piece {

	protected final int piecePosition;
	protected final Alliance playerColour;
	protected boolean isFirstMove; // Initially set to true, for pawns and castling
	protected PieceType pieceType;

	protected Piece(final int piecePosition, final Alliance playerColour, final PieceType pieceType) {
		this.piecePosition = piecePosition;
		this.playerColour = playerColour;
		this.pieceType = pieceType;
		// Pass PieceType in constructor? If you need to keep track of pieces in the
		// future implement this
	}

	public List<Move> calculateLegalMoves(Board board) {
		return null;
	}

	public Alliance getPieceColour() {
		return this.playerColour;
	}

	public int getPiecePosition() {
		return this.piecePosition;
	}

	public int identifyColumn(int currentTileCoordinate) { // Needed for MoveExceptions for all pieces
		return BoardUtility.calculateColumn(currentTileCoordinate);
	}

	public PieceType getPieceType() {
		// TODO Auto-generated method stub
		return this.pieceType;
	}

	public enum PieceType { // Add king logic here later

		BISHOP("B"), KING("K"), KNIGHT("N"), PAWN("P"), QUEEN("Q"), ROOK("R");

		private String pieceName;

		PieceType(String pieceName) {

			this.pieceName = pieceName;
		}

		public String toString() {
			return this.pieceName;
		}

	}

	/*
	 * To do:
	 * 
	 * Piece children have duplicate code that could be resolved by the following
	 * two improvements to the piece class:
	 * 
	 * Implement "Sliding Piece" method for bishop/rook/queen Implement
	 * "Jumping Piece" method for knight/pawn/king
	 */

}
