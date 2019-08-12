package player;

import board.Board;
import board.Move;

public class BoardTransition {

	/***
	 * This class stores variables relating to the transitioning of a board.
	 * Currently, only newBoard will be used (for output of a Board) but the other
	 * variables will be useful for future GUI and feature development.
	 */

	private Board newBoard;

	// below variables are currently redundant, but will be helpful
	private Board oldBoard;
	private Move madeMove;

	public BoardTransition(Board oldBoard, Board newBoard, Move madeMove) {
		this.oldBoard = oldBoard;
		this.newBoard = newBoard;
		this.madeMove = madeMove;
	}

	public Board getOldBoard() {
		return oldBoard;
	}

	public Board getNewBoard() {
		return newBoard;
	}

	public Move getMadeMove() {
		return madeMove;
	}

}
