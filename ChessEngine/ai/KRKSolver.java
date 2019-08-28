package ai;

import java.util.List;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;
import pieces.Piece.PieceType;
import pieces.Rook;

public class KRKSolver {

	/***
	 * This class will contain the logic for solving a King-Rook vs King endgame.
	 * 
	 * This is the simplest type of endgame puzzle for a chess game, and the
	 * starting point of this project.
	 * 
	 * This heuristic will ONLY work on a board with ONLY three pieces as described
	 * above. Currently, this King-Rook vs King solver can solve all KRK puzzles
	 * given to it, except for ones described in the "known bugs" section just
	 * below.
	 * 
	 ***/

	/*
	 * Known Bugs/problems:
	 * 
	 * 1. GenerateBestMatingEdge only generates 1st and 8th row checkmates. For an
	 * ideal solver, checkmating needs to happen on every side of the board.
	 * Currently, logic is only implemented to checkmate on the top/bottom edges of
	 * the board.
	 * 
	 * 2. If the playerKing is closer to the matingEdge than the targetKing, the
	 * targetKing can block the playerKing from getting to the desired
	 * restriction-row. This can be solved in two ways: 1) Through improving how the
	 * king gets to the restricting row (by not just giving it a move, but telling
	 * it to go around the restriction), and 2) through improving the
	 * generateBestMatingEdge() function. However, even with a better function,
	 * there still needs to be a "moveAroundrestriction()" function implemented for
	 * the king.
	 * 
	 */

	private boolean pinAgainstColumn; // if this is false, it means the pin is against a row (tile 0-7 and 56-63), if
	// its true, then A or H file

	private int matingEdge;

	private boolean rookIsSafe;

	private int directionToMatingCoordinates; // Informs king what direction it needs to move for the desired row/column

	private final Board board;

	private final Piece targetKing;
	private final Piece loneRook;
	private final Piece playerKing;

	private int currentTargetKingRow;
	private int currentTargetKingColumn;

	private int currentPlayerKingRow;
	private int currentPlayerKingColumn;

	private int currentLoneRookRow;
	private int currentLoneRookColumn;

	public KRKSolver(Board board) {
		this.board = board;

		this.loneRook = findRook(board.getCurrentPlayer().getActivePieces());
		this.targetKing = board.getOpponent(board.getCurrentPlayer().getAlliance()).getPlayerKing();
		this.playerKing = board.getCurrentPlayer().getPlayerKing();

		this.currentTargetKingColumn = BoardUtility.calculateColumn(this.targetKing.getPiecePosition());
		this.currentTargetKingRow = BoardUtility.calculateRow(this.targetKing.getPiecePosition());

		this.currentPlayerKingRow = BoardUtility.calculateRow(this.playerKing.getPiecePosition());
		this.currentPlayerKingColumn = BoardUtility.calculateColumn(this.playerKing.getPiecePosition());

		this.currentLoneRookRow = BoardUtility.calculateRow(this.loneRook.getPiecePosition());
		this.currentLoneRookColumn = BoardUtility.calculateColumn(this.loneRook.getPiecePosition());

		this.rookIsSafe = isRookDefended() || board.getCurrentPlayer()
				.attacksOnTile(board.getOpponent(board.getCurrentPlayer().getAlliance()).getLegalMovesInPosition(),
						this.loneRook.getPiecePosition())
				.isEmpty(); // Rook is safe if it is defended or if the list of opponent attack moves on its
							// tile is empty

		this.matingEdge = generateBestMatingEdge();

		// refactoring generateBestMatingEdge() is the key to fully solving this puzzle

	}

