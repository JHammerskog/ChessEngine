package pieces;

import java.util.Set;

import board.Board;
import board.Move;
import board.Player;

public abstract class Piece {

	protected final int piecePosition;
	protected final Player playerColour;

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

	/*
	 * To do:
	 * 
	 * Piece children have duplicate code that could be resolved by the following
	 * two improvements to the piece class:
	 * 
	 * Implement "Sliding Piece" method for bishop/rook/queen 
	 * Implement "Jumping Piece" method for knight/pawn/king
	 */

}
