package ai;

import java.util.List;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;
import pieces.Rook;
import pieces.Piece.PieceType;

/*
 * Dont Repeat Yourself problems for KPK and KRK:
 * 
 * generateBestMatingEdge()
 * makeKingRestrictingMove()
 * isKingOnRestrictingRow()
 * pieceWouldBeSafeNextTurn()
 * 
 * */

public class KPKSolver extends EndgameSolver {

	private Board board;

	private boolean pinAgainstColumn;
	private final int matingEdge;

	private final Piece targetKing;
	private final Piece playerKing;
	private final Piece lonePiece;

	private int currentTargetKingColumn;
	private int currentTargetKingRow;

	private int currentPlayerKingRow;
	private int currentPlayerKingColumn;

	private final int stepsToPromotion;
	private int playerKingStepsToDefense; // need good evaluation methods
	private int targetKingStepsToAttack;

	public KPKSolver(Board board) {
		this.board = board;

		this.lonePiece = findLonePiece(board.getCurrentPlayer().getActivePieces());
		this.targetKing = board.getOpponent(board.getCurrentPlayer().getAlliance()).getPlayerKing();
		this.playerKing = board.getCurrentPlayer().getPlayerKing();

		this.currentTargetKingColumn = BoardUtility.calculateColumn(this.targetKing.getPiecePosition());
		this.currentTargetKingRow = BoardUtility.calculateRow(this.targetKing.getPiecePosition());

		this.currentPlayerKingRow = BoardUtility.calculateRow(this.playerKing.getPiecePosition());
		this.currentPlayerKingColumn = BoardUtility.calculateColumn(this.playerKing.getPiecePosition());

		this.stepsToPromotion = stepsToPromotion(this.lonePiece.getPiecePosition()); // will be redundant after
																						// promotion

		this.matingEdge = generateBestMatingEdge();

		// check for cannotWinScenarios() last thing in constructor?

	}

	public Move generateKPKMove(Board board) {
		// if LonePiece == Pawn

		// while playerKing is able to defend pawn before capture OR pawn can promote
		// before capture:
		// return pawnmove

		// if loop breaks, move king to defense position close to pawn,

		// if LonePiece == Queen
		// Check for stalemate before making everymove

		// Check if king is restricted by Queen

		// if restricted, check if king is on restriction row (if not move king to
		// restrictingRow)

		// if

		return null;
	}

	// helper methods

	public void cannotWinScenarios() {
		// if pawn is on edge and targetKing is closer to PromotionCorner

		// the player pawn can be captured before the playerKing can defend it

		// if either of the above are true, offer draw
	}

	// move

	private Move makePawnMove() {

		// if current player is white
		Move pawnMove = MoveMaker.getMove(board, this.lonePiece.getPiecePosition(),
				this.lonePiece.getPiecePosition() + 8); // only works for pawn

		// else do -8 instead

		return pawnMove;
	}

	private Move moveKingInDefense() {
		// If possible, move king to a square which results in pawn being defended

		// if not possible, make a move which brings king closer to pawn being defended

		return null;
	}

	// Helper methods

	public boolean moveWouldLeadToStalemate(Move madeMove) {
		// Need to refactor isStaleMate() in Player.java before this works
		Board newBoard = madeMove.executeMoveAndBuildBoard();

		if (newBoard.getCurrentPlayer().isStaleMate()) {
			return true;
		}
		return false;
	}

	public int stepsToPromotion(int tileCoordinate) {
		return tileCoordinate / 8; // for white, implement for in later iteration
	}

	public boolean pawnIsonEdge(int tileCoordinate) {
		return BoardUtility.isPieceOnEdge(tileCoordinate);
	}

	public int rowsToTargetSquare(int currentRow, int targetSquare) {
		int targetSquareRow = BoardUtility.calculateRow(targetSquare);
		if (currentRow > targetSquareRow) {
			return currentRow - targetSquareRow;
		} else {
			return targetSquareRow - currentRow;
		}
	}

	public int columnsToTargetSquare(int currentColumn, int targetSquare) {
		int targetSquareColumn = BoardUtility.calculateColumn(targetSquare);
		if (currentColumn > targetSquareColumn) {
			return currentColumn - targetSquareColumn;
		} else {
			return targetSquareColumn - currentColumn;
		}
	}

	public int calculateAmountOfEfficientDiagonalMoves(int kingColumnOrRow, int targetSquare) {
		int rowDistance = rowsToTargetSquare(kingColumnOrRow, targetSquare);
		int columnDistance = columnsToTargetSquare(kingColumnOrRow, targetSquare);

		if (rowDistance > columnDistance) {
			return rowDistance - columnDistance;
		} else {
			return columnDistance - rowDistance;
		}
	}

	public int generateBestMatingEdge() { // Only works for mate on 1st and 8th rank currently
		int matingEdge = -1;
		int bestRow = -1;
//		int bestColumn = -1;

		int targetKingRow = getCurrentTargetKingRow();

//		int targetKingColumn = getCurrentTargetKingColumn();

		if (targetKingRow > 3) {
			bestRow = 7;
		} else {
			bestRow = 0;
		}

		matingEdge = bestRow;
		setPinAgainstColumn(false);

		return matingEdge;
	}

	public boolean couldBeCheckmatePosition() { // if it is whites move and this returns true, check queen moves for
												// checkmate
		int distanceBetweenKings = this.targetKing.getPiecePosition() - this.playerKing.getPiecePosition();

		if (getPinAgainstColumn()) {
			if (distanceBetweenKings == -10 || distanceBetweenKings == -6 || distanceBetweenKings == -2
					|| distanceBetweenKings == 2 || distanceBetweenKings == 6 || distanceBetweenKings == 10) {
				return true;
			}

		} else if (!getPinAgainstColumn()) {
			if (distanceBetweenKings == -15 || distanceBetweenKings == -17 || distanceBetweenKings == -16
					|| distanceBetweenKings == 16 || distanceBetweenKings == 15 || distanceBetweenKings == 17) {
				return true;
			}

		}
		return false;
	}

	public Piece findLonePiece(List<Piece> activePieces) { // Method to identify either
		for (Piece piece : activePieces) {
			if (!(piece.getPieceType() == PieceType.KING)) {
				return piece;
			}

		}
		throw new RuntimeException("This heuristic only works for a King-Rook vs. King endgame");
	}

	// Getters and setter

	public int getCurrentTargetKingColumn() {
		return currentTargetKingColumn;
	}

	public int getCurrentTargetKingRow() {
		return currentTargetKingRow;
	}

	public int getCurrentPlayerKingRow() {
		return currentPlayerKingRow;
	}

	public int getCurrentPlayerKingColumn() {
		return currentPlayerKingColumn;
	}

	private boolean getPinAgainstColumn() {
		return pinAgainstColumn;
	}

	public void setPinAgainstColumn(boolean pinAgainstColumn) {
		this.pinAgainstColumn = pinAgainstColumn;
	}

	public Board getBoard() {
		return board;
	}

}