	public Move generateRestrictingMove(Board board) {

		if (!isRookAtMaximumRestriction()) {
			return moveRookToMaximumRestriction();
		}
//		else if (!getRookIsSafe() && (getRookColumn() == 0 || getRookColumn() == 7)) {
//			// NEEDS REFACTORING
//			return moveRookAdjacentToMatingEdge(); // This will break everything up if waitingMove
//													// is required
//		}

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

	// KING LOGIC

	public Move makeKingRestrictingMove() { // Exception handling needed
		Move kingRestrictingMove = null;

		if (!getPinAgainstColumn()) {
			if (getPlayerKingColumn() > getPlayerKingColumn()) {
				kingRestrictingMove = MoveMaker.getMove(this.board, playerKing.getPiecePosition(),
						playerKing.getPiecePosition() - 1);
				if (pieceWouldBeSafeNextTurn(kingRestrictingMove)) {
					return kingRestrictingMove;
				}
			} else {
				kingRestrictingMove = MoveMaker.getMove(this.board, playerKing.getPiecePosition(),
						playerKing.getPiecePosition() + 1);
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

		moveTowardRestriction = MoveMaker.getMove(this.board, playerKing.getPiecePosition(),
				playerKing.getPiecePosition() + direction);
		if (pieceWouldBeSafeNextTurn(moveTowardRestriction)) {
			return moveTowardRestriction;
		}

		throw new RuntimeException("NEEDS ATTENTION");
	}

	// Helper functions

	private boolean getPinAgainstColumn() {
		return pinAgainstColumn;
	}

	public boolean waitingMoveRequired() { // if it is whites move and this returns true, a rook move must be made

		int distanceBetweenKings = this.targetKing.getPiecePosition() - this.playerKing.getPiecePosition();

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

	public boolean pieceWouldBeSafeNextTurn(Move madeMove) {
		Board newBoard = madeMove.executeMoveAndBuildBoard();

		if (newBoard.getCurrentPlayer().attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
				madeMove.getDestinationTileCoordinate()).isEmpty()) {
			return true;
		}
		return false;
	}

	public int generateBestMatingEdge() { // Only works for mate on 1st and 8th rank currently
		int matingEdge = -1;
		int bestRow = -1;
		int bestColumn = -1;

		int targetKingRow = getTargetKingRow();

		int targetKingColumn = getTargetKingColumn();

		if (targetKingRow > 3) {
			bestRow = 7;
		} else {
			bestRow = 0;
		}

		if (targetKingColumn > 3) {
			bestColumn = 7;
		} else {
			bestColumn = 0;
		}

		if (targetKingRow - bestRow > targetKingColumn - bestColumn) {

		}

		matingEdge = bestRow;

		return matingEdge;
	}

	public boolean isKingOnRestrictingRow(int matingEdge) {

		if (!getPinAgainstColumn()) {

			int rowsBetweenKings = getTargetKingRow() - getPlayerKingRow();

			if (matingEdge == 0) {
				if (rowsBetweenKings == -2) {
					return true;
				}
			} else if (matingEdge == 7) {
				if (rowsBetweenKings == 2) {
					return true;
				}
			}
		} else if (getPinAgainstColumn()) {
			// same as above
		}
		return false;
	}

	public boolean kingsAreInOpposition() { // Works and needed

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		int distanceBetweenKings = this.targetKing.getPiecePosition() - playerKingPosition;

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

	// getters

	public boolean getRookIsSafe() {
		return rookIsSafe;
	}

	public int getRookRow() {
		return this.currentLoneRookRow;
	}

	public int getTargetKingRow() {
		return this.currentTargetKingRow;
	}

	public int getPlayerKingRow() {
		return this.currentPlayerKingRow;
	}

	public int getRookColumn() {
		return this.currentLoneRookColumn;
	}

	public int getTargetKingColumn() {
		return this.currentTargetKingColumn;
	}

	public int getPlayerKingColumn() {
		return this.currentPlayerKingColumn;
	}

	public boolean isRookDefended() {
		return BoardUtility.isPieceDefended(this.loneRook, this.board);
	}

	public int getMatingEdge() {
		return matingEdge;
	}

	public Board getBoard() {
		return board;
	}

}
