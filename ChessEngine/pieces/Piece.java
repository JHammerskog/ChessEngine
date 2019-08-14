package pieces;

import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;

/***
 * The Piece class and its children define the logic of moving for each
 * individual piece. Also includes
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
	}

	public abstract List<Move> calculateLegalMoves(Board board);

	public Alliance getPieceAlliance() {
		return this.playerColour;
	}

	public int getPiecePosition() {
		return this.piecePosition;
	}

	public int identifyColumn(int currentTileCoordinate) { // Needed for MoveExceptions for all pieces
		return BoardUtility.calculateColumn(currentTileCoordinate);
	}

	public PieceType getPieceType() {
		return this.pieceType;
	}

	public abstract Piece movePiece(Move move);

	public enum PieceType {

		BISHOP("B", 300), KING("K", 50000), KNIGHT("N", 300), PAWN("P", 100), QUEEN("Q", 900), ROOK("R", 500);
		// Find some official weighting for pieceValue rather than using own knowledge

		private String pieceName;
		private int pieceValue; // This value will be used by minimax

		PieceType(String pieceName, int value) {

			this.pieceName = pieceName;
			this.pieceValue = value;
		}

		public String toString() {
			return this.pieceName;
		}

		public int getPieceValue() {
			return this.pieceValue;
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
