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

		if ((board.getCurrentPlayer().isCheckMate()) && board.getCurrentPlayer() != player) { // Not working,
			playerScore += (checkmateScore - (depth * 10)); // IN my mind, this depth modifier should mean that the checkmate with the lowest depth will have a higher score?
		}

		return playerScore;

	}

}
