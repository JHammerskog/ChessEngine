package pieces;

import java.util.Collections;
import java.util.Set;

import board.Move;
import board.Player;
import board.Board;
import board.Tile;

public class Rook extends Piece {

	public Rook(int piecePosition, Player playerColour) {
		super(piecePosition, playerColour);

	}

	int[] possibleRookMoves = { -8, -1, 1, 8 };

	@Override
	public Set<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and inhereted by Bishop/Queen/Rook

		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;

		for (int candidateVector : possibleRookMoves) {
			candidateCoordinate = this.piecePosition;

			while (Move.validDestinationTile(candidateCoordinate)) {

				if ((identifyColumn(candidateCoordinate) == 0 && (candidateVector == -1))
						|| (identifyColumn(candidateCoordinate) == 7 && (candidateVector == 1))) {
					break;
				}

				candidateCoordinate += candidateVector; // here you alter the candidateCoordinate and then check again,
														// meaning the above exception checking is useless

				if (Move.validDestinationTile(candidateCoordinate)) {
					Tile candidateTile = Board.getTile(candidateCoordinate);

					if (!candidateTile.tileIsOccupied()) { 

						legalMoves.add(new Move());
						//System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

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

	public int identifyColumn(int currentTileCoordinate) {
		return Move.calculateColumn(currentTileCoordinate);
	}
}
