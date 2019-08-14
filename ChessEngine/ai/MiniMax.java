package ai;

import board.Board;
import board.Move;

public class MiniMax {

	/***
	 * This is ai is based on a simple minimax recursive algorithm. It currently
	 * goes through all pseudolegal moves, if the move is possible it scores the
	 * board, and returns the move with the highest score.
	 * 
	 * Currently, this AI is not working very well. 
	 ***/

	StateEvaluator positionScore;
	int depth;

	public MiniMax(int depth) {
		this.depth = depth;
		this.positionScore = new StateEvaluator();
	}

	public Move minimax(int depth, boolean maximizingPlayer, Board board) {

		long startTime = System.currentTimeMillis();
		System.out.println("Starting to evaluate position with depth: " + depth);

		Move selectedMove = null;

		int bestValueForWhite = Integer.MIN_VALUE;
		int bestValueForBlack = Integer.MAX_VALUE;
		int tempValue;

		for (Move move : board.getCurrentPlayer().getLegalMovesInPosition()) { // FOr every move, see the best
																				// guaranteed score for the player

			Board newBoard = move.executeMoveAndBuildBoard();

			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getCheckStatus() == true) {

				if (maximizingPlayer) {

					tempValue = maximize(board, depth);

				} else {
					tempValue = minimize(board, depth);
				}

				// After highest score has been found, check which player it was and if the move
				// is better than previous moves, set it to selected move

				if (maximizingPlayer && tempValue >= bestValueForWhite) {
					bestValueForWhite = tempValue;
					selectedMove = move;

				} else if (!(maximizingPlayer) && tempValue <= bestValueForBlack) {

					bestValueForBlack = tempValue;
					selectedMove = move;
				}
			}
		}

		long endTime = System.currentTimeMillis() - startTime;

		System.out.println("This took me: " + endTime + " milliseconds to compute!");

		return selectedMove;
	}

	private int maximize(final Board board, int depth) {
		int bestFoundValue = Integer.MIN_VALUE;

		if (depth == 0 || board.getCurrentPlayer().isCheckMate()) {
			return this.positionScore.scorePosition(board, depth);
		}

		for (Move move : board.getCurrentPlayer().getLegalMovesInPosition()) {

			Board newBoard = move.executeMoveAndBuildBoard();

			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getCheckStatus() == true) {
				int tempValue = minimize(newBoard, depth - 1);

				if (tempValue >= bestFoundValue) {
					bestFoundValue = tempValue;
				}
			}

		}

		return bestFoundValue;
	}

	private int minimize(final Board board, int depth) {
		int bestFoundValue = Integer.MAX_VALUE; 

		if (depth == 0 || board.getCurrentPlayer().isCheckMate()) {
			return this.positionScore.scorePosition(board, depth);
		}

		for (Move move : board.getCurrentPlayer().getLegalMovesInPosition()) {
			Board newBoard = move.executeMoveAndBuildBoard();

			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getCheckStatus() == true) {

				int tempValue = maximize(newBoard, depth - 1);
				if (tempValue <= bestFoundValue) {
					bestFoundValue = tempValue;
				}
			}

		}

		return bestFoundValue;
	}

}
