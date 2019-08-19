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

public class Bishop extends Piece {

	private int[] bishopDiagonals = { -9, -7, 7, 9 };

	public Bishop(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.BISHOP);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {// the bulk of this function could be put in Piece and inhereted
														// by Bishop/Queen/Rook

		List<Move> legalMoves = new ArrayList<>();

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

						legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
						// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

						if (pieceColour != this.playerColour) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination)); // AttackingMove
							// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);
						} else {
							defendedPieces.add(pieceAtCandidateDestination);
						}
						break;
					}

				}

			}

		}

		return legalMoves;
	}

	public String toString() {
		return Piece.PieceType.BISHOP.toString();
	}

	@Override
	public Piece movePiece(Move move) {
		return new Bishop(move.getDestinationTileCoordinate(), move.getMovedPiece().getPieceAlliance() );
	}

}
