package board;

import pieces.Piece;

public class Move {

	Player attackingPlayer; //
	Piece movedPiece;
	Tile destinationTile;

	private static int[] firstColumn = generateColumn(0); // maybe int array too slow in grand scale?
	private static int[] secondColumn = generateColumn(1);
	private static int[] seventhColumn = generateColumn(6);
	private static int[] eighthColumn = generateColumn(7);

	private static int numberOfTiles = 64; // Tiles on a chess board
	private static int tilesPerColumn = 8; // Tiles per column on a chess board

	private static int[] generateColumn(int currentTilePosition) { // method to generate an int[] filled with all tiles
																	// in the current column

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

	public static boolean contains(int[] column, int candidateCoordinate) { // method to check a column for a tile
																			// coordinate
		boolean result = false; // needed for exclusion
								// work on finding a better solution for column identification

		for (int i : column) {
			if (i == candidateCoordinate) {
				result = true;
				break;
			}
		}

		return result;
	}

	public static boolean validDestinationTile(int destPosition) {
		return (destPosition >= 0 && destPosition < numberOfTiles);
	}

	public static int getNumberOfTiles() {
		return numberOfTiles;
	}

	public static int[] getFirstColumn() {
		return firstColumn;
	}

	public static int[] getSecondColumn() {
		return secondColumn;
	}

	public static int[] getSeventhColumn() {
		return seventhColumn;
	}

	public static int[] getEighthColumn() {
		return eighthColumn;
	}

}
