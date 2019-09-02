package ai;

import java.util.List;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;
import pieces.Piece.PieceType;
import pieces.Rook;

public class KRKSolver extends EndgameSolver {

	/***
	 * This class will contain the logic for solving a King-Rook vs King endgame and
	 * inherits from the EndgameSolver class.
	 * 
	 * This is the simplest type of endgame puzzle for a chess game, and the
	 * starting point of this project.
	 * 
	 * This heuristic will ONLY work on a board with ONLY three pieces as described
	 * above. Currently, this King-Rook vs King solver can solve all KRK puzzles
	 * given to it, except for ones described in the "known bugs" section just
	 * below.
	 * 
	 * In this iteration, working on being able to mate the opponents king on the
	 * vertical edges of the board has been de-prioritized in favour of developing
	 * more heuristics. There is however a lot of 'skeleton'-code which has not been
	 * written out due to this de-prioritization. For future optimization, the
	 * relevant code can just be plugged into the functions below as described by
	 * comments.
	 ***/

	/*
	 * Known Bugs/problems:
	 * 
	 * 1. If the playerKing is closer to the matingEdge than the targetKing, the
	 * targetKing can block the playerKing from getting to the desired
	 * restriction-row. This can be solved in two ways: 1) Through improving how the
	 * king gets to the restricting row (by not just giving it a move, but telling
	 * it to go around the restriction), and 2) through improving the
	 * generateBestMatingEdge() function. However, even with a better function,
	 * there still needs to be a "moveAroundrestriction()" function implemented for
	 * the king.
	 * 
	 */

	private final Piece loneRook;

	private final int currentLoneRookRow;
	private final int currentLoneRookColumn;

	private final boolean rookIsSafe;

	public KRKSolver(Board board) {
		super(board);

		this.loneRook = findRook(board.getCurrentPlayer().getActivePieces());

		this.currentLoneRookRow = BoardUtility.calculateRow(this.loneRook.getPiecePosition());
		this.currentLoneRookColumn = BoardUtility.calculateColumn(this.loneRook.getPiecePosition());

		this.rookIsSafe = isRookDefended() || board.getCurrentPlayer()
				.attacksOnTile(board.getOpponent(board.getCurrentPlayer().getAlliance()).getLegalMovesInPosition(),
						this.loneRook.getPiecePosition())
				.isEmpty(); // Rook is safe if it is defended or if the list of opponent attack moves on its
							// tile is empty

	}

	public Move generateRestrictingMove(Board board) {

		if (!isRookAtMaximumRestriction()) {
			return moveRookToMaximumRestriction();
		}

		if (!getRookIsSafe()) {
			return makeRookSafetyMove();
		}

		if (!isKingOnRestrictingRow(getMatingEdge())) {
			return moveKingTowardRestrictingRow();
		}

		if (kingsAreInOpposition()) {
			if (canRookMakeCheckingMove()) {
				return makeRookCheckingMove(getMatingEdge());
			} else {
				return makeRookSafetyMove();
				// You can trap king here and save a lot of moves if you move to
				// playerKingPosition - 9 or -7
			}
		} else if (waitingMoveRequired()) {
			return generateRookWaitingMove();

		} else {
			return makeKingRestrictingMove();
		}

	}

	// ROOK Moving LOGIC

