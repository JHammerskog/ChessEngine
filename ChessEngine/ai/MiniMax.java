package ai;

import board.Board;
import board.Move;
import player.Player;

public class MiniMax {

	/***
	 * This ai is based on a simple minimax recursive algorithm. It scores
	 * potential boards, and returns the move that leads to the board with the best
	 * possible outcome for the player. Able to solve the KRKMateInTwo() Board.
	 * 
	 * Still buggy, most likely due to an incorrect flag in either minimize() or
	 * maximize()
	 * 
	 * Will come back to this at a later time.
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

		for (Move move : board.getCurrentPlayer().getLegalMovesInPosition()) {

			Board newBoard = move.executeMoveAndBuildBoard();

			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck() == true) {

				if (maximizingPlayer) {

					tempValue = minimize(newBoard, depth);

					if (tempValue >= bestValueForWhite) {
						bestValueForWhite = tempValue;
						selectedMove = move;
						System.out.println("Best found move destination for white " + move.getMovedPiece() + ": "
								+ move.getDestinationTileCoordinate() + " worth: " + bestValueForWhite);

					}

				} else if (!maximizingPlayer) {

					tempValue = maximize(newBoard, depth);

					if (tempValue <= bestValueForBlack) {

						bestValueForBlack = tempValue;
						selectedMove = move;
						System.out.println("Best found move destination for black " + move.getMovedPiece() + ": "
								+ move.getDestinationTileCoordinate() + " worth: " + bestValueForBlack);

					}
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
			Player currentPlayer = newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance());
			if (currentPlayer.getIsNotInCheck() == true) {
				int tempValue = minimize(newBoard, depth - 1);

				if (tempValue > 20000000) {// Added this due to currently unknown bug
					throw new RuntimeException("Score impossibly high.");
				}

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
			Player currentPlayer = newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance());

			if (currentPlayer.getIsNotInCheck() == true) {

				int tempValue = maximize(newBoard, depth - 1);

				if (tempValue > 20000000) { // Added this due to currently unknown bug
					throw new RuntimeException("Score impossibly high.");
				}
				if (tempValue <= bestFoundValue) {
					bestFoundValue = tempValue;
				}

			}

		}

		return bestFoundValue;
	}

}
