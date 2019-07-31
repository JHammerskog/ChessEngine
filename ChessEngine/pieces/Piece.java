package pieces;

import java.util.Set;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Player;

/***
 * @author 1200046h The Piece class and its children define the logic of moving
 *         for each individual piece
 */

public abstract class Piece {

	protected final int piecePosition;
	protected final Player playerColour;
	protected boolean isFirstMove; // Initially set to true, for pawns and castling

	protected Piece(final int piecePosition, final Player playerColour) {
		this.piecePosition = piecePosition;
		this.playerColour = playerColour;
	}

	public Set<Move> calculateLegalMoves(Board board) {
		return null;
	}

	public Player getPieceColour() {
		return this.playerColour;
	}
	
	public int getPiecePosition() {
		return this.piecePosition;
	}

	public int identifyColumn(int currentTileCoordinate) { // Needed for MoveExceptions for all pieces, maybe re-home
															// this method elsewhere?
		return BoardUtility.calculateColumn(currentTileCoordinate);
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
