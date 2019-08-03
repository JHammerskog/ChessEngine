package pieces;

import java.util.Collections;
import java.util.Set;

import board.Move;
import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Tile;
import board.Move.*;

public class Rook extends Piece {

	public Rook(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour);

	}

	private int[] possibleRookMoves = { -8, -1, 1, 8 };

	@Override
	public Set<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and inhereted
														// by Bishop/Queen/Rook

		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;

		for (int candidateVector : possibleRookMoves) {
			candidateCoordinate = this.piecePosition;

			while (BoardUtility.validDestinationTile(candidateCoordinate)) {

				if ((identifyColumn(candidateCoordinate) == 0 && (candidateVector == -1))
						|| (identifyColumn(candidateCoordinate) == 7 && (candidateVector == 1))) {
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
						Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(new AttackingMove(board, this, candidateTile, pieceAtCandidateDestination));
							// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);
						}
						break;
					}

				}

			}

		}

		return legalMoves;
	}

	public String toString() {
		return Piece.PieceType.ROOK.toString();
	}

}
