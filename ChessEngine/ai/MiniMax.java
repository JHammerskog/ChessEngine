package ai;

public class MiniMax { // redundant code, refactor later

	State state; // will be replaced with "Board" class when developed

	public MiniMax() {
		

	}

	public int minimax(int depth, int values[], boolean maximizingPlayer, int node) {
		int selectedMove;

		if (maximizingPlayer) {
			selectedMove = maximize(this.state, depth);

		} else {
			selectedMove = minimize(this.state, depth);

		}
		return selectedMove;
	}

	private int maximize(final State state, int depth) {
		int bestMove = Integer.MIN_VALUE; // Best move initially is set to the lowest possible number
		
		// for(LEGAL_MOVES): Make all moves and evaluate new board
		// if one move is yields a higher value than current bestMove, set currentMove
		// to BestMove

		// Implement opposite of above in minimize()

		return bestMove;
	}

	private int minimize(final State state, int depth) {
		int bestFoundValue = Integer.MAX_VALUE; 
		// Best move initially is set to the highest possible number

		return bestFoundValue;
	}

}
