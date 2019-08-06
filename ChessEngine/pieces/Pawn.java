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

public class Pawn extends Piece {

	public Pawn(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.PAWN);

	}

	private int pawnVectors[] = { 7, 8, 9, 16 };

	public List<Move> calculateLegalMoves(Board board) {
		List<Move> legalMoves = new ArrayList<>();
		int candidateCoordinate;

		if (this.getPieceColour() == Alliance.WHITE) { // Not generic, maybe add isWhite/black method to Player enum
			for (int i = 0; i < pawnVectors.length; i++) {
				pawnVectors[i] = -pawnVectors[i]; // maybe find a better way for directionality
			}
		}
		for (int candidateVector : pawnVectors) {

			candidateCoordinate = this.piecePosition + candidateVector;

			if (!BoardUtility.validDestinationTile(candidateCoordinate)) {
				continue;
			}

			Tile candidateTile = board.getTile(candidateCoordinate);

			if (candidateVector == 8 && !candidateTile.tileIsOccupied()) {

				legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
			} else if ((candidateVector == 16 || candidateVector == -16)
					&& this.isFirstMove /* && isBlack && on 7th row || isWhite and on 2nd row */) { // make more generic
				final int tileBeforeCandidate = (candidateCoordinate - candidateVector) + (candidateVector / 2);
				if (!candidateTile.tileIsOccupied() || !board.getTile(tileBeforeCandidate).tileIsOccupied()) {
					// Both squares in front of pawn must be unoccupied for it to be a legal move

					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
				}
			} else if (candidateVector == 7
					&& !(identifyColumn(candidateCoordinate) == 8 && (this.getPieceColour() == Alliance.WHITE))
					|| !(identifyColumn(candidateCoordinate) == 1) && (this.getPieceColour() == Alliance.BLACK)) {
				if (candidateTile.tileIsOccupied()) {
					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour) {
						legalMoves
								.add(new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));
					}

				} else if (candidateVector == 9
						&& !(identifyColumn(candidateCoordinate) == 1 && (this.getPieceColour() == Alliance.WHITE))
						|| !(identifyColumn(candidateCoordinate) == 8) && (this.getPieceColour() == Alliance.BLACK)) {
					if (candidateTile.tileIsOccupied()) {
						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));
						}

					}
				}
			}

		}
		return legalMoves;
	}

	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}

}
