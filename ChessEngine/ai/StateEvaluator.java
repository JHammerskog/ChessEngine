package ai;

import board.Board;
import pieces.Piece;
import player.Player;

public class StateEvaluator {

	private int checkmateScore = 1000000;
	// TODO A lot more evaluating (bonus scores for check, depth etc)

	public int scorePosition(Board board, int depth) {
		int positionScore = scorePlayer(board, board.getWhitePlayer(), depth)
				- scorePlayer(board, board.getBlackPlayer(), depth);

		return positionScore;
	}

	public int scorePlayer(Board board, Player player, int depth) {

		int playerScore = 0;

		for (Piece piece : player.getActivePieces()) {
			playerScore += piece.getPieceType().getPieceValue();
		}

		if ((board.getCurrentPlayer().isCheckMate()) && board.getCurrentPlayer() != player) {
			playerScore += (checkmateScore + (depth * 100)); // Depth modifier means sooner checkmate = higher score
		}

		return playerScore;

	}

}
