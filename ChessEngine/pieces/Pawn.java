package pieces;

import java.util.Collections;
import java.util.Set;

import board.Board;
import board.Move;
import board.Player;

public class Pawn extends Piece {

	protected Pawn(int piecePosition, Player playerColour) {
		super(piecePosition, playerColour);
		
	}
	
	public Set<Move> calculateLegalMoves(Board board) {
		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;
		
		return legalMoves;
	}

}
