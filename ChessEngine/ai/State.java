package ai;

public class State {

	private int[] testArray = { -3, -2, -1, 1, 2, 3 };

	public int[] getTestArray() {
		return this.testArray;
	}


}

// Until the board class is developed, I'll be using this abstracted class to
// just supply a list of integers for the minimax to play with
