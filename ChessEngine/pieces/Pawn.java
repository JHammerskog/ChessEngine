package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.AttackingMove;
import board.Move.NonAttackingMove;
import board.Move.PawnPromotionMove;
import board.Tile;

public class Pawn extends Piece {

	public Pawn(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour, PieceType.PAWN);

	}

	private int pawnVectors[] = { 7, 8, 9, 16 };

	public List<Move> calculateLegalMoves(Board board) {
		List<Move> legalMoves = new ArrayList<>();
		int candidateCoordinate;

		for (int candidateVector : pawnVectors) {

			if (this.getPieceAlliance() == Alliance.WHITE && candidateVector > 0) {
				candidateVector = -candidateVector;
				// solution to a bug you couldn't figure out
			}

			candidateCoordinate = this.piecePosition + candidateVector;

			if (!BoardUtility.validDestinationTile(candidateCoordinate)) {
				continue;
			}

			Tile candidateTile = board.getTile(candidateCoordinate);

			if ((candidateVector == 8 || candidateVector == -8) && !candidateTile.tileIsOccupied()) {
				if (!(this.getPieceAlliance().isPromotionRow(candidateCoordinate))) {
					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
				} else {
					legalMoves.add(new PawnPromotionMove(board, this, candidateCoordinate, null));

				}

			} else if ((candidateVector == 16 || candidateVector == -16)
					&& this.isFirstMove /* && isBlack && on 7th row || isWhite and on 2nd row */) { // make more generic
				final int tileBeforeCandidate = (candidateCoordinate - candidateVector) + (candidateVector / 2);
				if (!candidateTile.tileIsOccupied() || !board.getTile(tileBeforeCandidate).tileIsOccupied()) {
					// Both squares in front of pawn must be unoccupied for it to be a legal move

					legalMoves.add(new NonAttackingMove(board, this, candidateCoordinate));
				}
			} else if ((candidateVector == 7 || candidateVector == -7)
					&& (!(identifyColumn(this.piecePosition) == 7 && (this.getPieceAlliance() == Alliance.WHITE))
							|| !(identifyColumn(this.piecePosition) == 0)
									&& (this.getPieceAlliance() == Alliance.BLACK))) {
				if (candidateTile.tileIsOccupied()) {
					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

					if (pieceColour != this.playerColour) {
						if (!(this.getPieceAlliance().isPromotionRow(candidateCoordinate))) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));
						} else {
							legalMoves.add(new PawnPromotionMove(board, this, candidateCoordinate,
									pieceAtCandidateDestination));
						}

					} else {
						defendedPieces.add(pieceAtCandidateDestination);
					}

				}
			} else if ((candidateVector == 9 || candidateVector == -9)
					&& !(identifyColumn(this.piecePosition) == 0 && (this.getPieceAlliance() == Alliance.WHITE))
					|| !(identifyColumn(this.piecePosition) == 7) && (this.getPieceAlliance() == Alliance.BLACK)) {
				if (candidateTile.tileIsOccupied()) {
					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceAlliance();

					if (pieceColour != this.playerColour) {
						if (!(this.getPieceAlliance().isPromotionRow(candidateCoordinate))) {
							legalMoves.add(
									new AttackingMove(board, this, candidateCoordinate, pieceAtCandidateDestination));
						} else {
							legalMoves.add(new PawnPromotionMove(board, this, candidateCoordinate,
									pieceAtCandidateDestination));
						}
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
		return new Pawn(move.getDestinationTileCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}

}
