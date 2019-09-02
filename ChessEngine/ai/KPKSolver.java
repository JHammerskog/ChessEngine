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


	private final Piece lonePiece;

	private final int stepsToPromotion;
	private int playerKingStepsToDefense;
	private int targetKingStepsToAttack;

	public KPKSolver(Board board) {
		super(board);


		this.lonePiece = findLonePiece(board.getCurrentPlayer().getActivePieces());

		this.stepsToPromotion = stepsToPromotion(this.lonePiece.getPiecePosition()); // will be redundant after
																						// promotion


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


	public boolean couldBeCheckmatePosition() { // if it is whites move and this returns true, check queen moves for
												// checkmate
		int distanceBetweenKings = getTargetKing().getPiecePosition() - getPlayerKing().getPiecePosition();

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



}
