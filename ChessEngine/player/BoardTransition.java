package player;

import board.Board;
import board.Move;

public class BoardTransition {

	/***
	 * This class stores variables relating to the transitioning of a board. This is
	 * especially useful for features relating to transitioning back to previous
	 * boards (like an undo-move feature).
	 * 
	 * Currently, it is most likely not within the scope of this project to
	 * implement such features, and this class will most likely be redundant at
	 * project handin.
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
