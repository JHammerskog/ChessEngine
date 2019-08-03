package pieces;

import java.util.Collections;
import java.util.Set;

import board.Board;
import board.BoardUtility;
import board.Move;

import board.Alliance;
import board.Tile;
import board.Move.*;

public class Pawn extends Piece {

	public Pawn(int piecePosition, Alliance playerColour) {
		super(piecePosition, playerColour);

	}

	private int pawnVectors[] = { 7, 8, 9, 16 };

	public Set<Move> calculateLegalMoves(Board board) {
		Set<Move> legalMoves = Collections.EMPTY_SET;
		int candidateCoordinate;

		if (this.getPieceColour() == Alliance.WHITE) { // Not generic, maybe add isWhite/black method to Player enum
			for (int i : pawnVectors) {
				pawnVectors[i] = -pawnVectors[i]; // White pawns move in the negative opposite of black
				// potentially find a better way to determine direction of pawns
			}
		}
		for (int candidateVector : pawnVectors) {

			candidateCoordinate = this.piecePosition + candidateVector;

			if (!BoardUtility.validDestinationTile(candidateCoordinate)) {
				continue;
			}

			Tile candidateTile = board.getTile(candidateCoordinate);

			if (candidateVector == 8 && !candidateTile.tileIsOccupied()) {

				legalMoves.add(new NonAttackingMove(board, this, candidateTile));
			} else if ((candidateVector == 16 || candidateVector == -16)
					&& this.isFirstMove /* && isBlack && on 7th row || isWhite and on 2nd row */) { // make more generic
				final int tileBeforeCandidate = (candidateCoordinate - candidateVector) + (candidateVector / 2);
				if (!candidateTile.tileIsOccupied() || !board.getTile(tileBeforeCandidate).tileIsOccupied()) {
					// Both squares in front of pawn must be unoccupied for it to be a legal move

					legalMoves.add(new NonAttackingMove(board, this, candidateTile));
				}
			} else if (candidateVector == 7
					&& !(identifyColumn(candidateCoordinate) == 8 && (this.getPieceColour() == Alliance.WHITE))
					|| !(identifyColumn(candidateCoordinate) == 1) && (this.getPieceColour() == Alliance.BLACK)) {
				if (candidateTile.tileIsOccupied()) {
					Piece pieceAtCandidateDestination = candidateTile.getPiece();
					Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

					if (pieceColour != this.playerColour) {
						legalMoves.add(new AttackingMove(board, this, candidateTile, pieceAtCandidateDestination));
					}

				} else if (candidateVector == 9
						&& !(identifyColumn(candidateCoordinate) == 1 && (this.getPieceColour() == Alliance.WHITE))
						|| !(identifyColumn(candidateCoordinate) == 8) && (this.getPieceColour() == Alliance.BLACK)) {
					if (candidateTile.tileIsOccupied()) {
						Piece pieceAtCandidateDestination = candidateTile.getPiece();
						Alliance pieceColour = pieceAtCandidateDestination.getPieceColour();

						if (pieceColour != this.playerColour) {
							legalMoves.add(new AttackingMove(board, this, candidateTile, pieceAtCandidateDestination));
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
