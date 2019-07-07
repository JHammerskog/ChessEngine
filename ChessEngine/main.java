public class main {
	
	
	// Incorrectly labelled main, - TESTING AREA

	static int MAX;
	static int MIN;
	static int[] testArray = new int[10];

	public static void main(String[] args) {

		for (int i = 0; i < testArray.length; i++) {
			testArray[i] = i;
		}

		for (int e : testArray) {
			System.out.println(e);
		}

	}

	static int minimax(int depth, int values[], boolean maximizingPlayer, int nodes) {

		if (maximizingPlayer) {
			int bestMove = MAX;

			return bestMove;
		} else {
			int bestMove = MIN;

			return bestMove;
		}

	}

}

// This is the base of the minimax, insert the correct stuff when you've implemented your trees