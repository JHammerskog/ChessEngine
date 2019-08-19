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
	 * above.
	 * 
	 * NEEDS TESTING & FURTHER IMPLEMENTATION
	 * 
	 * COMMIT NOTE: Most of the content of this class will be refactored due to me
	 * deciding to completely change my implementation of this puzzle solver. A
	 * time-efficient less perfect (more moves to checkmate) algorithm will be
	 * implemented in place of a more convoluted and time-consuming one.
	 * 
	 * The entire move-deciding behaviour will be dictated as described in
	 * generateRestrictingMove()
	 ***/
	// These two variables should never be modified by this class, so they should be
	// passed into the constructor
	private int desiredRowOrColumnForTargetKing; // never using this right now, replaced by below booleans for
													// simplicity?
	private boolean pinAgainstColumn; // if this is false, it means the pin is against a row (tile 0-7 and 56-63), if
	// its true, then A or H file

	private boolean targetKingIsOnEdge;

	private int desiredRowOrColumnForAttackingKing;
	private boolean attackingKingInPosition;

	private boolean rookIsSafe;

	private int preMateRowOrColumnForAttackingRook;

	private int directionToMatingCoordinates; // Informs king what direction it needs to move for the desired row/column

	private final Board board;
	private final Piece targetKing;
	private final Piece loneRook;

	public KRKSolver(Board board, boolean pinAgainstColumn, int desiredRowOrColumnForTargetKing) {
		this.board = board;
		this.pinAgainstColumn = pinAgainstColumn;
		this.desiredRowOrColumnForTargetKing = desiredRowOrColumnForTargetKing;

		this.loneRook = findRook(board.getCurrentPlayer().getActivePieces());
		this.targetKing = board.getOpponent(board.getCurrentPlayer().getAlliance()).getPlayerKing();
		this.targetKingIsOnEdge = isKingOnEdge(this.targetKing.getPiecePosition());

		this.rookIsSafe = isRookDefended() || board.getCurrentPlayer()
				.attacksOnTile(board.getOpponent(board.getCurrentPlayer().getAlliance()).getLegalMovesInPosition(),
						this.loneRook.getPiecePosition())
				.isEmpty(); // Rook is safe if it is defended or if the list of opponent attack moves on its
							// tile is empty

	}

	public Move generateRestrictingMove(Board board, int matingEdge, boolean pinAgainstColumn) {
		// BELOW COMMENTS DESCRIBE HOW TO RESTRICT KING WHEN MATINGEDGE = 0 and
		// !pinAgainstColumn

		// Check maximum level of restriction to mating edge (Maximum level of
		// restriction means moving to kingColumOrRow+1 AND not getting captured)

		// Check if that rook cannot be captured, if false - return rookSafetyMove

		// Check if playerKing is on opposite ROW from targetKing (targetKingRow + 2) -
		// if false return move that brings king closer to that row

		// check if kings are in opposition - if false, check ifwaitingMoveRequired()
		// (if that is true then return a rook waiting move, if false then move king
		// along the row to get closer to targetKing)

		// If KINGS ARE IN OPPOSITION check if the rook would be uncapturable if it
		// checked the targetKing (if false, slide the rook across the column)

		// if true, return a rook checking move

		return null;
	}

	public Move findKRKMove(Board board) { // Most likely redundant

		if (this.targetKingIsOnEdge) {

			setMatingParameters();
			if (isRookOnMatingRowOrColumn(this.loneRook.getPiecePosition())) {
				if (getRookIsSafe()) { // if rook is safe. Refactor
					return generateMatingSequence(board);
				} else {
					// Move rook to safety
					return null;
				}
			} else {

				return null;
			}

		} else {
			generateBestMatingEdge();
			// and then
			return generateRestrictingMove(board, this.desiredRowOrColumnForTargetKing, this.pinAgainstColumn);
		}

	}

	public Move generateMatingSequence(Board board) { // Use this method when loneRook and targetKing are both in
														// position

		if (kingsAreInOpposition()) {
			for (Move move : this.loneRook.calculateLegalMoves(board)) { // Rook is only piece that can checkmate in
																			// this type of puzzle, so
																			// only need to consider rook legal moves
				Board newBoard = move.executeMoveAndBuildBoard();
				if (newBoard.getCurrentPlayer().isCheckMate()) {
					return move;
				} else {
					// Move the rook
				}
			}
		} else if (waitingMoveRequired()) {

			// if waiting move required, do that, or return move that gets king in
			// opposition
		} else {
			// move king in direction of opponentking as set by "setDirectionOfPlayerKing()"
		}

		return null;
	}

	public Move generateRookWaitingMove(boolean pinAgainstColumn, int matingEdge) { // Refactor this to generateRestrictingMove()
		Move move = null;
		if (pinAgainstColumn) {
			if (matingEdge == 0) {
				// go to tile 1 or 57
				move = MoveMaker.getMove(this.board, this.loneRook.getPiecePosition(), 1);
				if (/* move is legal and rook would be defended */) {
					// return move;
				} // else try tile 57

			} else if (matingEdge == 7) {
				// go to tile 6 or 62
			}
		} else if (!pinAgainstColumn) {
			if (matingEdge == 0) {
				// go to tile 8 or 15
			} else if (matingEdge == 7) {
				// go to tile 48 or 55
			}
		}

		return null;
	}

	// Helper functions below

	public int generateBestMatingEdge() { // PROBLEM: Setting a new one every time a position is evaluated could lead
											// to problems?
		int targetKingPosition = this.targetKing.getPiecePosition();

		// check which edge targetKingPosition is closest to
		// return that edge so generateRestrictingMove() can start working
		// also should be used by setMatingParameters()

		int targetKingRow = BoardUtility.calculateRow(targetKingPosition);
		int targetKingColumn = BoardUtility.calculateColumn(targetKingPosition);

		int edgeNumber = -1; // will either be 0 or 7

		// above variable closest to either 0 or 7 will be the best mating edge
		return edgeNumber;
	}

	// When making a restricting move, use the below function to determine whether
	// the destinationTile is safe or not

	public int setDirectionOfPlayerKing(boolean pinAgainstColumn) { // Refactor this
		// This function will inform the attacking king which direction it needs to go
		// for the mating sequence to be successful

		// CURRENTLY ONLY WORKS IF TARGET KING IS ALREADY ON THE EDGE & LONEROOK IS IN
		// CORRECT POSITION

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		boolean playerKingOnCorrectRowOrColumn = false;

		if (pinAgainstColumn) { // This if statement could use improvement
			playerKingOnCorrectRowOrColumn = BoardUtility
					.calculateColumn(playerKingPosition) == this.desiredRowOrColumnForAttackingKing;
		} else {
			playerKingOnCorrectRowOrColumn = BoardUtility
					.calculateRow(playerKingPosition) == this.desiredRowOrColumnForAttackingKing;
		}

		if (!(playerKingOnCorrectRowOrColumn)) {
			// The below could break the puzzlesolver if the diretion given leads to an
			// illegal move (if there is a piece in the way)
			if (!pinAgainstColumn) {
				if (BoardUtility.calculateRow(playerKingPosition) > this.desiredRowOrColumnForAttackingKing) {
					return -8;
				} else {
					return 8;
				}

			} else {
				if (BoardUtility.calculateColumn(playerKingPosition) > this.desiredRowOrColumnForAttackingKing) {
					return -1;
				} else {
					return 1;
				}
			}
		} else { // The below logic will try to get the playerKing in opposition of the
					// targetKing
			if (pinAgainstColumn) {
				if (playerKingPosition < targetKing.getPiecePosition()) {
					return 8;
				} else {
					return -8;
				}

			} else if (!pinAgainstColumn) {
				if (playerKingPosition > this.targetKing.getPiecePosition()) {
					if (playerKingPosition - this.targetKing.getPiecePosition() > 16) {
						return -1;
					} else {
						return +1;
					}
				} else {
					if (playerKingPosition - this.targetKing.getPiecePosition() > -16) {
						return -1;
					} else {
						return +1;
					}
				}

			}
		}

		// The way the above if funciton is structured means that another function has
		// to
		// check if kings are in opposition. (See kingsAreInOpposition())
		throw new RuntimeException("Should be impossible to get here");
	}

	public boolean kingsAreInOpposition() { // Works and needed

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		int distanceBetweenKings = this.targetKing.getPiecePosition() - playerKingPosition;

		if (this.pinAgainstColumn) { // If pin is against column
			if (distanceBetweenKings == -2 || distanceBetweenKings == 2) {
				return true;
			}

		} else if (!this.pinAgainstColumn) { // If pin is against Row
			if (distanceBetweenKings == -16 || distanceBetweenKings == 16) {
				return true;
			}

		}
		return false;
	}

	public boolean waitingMoveRequired() { // if it is whites move and this returns true, a rook move must be made
		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		int distanceBetweenKings = this.targetKing.getPiecePosition() - playerKingPosition;

		if (this.pinAgainstColumn) {
			if (distanceBetweenKings == -10 || distanceBetweenKings == -6 || distanceBetweenKings == 6
					|| distanceBetweenKings == -10) {
				return true;
			}

		} else if (!this.pinAgainstColumn) {
			if (distanceBetweenKings == -15 || distanceBetweenKings == -17 || distanceBetweenKings == 15
					|| distanceBetweenKings == -17) {
				return true;
			}

		}

		return false;
	}

	// Make a waitingMoveRequired() method (e.g. if enemy king can avoid opposition

	public Rook findRook(List<Piece> activePieces) { // is only used to set the class variable "loneRook"
		for (Piece piece : activePieces) {
			if (piece.getPieceType() == PieceType.ROOK) {
				return (Rook) piece;
			}

		}
		throw new RuntimeException("This heuristic only works for a King-Rook vs. King endgame");
	}

	public boolean isKingOnEdge(int tileCoordinate) { // Move this to BoardUtility class and pass it a piecePosition
		return BoardUtility.isPieceOnEdge(tileCoordinate);
	}

	public boolean getRookIsSafe() {
		return rookIsSafe;
	}

	public boolean isRookOnMatingRowOrColumn(int tileCoordinate) {
		if (this.pinAgainstColumn) {
			if (BoardUtility.calculateColumn(tileCoordinate) == this.preMateRowOrColumnForAttackingRook) {
				return true;
			}
		} else {
			if (BoardUtility.calculateRow(tileCoordinate) == this.preMateRowOrColumnForAttackingRook) {
				return true;
			}
		}

		return false;
	}

	public boolean isRookDefended() { // This should be abstracted and moved somewhere more generic
										// (isPieceDefended(Piece piece))
		if (this.board.getCurrentPlayer().getDefendedPieces().contains(this.loneRook)) {
			return true;
		}

		return false;
	}

	public void setMatingParameters() { // Completely redundant, needs complete refactoring

		// THIS FUNCTION ONLY WORKS WHEN KING IS ALREADY ON A EDGE
		// Refactor after writing more of generateRestrictingMove()

		int coordinateOnEdge = this.targetKing.getPiecePosition();
		// The above variable is the only thing that needs changing. INstead of setting
		// it to the targetKings piece position, a function should determine which edge
		// is the most ideal for mating.

		if (BoardUtility.calculateColumn(coordinateOnEdge) == 0) {
			this.desiredRowOrColumnForTargetKing = 0;
			this.preMateRowOrColumnForAttackingRook = 1;
			this.pinAgainstColumn = true;
			this.desiredRowOrColumnForAttackingKing = 2;

			this.directionToMatingCoordinates = setDirectionOfPlayerKing(this.pinAgainstColumn);

		} else if (BoardUtility.calculateColumn(coordinateOnEdge) == 7) {
			this.desiredRowOrColumnForTargetKing = 7;
			this.desiredRowOrColumnForAttackingKing = 6;
			this.pinAgainstColumn = true;
			this.desiredRowOrColumnForAttackingKing = 5;

			this.directionToMatingCoordinates = setDirectionOfPlayerKing(this.pinAgainstColumn);

		}

		if (BoardUtility.calculateRow(coordinateOnEdge) == 0) {
			this.desiredRowOrColumnForTargetKing = 0;
			this.preMateRowOrColumnForAttackingRook = 1;
			this.pinAgainstColumn = false;
			this.desiredRowOrColumnForAttackingKing = 2;

			this.directionToMatingCoordinates = setDirectionOfPlayerKing(this.pinAgainstColumn);

		} else if (BoardUtility.calculateRow(coordinateOnEdge) == 7) {
			this.desiredRowOrColumnForTargetKing = 7;
			this.desiredRowOrColumnForAttackingKing = 6;
			this.pinAgainstColumn = false;
			this.desiredRowOrColumnForAttackingKing = 5;
			this.directionToMatingCoordinates = setDirectionOfPlayerKing(this.pinAgainstColumn);
		}
	}

}