	public Move moveRookToMaximumRestriction() {
		int rookTargetRow;
		if (!getPinAgainstColumn()) {

			if (getMatingEdge() == 0) {
				rookTargetRow = getTargetKingRow() + 1;

				for (Move move : loneRook.calculateLegalMoves(this.board)) { // try to improve this
					if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == rookTargetRow) {

						if (pieceWouldBeSafeNextTurn(move)) {
							return move;
						}

					}
				}
			} else {
				rookTargetRow = getTargetKingRow() - 1;
				for (Move move : loneRook.calculateLegalMoves(this.board)) { // try to improve this
					if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == rookTargetRow) {
						if (pieceWouldBeSafeNextTurn(move)) {
							return move;
						}
					}

				}
			}

		} else if (getPinAgainstColumn()) {
			if (getMatingEdge() == 0) {
				// Move the rook to any tile on getTargetKingColumn() - 1
			} else {
				// Move the rook to any tile on getTargetKingColumn() - 1
			}
		}
		return makeRookSafetyMove();
	}

	public boolean isRookAtMaximumRestriction() {
		if (!getPinAgainstColumn()) {
			if (getMatingEdge() == 0) {
				return getRookRow() - getTargetKingRow() == 1;
			} else {
				return getRookRow() - getTargetKingRow() == -1;
			}

		} else {
			if (getMatingEdge() == 0) {
				return getRookColumn() - getTargetKingRow() == 1;
			} else {
				return getRookColumn() - getTargetKingColumn() == -1;
			}
		}
	}

	public Move makeRookSafetyMove() { // Should work
		Move safetyMove = null;

		if (!getPinAgainstColumn()) {

			for (Move move : this.loneRook.calculateLegalMoves(getBoard())) {
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == getRookRow()) { // same row
					if (BoardUtility.calculateColumn(move.getDestinationTileCoordinate()) == 0
							|| BoardUtility.calculateColumn(move.getDestinationTileCoordinate()) == 7) { // edge of
																											// board
						safetyMove = move;
						if (pieceWouldBeSafeNextTurn(safetyMove)) {
							return safetyMove;
						}
					}

				}
			}

		} else {
			for (Move move : this.loneRook.calculateLegalMoves(getBoard())) {
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == 0
						|| BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == 7) {
					safetyMove = move;
					if (pieceWouldBeSafeNextTurn(safetyMove)) {
						return safetyMove;
					}
				}
			}

		}

		throw new RuntimeException("Should never get here, method needs attention");
	}

	public Move traverseRookMoves(int desiredDestinationCoordinate) {
		for (Move move : loneRook.calculateLegalMoves(this.board)) {
			if (move.getDestinationTileCoordinate() == desiredDestinationCoordinate) {
				return move;

			}
		}
		throw new RuntimeException("No move like that exists! This method needs attention");
	}

	public Move makeRookCheckingMove(int matingEdge) { // should only be called if rook is on edge
		Move checkingMove = null;
		if (!getPinAgainstColumn()) {
			if (getRookRow() > matingEdge) {
				checkingMove = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(),
						this.loneRook.getPiecePosition() - 8);
				if (pieceWouldBeSafeNextTurn(checkingMove)) {
					return checkingMove;
				}
			} else {
				checkingMove = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(),
						this.loneRook.getPiecePosition() + 8);
				if (pieceWouldBeSafeNextTurn(checkingMove)) {
					return checkingMove;
				}
			}
		} else {
			// do same as above, but return -1 or + 1 instead
		}

		throw new RuntimeException("Exception not caught here");
	}

	public Move generateRookWaitingMove() { // Needs testing, expecting bugs
		Move move = null;
		if (!getPinAgainstColumn()) {
			if (!(getRookColumn() == 7)) {
				move = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(),
						this.loneRook.getPiecePosition() + 1);
			} else {
				move = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(),
						this.loneRook.getPiecePosition() - 1);

			}

			if (!pieceWouldBeSafeNextTurn(move)) {
				return makeRookSafetyMove();
			}

			return move;

		} else if (!getPinAgainstColumn()) {
			// same as above but + 8 and - 8
		}

		return null;
	}

	public boolean canRookMakeCheckingMove() {

		if (!getPinAgainstColumn()) {
			if (getMatingEdge() == 0) {
				Move move = traverseRookMoves(this.loneRook.getPiecePosition() - 8);
				Board newBoard = move.executeMoveAndBuildBoard();
				if (newBoard.getCurrentPlayer().attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
						move.getDestinationTileCoordinate()).isEmpty()) {
					return true;
				}

			} else if (getMatingEdge() == 7) {
				Move move = traverseRookMoves(this.loneRook.getPiecePosition() + 8);
				Board newBoard = move.executeMoveAndBuildBoard();
				if (newBoard.getCurrentPlayer().attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
						move.getDestinationTileCoordinate()).isEmpty()) {
					return true;
				}
			}

		}
		return false;
	}

	// KING LOGIC This could also be moved to the endgame solver?

	public Move makeKingRestrictingMove() { // Exception handling needed
		Move kingRestrictingMove = null;

		if (!getPinAgainstColumn()) {
			if (getPlayerKingColumn() > getTargetKingColumn()) {
				kingRestrictingMove = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
						getPlayerKing().getPiecePosition() - 1);
				if (pieceWouldBeSafeNextTurn(kingRestrictingMove)) {
					return kingRestrictingMove;
				}
			} else {
				kingRestrictingMove = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
						getPlayerKing().getPiecePosition() + 1);
				if (pieceWouldBeSafeNextTurn(kingRestrictingMove)) {
					return kingRestrictingMove;
				}
			}
		} else {
			// do same as above, but return -8 or + 8 instead
		}

		return null;
	}

	public Move moveKingTowardRestrictingRow() { // Breaks down when piece is in the way or king moves into check
		Move moveTowardRestriction = null;

		int targetKingRowOrColumn = -1;
		int direction = 1337;
		if (getMatingEdge() == 0) {
			targetKingRowOrColumn = getTargetKingRow() + 2;

		} else if (getMatingEdge() == 7) {
			targetKingRowOrColumn = getTargetKingRow() - 2;

		}

		if (!getPinAgainstColumn()) {
			if (getPlayerKingRow() > targetKingRowOrColumn) {
				direction = -8;

			} else {
				direction = 8;
			}

		} else if (getPinAgainstColumn()) {

			if (getPlayerKingRow() > targetKingRowOrColumn) {
				direction = -1;

			} else {
				direction = 1;
			}
		}

		moveTowardRestriction = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
				getPlayerKing().getPiecePosition() + direction);
		if (pieceWouldBeSafeNextTurn(moveTowardRestriction)) {
			return moveTowardRestriction;
		}

		throw new RuntimeException("NEEDS ATTENTION");
	}

	// Helper functions

	public boolean waitingMoveRequired() { // if it is whites move and this returns true, a rook move must be made

		int distanceBetweenKings = getTargetKing().getPiecePosition() - this.getPlayerKing().getPiecePosition();

		if (getPinAgainstColumn()) {
			if (distanceBetweenKings == -10 || distanceBetweenKings == -6 || distanceBetweenKings == 6
					|| distanceBetweenKings == 10) {
				return true;
			}

		} else if (!getPinAgainstColumn()) {
			if (distanceBetweenKings == -15 || distanceBetweenKings == -17 || distanceBetweenKings == 15
					|| distanceBetweenKings == 17) {
				return true;
			}

		}
		return false;
	}

	public boolean kingsAreInOpposition() { // Works and needed

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		int distanceBetweenKings = getTargetKing().getPiecePosition() - playerKingPosition;

		if (getPinAgainstColumn()) { // If pin is against column
			if (distanceBetweenKings == -2 || distanceBetweenKings == 2) {
				return true;
			}

		} else if (!getPinAgainstColumn()) { // If pin is against Row
			if (distanceBetweenKings == -16 || distanceBetweenKings == 16) {
				return true;
			}

		}
		return false;
	}

	public Rook findRook(List<Piece> activePieces) { // is only used to set the class variable "loneRook"
		for (Piece piece : activePieces) {
			if (piece.getPieceType() == PieceType.ROOK) {
				return (Rook) piece;
			}

		}
		throw new RuntimeException("This heuristic only works for a King-Rook vs. King endgame");
	}

	// getters & setter

	public boolean getRookIsSafe() {
		return rookIsSafe;
	}

	public int getRookRow() {
		return this.currentLoneRookRow;
	}

	public int getRookColumn() {
		return this.currentLoneRookColumn;
	}

	public boolean isRookDefended() {
		return BoardUtility.isPieceDefended(this.loneRook, this.board);
	}

}
