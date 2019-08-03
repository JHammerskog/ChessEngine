package board;

/***
 * @author 1200046h This class contains helper methods to do with board logic
 */
public class BoardUtility { // This entire class should be moved to a board utility class

	protected static final int numberOfTiles = 64; // Set to tiles on a chess board

	protected static final int tilesPerColumn = 8; // Set to tiles per column on a chess board

	private static int[] generateColumn(int currentTilePosition) { // Currently redundant, keeping for now

		int[] columnArray = new int[8];
		int tempTile = currentTilePosition;

		while (tempTile > 0) {
			// While loop not necessary at the moment, but may be useful if I want to pass
			// the method a tileCoordinate that isn't on the 8th rank
			tempTile = tempTile - tilesPerColumn;
		}

		for (int i = 0; i < columnArray.length; i++) {
			columnArray[i] = tempTile;
			tempTile = tempTile + tilesPerColumn;
		}
		return columnArray;
	}

	public static boolean contains(int[] column, int candidateCoordinate) { // Currently redundant, keeping for now

		boolean result = false;

		for (int i : column) {
			if (i == candidateCoordinate) {
				result = true;
				break;
			}
		}

		return result;
	}

	public static int calculateColumn(int currentPiecePosition) { // maybe move to Board class?
		int result;

		result = currentPiecePosition % tilesPerColumn;

		return result;
	}

	/*
	 * Although the above method reduces abstraction elsewhere, it only affects
	 * classes which are specifically designed to play chess. If time allows find a
	 * more generic way to evaluate column exceptions.
	 */

	public static boolean validDestinationTile(int destPosition) {
		return (destPosition >= 0 && destPosition < numberOfTiles);
	}

	public static int getNumberOfTiles() {
		return numberOfTiles;
	}

}
