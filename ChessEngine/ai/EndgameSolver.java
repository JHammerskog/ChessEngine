package ai;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;

public class EndgameSolver {
	/***
	 * This class is the parent class of KRKSolver and KPKSolver and contains the
	 * majority of shared functionality/requirements between the two heuristics. The
	 * information contained in this class will consistently be used by both
	 * heuristics, which is why using inheritance makes sense.
	 */

// In a future iteration you could add king logic here (which lessen duplicate code between the heuristics)

	final Board board;
	private int currentTargetKingColumn;
	private int currentTargetKingRow;
	private int currentPlayerKingRow;
	private int currentPlayerKingColumn;

	private int matingEdge;
	private boolean pinAgainstColumn;

	private final Piece targetKing;
	private final Piece playerKing;

	public EndgameSolver(Board board) {
		this.board = board;

		this.targetKing = board.getOpponent(board.getCurrentPlayer().getAlliance()).getPlayerKing();
		this.playerKing = board.getCurrentPlayer().getPlayerKing();

		this.currentTargetKingColumn = BoardUtility.calculateColumn(this.targetKing.getPiecePosition());
		this.currentTargetKingRow = BoardUtility.calculateRow(this.targetKing.getPiecePosition());

		this.currentPlayerKingRow = BoardUtility.calculateRow(this.playerKing.getPiecePosition());
		this.currentPlayerKingColumn = BoardUtility.calculateColumn(this.playerKing.getPiecePosition());

		this.matingEdge = generateBestMatingEdge();

	}

	public int generateBestMatingEdge() { // Only works for mate on 1st and 8th rank currently
		int matingEdge = -1;
		int bestRow = -1;
		// int bestColumn = -1;

		int targetKingRow = getTargetKingRow();

		if (getTargetKingRow() > getPlayerKingRow()) {
			matingEdge = 7;
		} else if (getTargetKingRow() == getPlayerKingRow()) {
			if (targetKingRow > 3) {
				matingEdge = 7;
			} else {
				matingEdge = 0;
			}
		} else {
			matingEdge = 0;
		}

		setPinAgainstColumn(false);

		return matingEdge;
	}
	
	public Move makeKingRestrictingMove() { // Exception handling needed
		Move kingRestrictingMove = null;

		if (!getPinAgainstColumn()) {
			if (getPlayerKingColumn() > getTargetKingColumn()) {
				kingRestrictingMove = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
						getPlayerKing().getPiecePosition() - 1);
				if (pieceNotAttackedAfterMove(kingRestrictingMove)) {
					return kingRestrictingMove;
				}
			} else {
				kingRestrictingMove = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
						getPlayerKing().getPiecePosition() + 1);
				if (pieceNotAttackedAfterMove(kingRestrictingMove)) {
					return kingRestrictingMove;
				}
			}
		} else {
			// do same as above, but return -8 or + 8 instead
		}

		throw new RuntimeException("Needs attention");
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
		if (pieceNotAttackedAfterMove(moveTowardRestriction)) {
			return moveTowardRestriction;
		}

		throw new RuntimeException("NEEDS ATTENTION");
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
			// same as above but get columns instead of rows
		}
		return false;
	}
	

	public boolean pieceNotAttackedAfterMove(Move madeMove) {
		Board newBoard = madeMove.executeMoveAndBuildBoard();

		if (newBoard.getCurrentPlayer().attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
				madeMove.getDestinationTileCoordinate()).isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean moveWouldLeadToStalemate(Move madeMove) {
		Board newBoard = madeMove.executeMoveAndBuildBoard();

		if (newBoard.getCurrentPlayer().isStaleMate()) {
			return true;
		}
		return false;
	}

	public int getTargetKingRow() {
		return this.currentTargetKingRow;
	}

	public int getPlayerKingRow() {
		return this.currentPlayerKingRow;
	}

	public int getTargetKingColumn() {
		return this.currentTargetKingColumn;
	}

	public int getPlayerKingColumn() {
		return this.currentPlayerKingColumn;
	}

	public int getMatingEdge() {
		return matingEdge;
	}

	public Piece getTargetKing() {
		return this.targetKing;
	}

	public Piece getPlayerKing() {
		return this.playerKing;
	}

	public boolean getPinAgainstColumn() {
		return pinAgainstColumn;
	}

	public void setPinAgainstColumn(boolean pinAgainstColumn) {
		this.pinAgainstColumn = pinAgainstColumn;
	}

	public Board getBoard() {
		return board;
	}

}
