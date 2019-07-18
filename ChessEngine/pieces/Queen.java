package pieces;

import java.util.Collections;
import java.util.Set;

import board.Board;
import board.Move;
import board.Player;
import board.Tile;

public class Queen extends Piece {

	private Queen(int piecePosition, Player playerColour) { // CHANGE BACK CONSTRUCTOR TO PRIVATE
		super(piecePosition, playerColour);

	}

	int[] possibleQueenMoves = { -9, -8, -7, -1, 1, 7, 8, 9 };

	@Override
	public Set<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and inhereted by Bishop/Queen/Rook

		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;

		for (int candidateVector : possibleQueenMoves) {
			candidateCoordinate = this.piecePosition; // This is the root of your problems

			while (Move.validDestinationTile(candidateCoordinate)) { // This while loop keeps going

				if ((identifyColumn(candidateCoordinate) == 0
						&& (candidateVector == -1 || candidateVector == -9 || candidateVector == 7))
						|| ((identifyColumn(candidateCoordinate) == 7
								&& (candidateVector == 1 || candidateVector == -7 || candidateVector == 9)))) {

					break;
				}

				candidateCoordinate += candidateVector; // This here means you will go around the loop again and again

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
