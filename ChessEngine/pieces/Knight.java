package pieces;

import java.util.Collections;
import java.util.Set;

import board.Move;
import board.Player;
import board.Board;
import board.Tile;

public class Knight extends Piece {

	int[] possibleKnightMoves = { -17, -15, -10, -6, 6, 10, 15, 17 }; // Possible moves on column 3-6

	// Above variable lists a no-exception knight move scenario, below is each
	// column exception

	int[] firstColumnKnightMoves = { -15, -6, 10, 17 };
	int[] secondColumnKnightMoves = { -17, -15, -6, 10, 15, 17 };
	int[] seventhColumnKnightMoves = { -17, -15, -10, 6, 15, 17 };
	int[] eighthColumnKnightMoves = { -17, -10, 6, 15 };

	private Knight(final int piecePosition, final Player playerColour) {
		super(piecePosition, playerColour);
	}

	@Override
	public Set<Move> calculateLegalMoves(Board Board) {

		Set<Move> legalMoves = Collections.EMPTY_SET; // Consider List instead of set and populate list with best moves
														// first when improving code?
		int candidateCoordinate;

		// do Move.contains

		if (Move.contains(Move.getFirstColumn(), this.piecePosition)) { // Find a better solution to identifying column
			possibleKnightMoves = firstColumnKnightMoves;
		}

		if (Move.contains(Move.getSecondColumn(), this.piecePosition)) {
			possibleKnightMoves = secondColumnKnightMoves;
		}
		if (Move.contains(Move.getSeventhColumn(), this.piecePosition)) {
			possibleKnightMoves = seventhColumnKnightMoves;
		}
		if (Move.contains(Move.getEighthColumn(), this.piecePosition)) {
			possibleKnightMoves = eighthColumnKnightMoves;
		}

		for (int candidateMove : possibleKnightMoves) {
			candidateCoordinate = this.piecePosition + candidateMove;

			if (Move.validDestinationTile(candidateCoordinate)) { // change true to "Valid tile" logic

				Tile candidateTile = Board.getTile(candidateCoordinate);

				if (!candidateTile.tileIsOccupied()) {

					legalMoves.add(new Move()); // Move logic not yet finished
					// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

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
