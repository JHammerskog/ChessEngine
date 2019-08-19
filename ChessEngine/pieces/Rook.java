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

public class Rook extends Piece {

	public Rook(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.ROOK);

	}

	private int[] possibleRookMoves = { -8, -1, 1, 8 };

	@Override
	public List<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and
															// inhereted
															// by Bishop/Queen/Rook

		List<Move> legalMoves = new ArrayList<>();
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

						legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
						// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

						if (pieceColour != this.playerColour) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));
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

	@Override
	public Piece movePiece(Move move) {
		return new Rook(move.getDestinationTileCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	public String toString() {
		return Piece.PieceType.ROOK.toString();
	}

}
