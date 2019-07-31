package pieces;

import java.util.Collections;
import java.util.Set;

import board.Move;
import board.Player;
import board.Board;
import board.BoardUtility;
import board.Tile;
import board.Move.NonAttackingMove;

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
	public Set<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and inhereted
														// by King/pawn/Knight

		Set<Move> legalMoves = Collections.EMPTY_SET; // Consider List instead of set and populate list with best moves
														// first when improving code?
		int candidateCoordinate;

		if ((identifyColumn(this.piecePosition) == 0)) { // Find a better solution to identifying column, Maybe if 2 > && < 7? else do below
			possibleKnightMoves = firstColumnKnightMoves;
		}

		if (identifyColumn(this.piecePosition) == 1) {
			possibleKnightMoves = secondColumnKnightMoves;
		}

		if (identifyColumn(this.piecePosition) == 6) {
			possibleKnightMoves = seventhColumnKnightMoves;
		}
		if (identifyColumn(this.piecePosition) == 7) {
			possibleKnightMoves = eighthColumnKnightMoves;
		}

		for (int candidateMove : possibleKnightMoves) {
			candidateCoordinate = this.piecePosition + candidateMove;

			if (BoardUtility.validDestinationTile(candidateCoordinate)) {

				Tile candidateTile = board.getTile(candidateCoordinate);

				if (!candidateTile.tileIsOccupied()) {

					legalMoves.add(new NonAttackingMove(board, this, candidateTile)); // Move logic not yet finished
					// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

				} else {

					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Player pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour) {
						legalMoves.add(new Move()); // AttackingMove
					}
				}
			}

		}

		return legalMoves;
	}

}
