package pieces;

import java.util.Collections;
import java.util.Set;


import board.Player;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Tile;

public class Bishop extends Piece {

	int[] bishopDiagonals = { -9, -7, 7, 9 };

	// EXCLUSIONS BISHOP: -9 & 7 when on first column, -7 & 9 when on 8th column

	private Bishop(int piecePosition, Player playerColour) {
		super(piecePosition, playerColour);
	}

	@Override
	public Set<Move> calculateLegalMoves(Board board) {// the bulk of this function could be put in Piece and inhereted by Bishop/Queen/Rook


		Set<Move> legalMoves = Collections.EMPTY_SET; // Consider List instead of set and populate list with best moves

		int candidateCoordinate;

		for (int candidateVector : bishopDiagonals) {
			candidateCoordinate = this.piecePosition;

			while (BoardUtility.validDestinationTile(candidateCoordinate)) {

				if (identifyColumn(this.piecePosition) == 0 && ((candidateVector == -9) || (candidateVector == 7))
						|| identifyColumn(this.piecePosition) == 7
								&& ((candidateVector == -7) || (candidateVector == 9))) {
					break;
				}

				candidateCoordinate += candidateVector;

				if (BoardUtility.validDestinationTile(candidateCoordinate)) {
					Tile candidateTile = board.getTile(candidateCoordinate);

					if (!candidateTile.tileIsOccupied()) {

						legalMoves.add(new NonAttackingMove(board, this, candidateTile));
						// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Player pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(new Move()); // AttackingMove
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
