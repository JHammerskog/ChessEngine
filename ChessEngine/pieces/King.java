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

public class King extends Piece {

	public King(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.KING);

	}

	private int[] possibleKingMoves = { -9, -8, -7, -1, 1, 7, 8, 9 };

	@Override
	public List<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and
															// inhereted
															// by King/pawn/Knight

		List<Move> legalMoves = new ArrayList<>();
		int candidateCoordinate;

		for (int candidateMove : possibleKingMoves) {
			candidateCoordinate = this.piecePosition + candidateMove;

			if (BoardUtility.validDestinationTile(candidateCoordinate)) {

				Tile candidateTile = board.getTile(candidateCoordinate);

				if (!candidateTile.tileIsOccupied() /* && tile is not attacked */) {

					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate)); // Move logic not yet
					// finished
					// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

				} else {

					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour /* && piece is not defended */) {
						legalMoves
								.add(new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination)); // AttackingMove
					}
				}
			}

		}

		return legalMoves;
	}

	public String toString() {
		return Piece.PieceType.KING.toString();
	}

}
