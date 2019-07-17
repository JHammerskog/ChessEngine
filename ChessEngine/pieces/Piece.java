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

	public Set<Move> calculateLegalMoves(Board Board) {
		return null;
	}

	public Player getPieceColour() { // returns the colour of the piece
		return this.playerColour;
	}
}
