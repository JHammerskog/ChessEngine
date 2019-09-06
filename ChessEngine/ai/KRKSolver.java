package ai;

import java.util.List;

import board.Alliance;
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
	 * given to it.
	 * 
	 * In this iteration, working on being able to mate the opponents king on the
	 * vertical edges of the board has been de-prioritized in favour of developing
	 * more heuristics. There is however a lot of 'skeleton'-code which has not been
	 * written out due to this de-prioritization. For future optimization, the
	 * relevant code can just be plugged into the functions below as described by
	 * comments.
	 ***/

	private final Piece loneRook;

	private final int currentLoneRookRow;
	private final int currentLoneRookColumn;

	private final boolean rookIsSafe;

	public KRKSolver(Board board) {
		super(board);

		this.loneRook = findRook(board.getCurrentPlayer().getActivePieces());

		this.currentLoneRookRow = BoardUtility.calculateRow(this.loneRook.getPiecePosition());
		this.currentLoneRookColumn = BoardUtility.calculateColumn(this.loneRook.getPiecePosition());

		this.rookIsSafe = isRookDefended(getRook().getPieceAlliance()) || board.getCurrentPlayer()
				.attacksOnTile(board.getOpponent(board.getCurrentPlayer().getAlliance()).getLegalMovesInPosition(),
						this.loneRook.getPiecePosition())
				.isEmpty(); // Rook is safe if it is defended or if the list of opponent attack moves on its
							// tile is empty

	}

	public Move generateRestrictingMove(Board board) { // called to generate a king-rook move

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
			// try catch needed here
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

						if (pieceNotAttackedAfterMove(move)) {
							return move;
						}

					}
				}
			} else {
				rookTargetRow = getTargetKingRow() - 1;
				for (Move move : loneRook.calculateLegalMoves(this.board)) { // try to improve this
					if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == rookTargetRow) {
						if (pieceNotAttackedAfterMove(move)) {
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

			for (Move move : getRook().calculateLegalMoves(getBoard())) {
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == getRookRow()) { // same row
					if (BoardUtility.calculateColumn(move.getDestinationTileCoordinate()) == 0
							|| BoardUtility.calculateColumn(move.getDestinationTileCoordinate()) == 7) { // edge of
																											// board
						safetyMove = move;
						if (pieceNotAttackedAfterMove(safetyMove)) {
							return safetyMove;
						}
					}

				}
			}

		} else {
			for (Move move : getRook().calculateLegalMoves(getBoard())) {
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == 0
						|| BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == 7) {
					safetyMove = move;
					if (pieceNotAttackedAfterMove(safetyMove)) {
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
				if (pieceNotAttackedAfterMove(checkingMove)) {
					return checkingMove;
				}
			} else {
				checkingMove = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(),
						this.loneRook.getPiecePosition() + 8);
				if (pieceNotAttackedAfterMove(checkingMove)) {
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

			if (!pieceNotAttackedAfterMove(move)) {
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

	public Rook getRook() {
		return (Rook) loneRook;
	}

	public int getRookRow() {
		return this.currentLoneRookRow;
	}

	public int getRookColumn() {
		return this.currentLoneRookColumn;
	}

	public boolean isRookDefended(Alliance alliance) {
		return BoardUtility.isPieceDefended(getRook(), getBoard(), alliance);
	}

}
