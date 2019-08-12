package pieces;

import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;

/***
 * The Piece class and its children define the logic of moving for each
 * individual piece.
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
