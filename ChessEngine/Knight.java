
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Knight extends Piece {

	int[] possibleKnightMoves = { -17, -15, -10, -6, 6, 10, 15, 17 }; // Possible moves on column 3-6

	Knight(final int piecePosition, final Player playerColour) {
		super(piecePosition, playerColour);
	}

	private int[] calculateCandidateMoves() {
		int[] firstColumnKnightMoves = { -15, -6, 10, 17 };
		int[] secondColumnKnightMoves = { -17, -15, -6, 10, 15, 17 };
		int[] seventhColumnKnightMoves = { -17, -15, -10, 6, 15, 17 };
		int[] eighthColumnKnightMoves = { -17, -10, 6, 15 };
		// These are exceptions for the various columns, don't use this method

		return null;
	}

	@Override
	public Set<Move> calculateLegalMoves(Board Board) {

		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;

		for (int candidateMoves : possibleKnightMoves) {
			candidateCoordinate = this.piecePosition + candidateMoves;
			if (true) { // change true to "Valid tile" logic
				Tile candidateTile = Board.getTile(candidateCoordinate);
				if (!candidateTile.tileIsOccupied()) {
					legalMoves.add(new Move()); // Move logic not yet finished
				} else {
					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Player pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour) {
						legalMoves.add(new Move());
					}
				}
			}

		}

		return legalMoves;
	}

}
