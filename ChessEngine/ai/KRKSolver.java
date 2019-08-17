package ai;

import java.util.List;

import board.Board;
import board.BoardUtility;
import board.Move;
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
	 ***/

	int desiredRowOrColumnForTargetKing; // never using this right now, replaced by below booleans for simplicity?
	boolean targetKingIsOnEdge;
	boolean pinAgainstColumn; // if this is false, it means the pin is against a row (tile 0-7 and 56-63), if
								// its true, then A or H file

	int desiredRowOrColumnForAttackingKing;
	int preMateRowOrColumnForAttackingRook;

	int directionToMatingCoordinates; // Informs king what direction it needs to move for the desired row/column

	final Board board;
	final Piece targetKing;
	final Piece loneRook;

	public KRKSolver(Board board) {
		this.board = board;

		this.loneRook = findRook(board.getCurrentPlayer().getActivePieces());
		this.targetKing = board.getOpponent(board.getCurrentPlayer().getAlliance()).getPlayerKing();
		this.targetKingIsOnEdge = isKingOnEdge();

	}

	public Move findKRKMove(Board board) {

		if (isKingOnEdge()) {

			setMatingParameters();
			// determine if rook is on edge+1
			return generateMatingSequence();

		} else {
			// Find which edge king is closest too and:
			return generateRestrictingMove();
		}

	}

	public Move generateMatingSequence() {

		// Check for mate in 1
		// Else If targetKing cannot take the rook next turn and king is not in
		// opposition, move king on edge+2 and try to get into opposition

		// Else If rook is not already there && the square is not attacked by
		// targetKing: RETURN: Move rook to column/row of kingRowOrColumn+1 to restrict
		// targetKing
		// else: Move rook to edge+2 WORRY ABOUT THIS LAST
		return null;
	}

	public Move generateRestrictingMove() {

		// Find closest edge to restrict king to

		// check every move that the rook is not threatened, and that it will not be
		// threatened after the next move
		// If threatened, move across the row/column
		// Look to restrict more, if you cannot restrict more move king closer

		return null;
	}

	// Helper functions below

	public void generateBestMatingEdge() { // implement when less tired
		int targetKingPosition = this.targetKing.getPiecePosition();

		// check which edge targetKingPosition is closest to
		// return that edge so generateRestrictingMove() can start working
		// also should be used by setMatingParameters()

		int targetKingRow = BoardUtility.calculateRow(targetKingPosition);
		int targetColumn = BoardUtility.calculateColumn(targetKingPosition);

		// above variable closest to either 0 or 7 will be the best mating edge

	}

	public boolean isKingOnEdge() { // works
		if (BoardUtility.calculateColumn(this.targetKing.getPiecePosition()) == 0 // A column
				|| BoardUtility.calculateColumn(this.targetKing.getPiecePosition()) == 7 // H Column
				|| BoardUtility.calculateRow(this.targetKing.getPiecePosition()) == 0 // 8th rank (from white's
																						// perspective)
				|| BoardUtility.calculateRow(this.targetKing.getPiecePosition()) == 7) { // 1st rank (from white's
			// perspective)
			return true;
		}
		return false;
	}

	// When making a restricting move, use the below function to determine whether
	// the destinationTile is safe or not
	public boolean targetKingCanCaptureRookNextMove(Move move) { // Needs testing
		Board newBoard = move.executeMoveAndBuildBoard();

		Piece loneRook = findRook(newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getActivePieces());

		for (Move potentialRookCapture : newBoard.getCurrentPlayer().getLegalMovesInPosition()) {
			if (potentialRookCapture.getDestinationTileCoordinate() == loneRook.getPiecePosition()) {
				// This list has to be made to check attacks on the rooks tile
				List<Move> attacksOnTile = newBoard.getCurrentPlayer()
						.attacksOnTile(
								newBoard.getOpponent(newBoard.getCurrentPlayer().getOpponentAlliance())
										.getLegalMovesInPosition(),
								potentialRookCapture.getDestinationTileCoordinate());
				if (!(attacksOnTile.isEmpty())) {
					return true;
				}
			}
		}

		return false;
	}

	// This function will set
	public void setMatingParameters() { // Needs testing

		// THIS FUNCTION ONLY WORKS WHEN KING IS ALREADY ON A EDGE
		// Refactor after writing more of generateRestrictingMove()

		int coordinateOnEdge = this.targetKing.getPiecePosition();
		// The above variable is the only thing that needs changing. INstead of setting
		// it to the targetKings piece position, a function should determine which edge
		// is the most ideal for mating.

		if (BoardUtility.calculateColumn(coordinateOnEdge) == 0) {
			this.desiredRowOrColumnForTargetKing = 0;
			this.pinAgainstColumn = true;
			this.desiredRowOrColumnForAttackingKing = 2;

			this.directionToMatingCoordinates = setDirection(this.pinAgainstColumn);

		} else if (BoardUtility.calculateColumn(coordinateOnEdge) == 7) {
			this.desiredRowOrColumnForTargetKing = 7;
			this.pinAgainstColumn = true;
			this.desiredRowOrColumnForAttackingKing = 5;

			this.directionToMatingCoordinates = setDirection(this.pinAgainstColumn);

		}

		if (BoardUtility.calculateRow(coordinateOnEdge) == 0) {
			this.desiredRowOrColumnForTargetKing = 0;
			this.pinAgainstColumn = false;
			this.desiredRowOrColumnForAttackingKing = 2;

			this.directionToMatingCoordinates = setDirection(this.pinAgainstColumn);

		} else if (BoardUtility.calculateRow(coordinateOnEdge) == 7) {
			this.desiredRowOrColumnForTargetKing = 7;
			this.pinAgainstColumn = false;
			this.desiredRowOrColumnForAttackingKing = 5;
			this.directionToMatingCoordinates = setDirection(this.pinAgainstColumn);
		}
	}

	public int setDirection(boolean pinAgainstColumn) {
		// This function will inform the attacking king which direction it needs to go
		// for the mating sequence to be successful

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();

		if (!(this.targetKingIsOnEdge)) { // This is currently never true

			if (!pinAgainstColumn && playerKingPosition > this.targetKing.getPiecePosition()) {
				return -8;
			} else if (!pinAgainstColumn && playerKingPosition < this.targetKing.getPiecePosition()) {
				return 8;
			} else if (pinAgainstColumn && BoardUtility.calculateColumn(playerKingPosition) > BoardUtility
					.calculateColumn(this.targetKing.getPiecePosition())) {
				return -1;
			} else if (pinAgainstColumn && BoardUtility.calculateColumn(playerKingPosition) < BoardUtility
					.calculateColumn(this.targetKing.getPiecePosition())) {
				return 1;
			}

		} else { // if targetKing is on correct row/column, and so is the player's king do:
			if (pinAgainstColumn) {
				if (playerKingPosition < targetKing.getPiecePosition()) {
					return -8;
				} else {
					return 8;
				}

			} else if (!pinAgainstColumn) {
				if (playerKingPosition > this.targetKing.getPiecePosition()) {
					if (playerKingPosition - this.targetKing.getPiecePosition() > 8) {
						return -1;
					} else {
						return +1;
					}
				} else {
					if (playerKingPosition - this.targetKing.getPiecePosition() > -8) {
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

	public boolean kingsAreInOpposition() {

		int playerKingPosition = this.board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		int distanceBetweenKings = this.targetKing.getPiecePosition() - playerKingPosition;

		// ADD CHECK

		if (this.pinAgainstColumn) { // If pin is against column
			if (this.targetKingIsOnEdge) {
				if (distanceBetweenKings == -2 || distanceBetweenKings == 2) {
					return true;
				}
			}
		} else if (!this.pinAgainstColumn) { // If pin is against Row
			if (this.targetKingIsOnEdge) {
				if (distanceBetweenKings == -16 || distanceBetweenKings == 16) {
					return true;
				}
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

}
