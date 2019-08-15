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

public class Queen extends Piece {

	public Queen(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.QUEEN);

	}

	private int[] possibleQueenMoves = { -9, -8, -7, -1, 1, 7, 8, 9 };

	@Override
	public List<Move> calculateLegalMoves(Board board) { // the bulk of this function could be put in Piece and
															// inhereted
															// by Bishop/Queen/Rook

		List<Move> legalMoves = new ArrayList<>();
		int candidateCoordinate;

		for (int candidateVector : possibleQueenMoves) {
			candidateCoordinate = this.piecePosition;

			while (BoardUtility.validDestinationTile(candidateCoordinate)) {

				if ((identifyColumn(this.piecePosition) == 0
						&& (candidateVector == -1 || candidateVector == -9 || candidateVector == 7))
						|| ((identifyColumn(this.piecePosition) == 7
								&& (candidateVector == 1 || candidateVector == -7 || candidateVector == 9)))) {

					break;
				}

				candidateCoordinate += candidateVector;

				if (BoardUtility.validDestinationTile(candidateCoordinate)) {
					Tile candidateTile = board.getTile(candidateCoordinate);

					if (!candidateTile.tileIsOccupied()) {

						legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate)); // Figure out why no
																								// error
						// with lack of static call
						// System.out.println("NEW LEGAL MOVE FOR TESTING: " + candidateCoordinate);

					} else {

						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

						if (pieceColour != this.playerColour) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));

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
		return new Queen(move.getDestinationTileCoordinate(), move.getMovedPiece().getPieceAlliance() );
	}

	public String toString() {
		return Piece.PieceType.QUEEN.toString();
	}

}
