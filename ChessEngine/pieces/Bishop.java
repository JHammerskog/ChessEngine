package pieces;

import java.util.Collections;
import java.util.Set;

import board.Move;
import board.Player;
import board.Board;
import board.Tile;

public class Bishop extends Piece {

	int[] bishopDiagonals = { -9, -7, 7, 9 };

	// EXCLUSIONS BISHOP: -9 & 7 when on first column, -7 & 9 when on 8th column

	private Bishop(int piecePosition, Player playerColour) {
		super(piecePosition, playerColour);
	}

	@Override
	public Set<Move> calculateLegalMoves(Board Board) {

		Set<Move> legalMoves = Collections.EMPTY_SET; // Consider List instead of set and populate list with best moves

		int candidateCoordinate;

		for (int candidateVector : bishopDiagonals) {
			candidateCoordinate = this.piecePosition;

			while (Move.validDestinationTile(candidateCoordinate)) {

				if (Move.contains(Move.getFirstColumn(), candidateCoordinate)
						&& ((candidateVector == -9) || (candidateVector == 7)) // This Move.contains will be a problem,
																				// find
																				// better solution
						|| Move.contains(Move.getEighthColumn(), candidateCoordinate)
								&& ((candidateVector == -7) || (candidateVector == 9))) {
					break;
				}

				candidateCoordinate += candidateVector;

				if (Move.validDestinationTile(candidateCoordinate)) {
					Tile candidateTile = Board.getTile(candidateCoordinate);

					if (!candidateTile.tileIsOccupied()) {

						legalMoves.add(new Move());
						// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Player pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(new Move());
							// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);
						}
						break;
					}

				}

			}

		}

		return legalMoves;
	}
}
