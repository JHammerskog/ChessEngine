package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.AttackingMove;
import board.Move.NonAttackingMove;
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

				if ((identifyColumn(this.piecePosition) == 0
						&& (candidateMove == -1 || candidateMove == -9 || candidateMove == 7))
						|| ((identifyColumn(this.piecePosition) == 7
								&& (candidateMove == 1 || candidateMove == -7 || candidateMove == 9)))) {

					continue;
				}

				Tile candidateTile = board.getTile(candidateCoordinate);

				if (!candidateTile.tileIsOccupied() /* && tile is not attacked */) {

					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));

				} else {

					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

					if (pieceColour != this.playerColour /* && piece is not defended */) {
						legalMoves
								.add(new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination)); // AttackingMove
					} else {
						defendedPieces.add(pieceAtCandidateDestination);
					}
				}
			}

		}

		return legalMoves;
	}

	@Override
	public Piece movePiece(Move move) {
		return new King(move.getDestinationTileCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	public String toString() {
		return Piece.PieceType.KING.toString();
	}

	@Override
	public String toUnicode() {
		String unicode;
		if(this.getPieceAlliance() == Alliance.BLACK) {
			unicode = "\u265A";
		} else {
			unicode = "\u2654";
		}
		return unicode;
	}

}
