package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.AttackingMove;
import board.Move.NonAttackingMove;
import pieces.Piece.PieceType;
import board.Tile;

public class Knight extends Piece {

	private int[] possibleKnightMoves = { -17, -15, -10, -6, 6, 10, 15, 17 }; // Possible moves on column 3-6

	// Above variable lists a no-exception knight move scenario, below is each
	// column exception

	private int[] firstColumnKnightMoves = { -15, -6, 10, 17 };
	private int[] secondColumnKnightMoves = { -17, -15, -6, 10, 15, 17 };
	private int[] seventhColumnKnightMoves = { -17, -15, -10, 6, 15, 17 };
	private int[] eighthColumnKnightMoves = { -17, -10, 6, 15 };

	public Knight(final int piecePosition, final Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.KNIGHT);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and
															// inhereted
															// by King/pawn/Knight

		List<Move> legalMoves = new ArrayList<>(); // Consider List instead of set and populate list with best moves
													// first when improving code?
		int candidateCoordinate;

		if ((identifyColumn(this.piecePosition) == 0)) { // Find a better solution to identifying column, Maybe if 2 >
															// && < 7? else do below
			possibleKnightMoves = firstColumnKnightMoves;
		}

		if (identifyColumn(this.piecePosition) == 1) { // Refactor this if you have time at the end
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

					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate)); // Move logic not yet
					// finished
					// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

				} else {

					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour) {
						legalMoves
								.add(new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination)); // AttackingMove
					}
				}
			}

		}

		return legalMoves;
	}

	public String toString() {
		return Piece.PieceType.KNIGHT.toString();
	}

}
