package ai;

import board.Board;
import board.BoardUtility;
import board.Move;
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

		// int targetKingColumn = getTargetKingColumn();

		if (targetKingRow > 3) {
			bestRow = 7;
		} else {
			bestRow = 0;
		}

		matingEdge = bestRow;
		setPinAgainstColumn(false);

		return matingEdge;
	}

	public boolean pieceWouldBeSafeNextTurn(Move madeMove) {
		Board newBoard = madeMove.executeMoveAndBuildBoard();

		if (newBoard.getCurrentPlayer().attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
				madeMove.getDestinationTileCoordinate()).isEmpty()) {
			return true;
		}
		return false;
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
