package pieces;

import java.util.Collections;
import java.util.Set;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Player;
import board.Tile;
import board.Move.NonAttackingMove;

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
			candidateCoordinate = this.piecePosition; 

			while (BoardUtility.validDestinationTile(candidateCoordinate)) { 

				if ((identifyColumn(candidateCoordinate) == 0
						&& (candidateVector == -1 || candidateVector == -9 || candidateVector == 7))
						|| ((identifyColumn(candidateCoordinate) == 7
								&& (candidateVector == 1 || candidateVector == -7 || candidateVector == 9)))) {

					break;
				}

				candidateCoordinate += candidateVector; 

				if (BoardUtility.validDestinationTile(candidateCoordinate)) {
					Tile candidateTile = board.getTile(candidateCoordinate);

					if (!candidateTile.tileIsOccupied()) { 

						legalMoves.add(new NonAttackingMove(board, this, candidateTile));
						//System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Player pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(new Move()); // AttackingMove

						}
						break;
					}

				}

			}

		}

		return legalMoves;
	}

}
